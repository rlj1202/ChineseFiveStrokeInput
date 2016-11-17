package redlaboratory.unihan;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

/**
 * ù��° ������ JUnihan.<br>
 * �׳� ������ ������ ������.
 * �ؽ�Ʈ ���� ���� �ؽ����� �����ϰ� �����ڵ� ���� �Ӽ��� �ؽ� ���̺��� �־������.
 * <pre>
 * {@code HashMap<TableType, HashMap<Integer, HashMap<String, String>>>}
 * </pre> 
 * �ε��ð��� 6�� ������ �ſ� �⳪ �˻��� �ſ�ſ� ������.
 * @author ����
 *
 */
@Deprecated
public class _JUnihan {
	
	private HashMap<TableType, HashMap<Integer, HashMap<String, String>>> tables;
	
	public _JUnihan() {
		tables = new HashMap<TableType, HashMap<Integer, HashMap<String, String>>>();
	}
	
	public void initailize() {
		for (TableType tableType : TableType.values()) {
			tables.put(tableType, load(_JUnihan.class.getResourceAsStream(tableType.getFileName().concat(".txt"))));
		}
	}
	
	public String find(DataType dataType, char character) {
		HashMap<Integer, HashMap<String, String>> table = tables.get(dataType.getTableType());
		HashMap<String, String> dataSet = table.get((int) character);
		
		if (dataSet == null) return null;
		
		return dataSet.get(dataType.toString());
	}
	
	public HashMap<Integer, HashMap<String, String>> load(InputStream is) {
		HashMap<Integer, HashMap<String, String>> table = new HashMap<Integer, HashMap<String, String>>();
		
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(is, "utf-8"));
			
			String str;
			int previousUnicode = -1;
			while ((str = in.readLine()) != null) {
				if (str.startsWith("#")) continue;
				if (str.isEmpty()) continue;
				
				String[] strs = str.split("	");
				
				int unicode = Integer.parseInt(strs[0].substring(2), 16);
				String dataType = strs[1];
				String value = strs[2];
				
				if (previousUnicode != unicode) {
					previousUnicode = unicode;
					table.put(unicode, new HashMap<String, String>());
				}
				
				table.get(unicode).put(dataType, value);
			}
			
			in.close();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return table;
	}
	
}
