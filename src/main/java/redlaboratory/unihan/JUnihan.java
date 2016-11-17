package redlaboratory.unihan;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.BiFunction;

/**
 * 최종적으로 만든 JUnihan. 로딩이나 퍼포먼스 둘다 매우 빠른편이다.
 * 로딩속도는 1초를 좀 넘고 검색속도는 첫번째와 비슷하게 매우 매우 빠르다.(누르면 바로바로 나온다.)
 * <p>
 * 일단 로딩은 줄 하나하나를 DataSet이라는 인스턴스로 만들어 저장한다.
 * <p>
 * 이후 검색은 StrokeDatabase 처럼 이분법검색을 이용하여 검색한다.
 * 이미 안의 내용이 일정한 규칙으로 저장되어 있음을 이용한다.
 * <p>
 * 순서는 당연히 1순위는 유니코드, 이후는 데이터 타입으로 구분한다.
 * 하나의 테이블 안에서 타입마다 순서를 정해주었다.
 * <pre>
 * {@code
 * dataSetComparator = (a, b) -> {
 * 		if (a.unicode == b.unicode) {
 * 			return a.dataType.getOrder() < b.dataType.getOrder();
 * 		} else {
 * 			return a.unicode < b.unicode;
 * 		}
 * 	};
 * }
 * </pre>
 * @author 지수
 * @see {@link http://www.unicode.org/}
 */
public class JUnihan {
	
	private static BiFunction<DataSet, DataSet, Boolean> dataSetComparator = null;
	private static JUnihan instance = null;
	
	static {
		dataSetComparator = (a, b) -> {// It represents the priority of two inputs. (Is a higher than b?)
			if (a.unicode == b.unicode) return a.dataType.getOrder() < b.dataType.getOrder();
			else return a.unicode < b.unicode;
		};
	}
	
	public static class DataSet {
		
		public final int unicode;
		public final DataType dataType;
		public final String value;
		
		public DataSet(int unicode, DataType dataType, String value) {
			this.unicode = unicode;
			this.dataType = dataType;
			this.value = value;
		}
		
		public boolean equals(DataSet dataSet) {
			return unicode == dataSet.unicode && dataType.equals(dataSet.dataType);
		}
		
		public boolean hasHigherPriority(DataSet dataSet) {
			if (unicode == dataSet.unicode) return dataType.getOrder() < dataSet.dataType.getOrder();
			else return unicode < dataSet.unicode;
		}
		
		@Override
		public String toString() {
			return "{character:" + (char) unicode + ", unicode:" + Integer.toHexString(unicode) + ", tableType:" + dataType.getTableType().toString() + ", dataType:" + dataType.toString() + ", value:" + value + "}";
		}
		
	}
	
	private HashMap<TableType, List<DataSet>> datas;
	private HashMap<TableType, List<int[]>> indices;
	
	private JUnihan() {
		datas = new HashMap<TableType, List<DataSet>>();
		indices = new HashMap<TableType, List<int[]>>();
	}
	
	public void initialize() {
		for (TableType tableType : TableType.values()) initialize(tableType);
	}
	
	public void initialize(TableType tableType) {
		datas.put(tableType, load(JUnihan.class.getResourceAsStream(tableType.getFileName().concat(".txt"))));
		indices.put(tableType, getIndices(datas.get(tableType)));
	}
	
	public boolean isInitialized(TableType tableType) {
		return datas.containsKey(tableType) && indices.containsKey(tableType);
	}
	
	public List<DataSet> getDataSets(TableType tableType) {
		if (!isInitialized(tableType)) initialize(tableType);
		
		return datas.get(tableType);
	}
	
	public List<int[]> getIndices(TableType tableType) {
		if (!isInitialized(tableType)) initialize(tableType);
		
		return indices.get(tableType);
	}
	
	public boolean hasDataType(int unicode, DataType dataType) {
		return searchValue(unicode, dataType, null) != null;
	}
	
	/**
	 * Find value for dataType corresponding to a specific unicode parameter.
	 * @param unicode
	 * @param dataType
	 * @param def
	 * @return result value. def for searching failed.
	 */
	public String searchValue(int unicode, DataType dataType, String def) {
		List<DataSet> list = getDataSets(dataType.getTableType());
		
		DataSet tmp = new DataSet(unicode, dataType, null);
		
		int start = 0;
		int mid = list.size() / 2;
		int end = list.size() - 1;
		
		while (true) {
			DataSet target = list.get(mid);
			
			if (start > end) {
				System.out.println("RESULT[JUnihan]: for " + tmp.toString() + ", start:" + start + ", mid:" + mid + ", end:" + end + ", failed.");
				
				return def;
			}
			if (tmp.equals(target)) {
				System.out.println("RESULT[JUnihan]: for " + tmp.toString() + ", start:" + start + ", mid:" + mid + ", end:" + end + ", " + target.toString() + ", success.");
				
				return target.value;
			} else if (dataSetComparator.apply(tmp, target)) {
				end = mid - 1;
			} else {
				start = mid + 1;
			}
			
			mid = start + (end - start) / 2;
		}
	}
	
	public List<DataSet> searchValue(int unicode) {
		List<DataSet> result = new ArrayList<DataSet>();
		
		for (TableType tableType : TableType.values()) {
			List<int[]> list = getIndices(tableType);
			
			int index = -1;
			int length = -1;
			
			int start = 0;
			int mid = list.size() / 2;
			int end = list.size() - 1;
			
			while (true) {
				int[] target = list.get(mid);
				
				if (start > end) {
					break;
				}
				if (unicode == target[0]) {
					index = target[1];
					length = target[2];
					
					break;
				} else if (unicode < target[0]) {
					end = mid - 1;
				} else {
					start = mid + 1;
				}
				
				mid = start + (end - start) / 2;
			}
			
			if (index != -1 && length != -1) {
				result.addAll(getDataSets(tableType).subList(index, index + length));
			}
		}
		
		return result;
	}
	
	private List<DataSet> load(InputStream is) {
		List<DataSet> list = new ArrayList<DataSet>();
		
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(is, "utf-8"));
			
			String str;
			while ((str = in.readLine()) != null) {
				if (str.startsWith("#")) continue;
				if (str.isEmpty()) continue;
				
				int tabFirst = str.indexOf('	', 0);
				int tabSecond = str.indexOf('	', tabFirst + 1);
				String str0 = str.substring(0, tabFirst);
				String str1 = str.substring(tabFirst + 1, tabSecond);
				String str2 = str.substring(tabSecond + 1);
				
				int unicode = Integer.parseInt(str0.substring(2), 16);
				DataType dataType = DataType.valueOf(str1);
				String value = str2;
				
				list.add(new DataSet(unicode, dataType, value));
			}
			
			in.close();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	private List<int[]> getIndices(List<DataSet> dataSets) {
		List<int[]> list = new ArrayList<int[]>();
		
		int index = 0;
		int length = 0;
		
		int lastUnicode = dataSets.get(0).unicode;
		for (DataSet dataSet : dataSets) {
			if (lastUnicode != dataSet.unicode) {
				list.add(new int[] {lastUnicode, index, length});
				
				lastUnicode = dataSet.unicode;
				index += length;
				length = 0;
			}
			
			length++;
		}
		
		return list;
	}
	
	public static JUnihan getInstance() {
		return instance != null ? instance : (instance = new JUnihan());
	}
	
}
