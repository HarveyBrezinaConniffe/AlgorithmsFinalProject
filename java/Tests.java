import java.util.ArrayList;

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

		// Insert to tree.
		tst.insert("cat");
		tst.insert("car");
		tst.insert("carrot");
		tst.insert("dog");
		tst.insert("dome");
		tst.insert("house");

		ArrayList<String> predictions = tst.autocomplete("c");
		ArrayList<String> correctCompletions = new ArrayList<String>();
		correctCompletions.add("at");
		correctCompletions.add("ar");
		correctCompletions.add("arrot");
		assertEquals("Testing TST autocomplete.", correctCompletions, predictions);

		predictions = tst.autocomplete("do");
		correctCompletions = new ArrayList<String>();
		correctCompletions.add("g");
		correctCompletions.add("me");

		predictions = tst.autocomplete("car");
		correctCompletions = new ArrayList<String>();
		correctCompletions.add("");
		correctCompletions.add("rot");
	}
}
