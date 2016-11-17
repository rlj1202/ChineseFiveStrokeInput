package redlaboratory.unihan;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

/**
 * 두번째 제작한 JUnihan.<br>
 * 방식은 다음과 같다.<br>
 * <br>
 * 텍스트 파일에서 유니코드 하나당 처음 등장하는 줄의 번호와 마지막으로 등장하는 줄의 번호를 이용해
 * index와 length를 만들고 저장한다.<br>
 * <br>
 * 검색할 때는 index 부터 index + length 까지 검색한다.
 * <pre>
 * {@code
 * ...
 * U+0000 type0 value # 여기
 * U+0000 type1 value
 * U+0001 type0 value # 여기
 * U+0001 type1 value
 * U+0002 type0 value # 여기
 * U+0002 type1 value
 * U+0002 type2 value
 * ...}
 * </pre>
 * 일단 로딩 자체는 매우 빠른 편이다. 번호만 기록해두면 되기 때문인것 같다.
 * 또 어떤 범위 내에 접근하는것은 문제가 되지 않지만 그 범위 안에서 검색하는데 시간이 너무 오래 걸린다.(10개 검색시 1초 가량을 소비한다.)
 * 아무래도 내가 쓴 방법이 느린 방법이거나(정규식 이용) 원래 문자열 비교하는 작업 자체가 리소스를 많이 잡아먹는것 같다.(정규식 대신 String::startWith 를 사용하면 0.2로 정도로 단축되기는 하나 눈에 띄게 느리다.)
 * @author 지수
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
