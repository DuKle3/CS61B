package deque;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Comparator;

public class MaxArrayDequeTest {

    /** Test the max() and max(Comparator c) */
    @Test
    public void comparatorTest() {
        Comparator<String> c1 = new Comparator<>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        };

        MaxArrayDeque<String> s1 = new MaxArrayDeque<>(c1);
        assertTrue(s1.isEmpty());

        s1.addFirst("First");
        s1.addFirst("Second");
        s1.addFirst("Third");
        s1.addFirst("Apple");
        Object test = s1.max();
        assertEquals("Third", test);

        Comparator<String> c2 = new Comparator<>() {
            @Override
            public int compare(String o1, String o2) {
                return -1 * o1.compareTo(o2);
            }
        };

        Object test2 = s1.max(c2);
        assertEquals("Apple", test2);

        s1.addFirst("Zebra");
        assertEquals("Zebra", s1.max());
        assertEquals(5, s1.size());
    }
}
