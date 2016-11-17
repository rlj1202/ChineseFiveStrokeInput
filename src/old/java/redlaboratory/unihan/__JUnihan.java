package redlaboratory.unihan;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

/**
 * �ι�° ������ JUnihan.<br>
 * ����� ������ ����.<br>
 * <br>
 * �ؽ�Ʈ ���Ͽ��� �����ڵ� �ϳ��� ó�� �����ϴ� ���� ��ȣ�� ���������� �����ϴ� ���� ��ȣ�� �̿���
 * index�� length�� ����� �����Ѵ�.<br>
 * <br>
 * �˻��� ���� index ���� index + length ���� �˻��Ѵ�.
 * <pre>
 * {@code
 * ...
 * U+0000 type0 value # ����
 * U+0000 type1 value
 * U+0001 type0 value # ����
 * U+0001 type1 value
 * U+0002 type0 value # ����
 * U+0002 type1 value
 * U+0002 type2 value
 * ...}
 * </pre>
 * �ϴ� �ε� ��ü�� �ſ� ���� ���̴�. ��ȣ�� ����صθ� �Ǳ� �����ΰ� ����.
 * �� � ���� ���� �����ϴ°��� ������ ���� ������ �� ���� �ȿ��� �˻��ϴµ� �ð��� �ʹ� ���� �ɸ���.(10�� �˻��� 1�� ������ �Һ��Ѵ�.)
 * �ƹ����� ���� �� ����� ���� ����̰ų�(���Խ� �̿�) ���� ���ڿ� ���ϴ� �۾� ��ü�� ���ҽ��� ���� ��ƸԴ°� ����.(���Խ� ��� String::startWith �� ����ϸ� 0.2�� ������ ����Ǳ�� �ϳ� ���� ��� ������.)
 * @author ����
 *
 */
@Deprecated
public class __JUnihan {
	
	private HashMap<TableType, HashMap<Integer, Integer>> indices;
	private HashMap<TableType, HashMap<Integer, Integer>> lengthes;
	private boolean ready;
	
	public __JUnihan() {
		indices = new HashMap<TableType, HashMap<Integer, Integer>>();
		lengthes = new HashMap<TableType, HashMap<Integer, Integer>>();
		ready = false;
	}
	
	public void initialize() {
		for (TableType tableType : TableType.values()) load(tableType);
		ready = true;
	}
	
	private BufferedReader getBufferedReader(TableType tableType) throws UnsupportedEncodingException {
		return new BufferedReader(new InputStreamReader(__JUnihan.class.getResourceAsStream(tableType.getFileName().concat(".txt")), "utf-8"));
	}
	
	private void load(TableType tableType) {
		HashMap<Integer, Integer> indices = new HashMap<Integer, Integer>();
		HashMap<Integer, Integer> lengthes = new HashMap<Integer, Integer>();
		this.indices.put(tableType, indices);
		this.lengthes.put(tableType, lengthes);
		
		try {
			BufferedReader br = getBufferedReader(tableType);
			
			String str;
			int line = 0;
			int previousLine = 0;
			int previousUnicode = -1;
			
			while ((str = br.readLine()) != null) {
				if (!str.startsWith("#") && !str.isEmpty()) {
					String unicodeStr = str.substring(0, str.indexOf('	'));
					int unicode = Integer.parseInt(unicodeStr.substring(2), 16);
					
					if (previousUnicode != unicode) {
						lengthes.put(previousUnicode, line - previousLine);
						previousUnicode = unicode;
						previousLine = line;
						indices.put(unicode, line);
					}
					
				}
				
				line++;
			}
			
			br.close();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String find(DataType dataType, int unicode) {
		int index = indices.get(dataType.getTableType()).get(unicode);
		int length = lengthes.get(dataType.getTableType()).get(unicode);
		
		try {
			Stream<String> lines = getBufferedReader(dataType.getTableType()).lines();
			
			/*
			 * ^U\\+(?i)hexUnicode	dataType	.+$
			 */
			String line;
			try {
				String unicodeStr = Integer.toHexString(unicode).toUpperCase();
//				line = lines.skip(index).limit(length).filter((str) -> {return str.matches("^U\\+".concat(unicodeStr).concat("	").concat(dataType.toString()).concat("	.+$"));}).findFirst().get();
				line = lines.skip(index).limit(length).filter((str) -> {return str.startsWith("U+".concat(unicodeStr).concat("	").concat(dataType.toString()));}).findFirst().get();
			} catch (NoSuchElementException e) {
				return null;
			}
			
			int firstTab = line.indexOf('	');
			int secondTab = line.indexOf('	', firstTab + 1);
			
			return line.substring(secondTab + 1);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public boolean isReady() {
		return ready;
	}
	
}
