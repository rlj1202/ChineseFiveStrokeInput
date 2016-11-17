package redlaboratory.chineseinput.strokedatabase;

import java.util.Comparator;
import java.util.List;

public class DefaultStrokeDatabase implements StrokeDatabase {
	
	private List<Hanzi> hanzis;
	private Comparator<Hanzi> comp;
	
	public DefaultStrokeDatabase(List<Hanzi> hanzis, Comparator<Hanzi> comp) {
		this.hanzis = hanzis;
		this.comp = comp;
	}
	
	@Override
	public Hanzi getHanzi(int index) {
		return hanzis.get(index);
	}
	
	@Override
	public List<Hanzi> getHanzis(int offset, int length) {
		return hanzis.subList(offset, Math.min(offset + length, hanzis.size()));
	}
	
	@Override
	public int searchHanzi(String stroke) {
//		int rawIndex = Arrays.binarySearch(hanzis.toArray(new Hanzi[0]), new Hanzi(stroke, ' '), comp);
//		
//		if (rawIndex < 0) rawIndex = - rawIndex - 1;
//		
//		return rawIndex;
		if (stroke == null || stroke.isEmpty()) return -1;
		
		Hanzi key = new Hanzi(stroke, ' ');
		
		int start = 0;
		int mid = hanzis.size() / 2;
		int end = hanzis.size() - 1;
		
		while (true) {
			Hanzi target = hanzis.get(mid);
			
			if (stroke.equals(target.stroke)) {
				System.out.println("RESULT[StrokeDatabase]: for \'" + stroke +  "\', result is " + hanzis.get(mid).toString() + ", success.");
				
				break;
			} else if (comp.compare(key, target) > 0) {
				end = mid - 1;
			} else {
				start = mid + 1;
			}
			
			mid = start + (end - start) / 2;
			
			if (start > end) {
				System.out.println("RESULT[StrokeDatabase]: for \'" + stroke + "\', result is " + hanzis.get(mid).toString() + ", failed.");
				
				break;
			}
		}
		
		return mid;
	}
	
}
