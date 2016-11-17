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
		assertEquals("��", j.searchValue('��', DataType.kHangul, ""));
		assertEquals("��", j.searchValue('ʫ', DataType.kHangul, ""));
		assertEquals("��", j.searchValue('��', DataType.kHangul, ""));
	}
	
	@Test
	public final void testSearch() {
		j.searchValue('��');
	}
	
}
