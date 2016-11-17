package redlaboratory.unihan;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

public class JUnihanTest {
	
	private static JUnihan j;
	
	@BeforeClass
	public final static void testInitailize() {
		j = JUnihan.getInstance();
		j.initialize();
	}
	
	@Test
	public final void testSearchValue() {
		assertEquals("ΎΦ", j.searchValue('δρ', DataType.kHangul, ""));
		assertEquals("°‘", j.searchValue('Κ«', DataType.kHangul, ""));
		assertEquals("ΐΟ", j.searchValue('μι', DataType.kHangul, ""));
	}
	
	@Test
	public final void testSearch() {
		j.searchValue('δρ');
	}
	
}
