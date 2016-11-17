package redlaboratory.unihan;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

/**
 * 첫번째 제작한 JUnihan.<br>
 * 그냥 모조리 저장해 버린다.
 * 텍스트 파일 마다 해쉬맵을 생성하고 유니코드 마다 속성값 해쉬 테이블을 넣어버린다.
 * <pre>
 * {@code HashMap<TableType, HashMap<Integer, HashMap<String, String>>>}
 * </pre> 
 * 로딩시간은 6초 쯤으로 매우 기나 검색은 매우매우 빠르다.
 * @author 지수
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
