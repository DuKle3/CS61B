package randomizedtest;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by hug.
 */
public class TestBuggyAList {
  // YOUR TESTS HERE
    @Test
    public void testThreeAddThreeRemove() {
        BuggyAList<Integer> b = new BuggyAList<>();
        AListNoResizing<Integer> a = new AListNoResizing<>();
        b.addLast(4);
        b.addLast(5);
        b.addLast(6);
        a.addLast(4);
        a.addLast(5);
        a.addLast(6);
        assertEquals(a.removeLast(), b.removeLast());
    }
    @Test
    public void randomizedTest() {
        AListNoResizing<Integer> L = new AListNoResizing<>();
        BuggyAList<Integer> broken = new BuggyAList<>();

        int N = 5000;
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 4);
            if (operationNumber == 0) {
                // addLast
                int randVal = StdRandom.uniform(0, 100);
                L.addLast(randVal);
                broken.addLast(randVal);
            } else if (operationNumber == 1) {
                // size
                int size = L.size();
                int b_size = broken.size();
                assertEquals(size, b_size);
            } else if (operationNumber == 2 && L.size() > 0) {
                int value = L.getLast();
                int b_value = broken.getLast();
                assertEquals(value, b_value);
            } else if (operationNumber == 3 && L.size() > 0) {
                int value = L.removeLast();
                int b_remove = broken.removeLast();
                assertEquals(value, b_remove);
            }
        }
    }
}
