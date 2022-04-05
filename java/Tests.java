import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

@RunWith(JUnit4.class)
public class Tests {
	@Test
	public void testTST() {
		TST tst = new TST();

		tst.insert("cat");
	}
}
