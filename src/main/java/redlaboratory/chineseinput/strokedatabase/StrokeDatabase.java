package redlaboratory.chineseinput.strokedatabase;

import java.util.List;

public interface StrokeDatabase {
	
	Hanzi getHanzi(int index);
	
	List<Hanzi> getHanzis(int offset, int length);
	
	int searchHanzi(String stroke);
	
}
