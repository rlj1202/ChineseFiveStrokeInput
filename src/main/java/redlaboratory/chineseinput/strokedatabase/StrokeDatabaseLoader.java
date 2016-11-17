package redlaboratory.chineseinput.strokedatabase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class StrokeDatabaseLoader {
	
	private List<Hanzi> hanzis;
	
	private StrokeDatabaseLoader(List<Hanzi> hanzis) {
		this.hanzis = hanzis;
	}
	
	public List<Hanzi> getHanzis() {
		return hanzis;
	}
	
	public StrokeDatabaseLoader sort(Comparator<Hanzi> comp) {
		return new StrokeDatabaseLoader(sort(hanzis, comp));
	}
	
	private List<Hanzi> sort(List<Hanzi> hanzis, Comparator<Hanzi> comp) {
		if (hanzis.size() == 1) return hanzis;
		
		int mid = hanzis.size() / 2;
		List<Hanzi> leftArray = sort(hanzis.subList(0, mid), comp);
		List<Hanzi> rightArray = sort(hanzis.subList(mid, hanzis.size()), comp);
		
		int left = 0;
		int right = 0;
		
		List<Hanzi> result = new ArrayList<Hanzi>();
		
		while (true) {
			if (left >= leftArray.size()) {
				result.addAll(rightArray.subList(right, rightArray.size()));
				
				break;
			}
			if (right >= rightArray.size()) {
				result.addAll(leftArray.subList(left, leftArray.size()));
				
				break;
			}
			
			Hanzi leftChar = leftArray.get(left);
			Hanzi rightChar = rightArray.get(right);
			
			if (comp.compare(leftChar, rightChar) >= 0) {
				result.add(leftChar);
				left++;
			} else {
				result.add(rightChar);
				right++;
			}
		}
		
		return result;
	}
	
	public static StrokeDatabaseLoader load(InputStream is, String charsetName) throws IOException {
		List<Hanzi> hanzis = new ArrayList<Hanzi>();
		
		BufferedReader br = new BufferedReader(new InputStreamReader(is, charsetName));
		
		String buf;
		while ((buf = br.readLine()) != null) {
			if (buf.isEmpty() || buf.startsWith("#")) continue;
			
			String[] strs = buf.split("	");
			
			String stroke = strs[0];
			char character = strs[1].charAt(0);
			
			Hanzi hanzi = new Hanzi(stroke, character);
			hanzis.add(hanzi);
		}
		
		return new StrokeDatabaseLoader(hanzis);
	}
	
}
