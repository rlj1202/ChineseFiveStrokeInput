package redlaboratory.unihan;

import static org.junit.Assert.*;

import org.junit.Test;

public class JUnihanBootingTest {

	@Test
	public final void testInitialize() {
		JUnihan.getInstance().initialize();
		assertNull(null);
	}

}
