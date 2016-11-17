package redlaboratory.chineseinput.strokedatabase;

public class Hanzi {
	
	public final String stroke;
	public final char character;
	
	public Hanzi(String stroke, char character) {
		this.stroke = stroke;
		this.character = character;
	}
	
	public boolean equals(Hanzi hanzi) {
		return stroke.equals(hanzi.stroke) && character == hanzi.character;
	}
	
	@Override
	public String toString() {
		return "{stroke:" + stroke + ", character:" + character + "}";
	}
	
}
