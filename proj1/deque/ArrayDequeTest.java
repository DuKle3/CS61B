package deque;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArrayDequeTest {
    @Test
    public void getNextFrontAndLastTest() {
        ArrayDeque<Integer> a = new ArrayDeque<>();
        // 4 5 3 2 9 8 0 7
        a.addLast(9);
        a.addLast(8);
        a.addLast(0);
        a.addLast(7);
        a.addFirst(2);
        a.addFirst(3);
        a.addFirst(5);
        a.addFirst(4);
        int x1 = a.get(2);
        int x2 = a.get(4);
        assertEquals(3, x1);
        assertEquals(9, x2);
        a.addLast(15);
        a.addLast(15);
        a.addLast(15);
        a.addLast(15);
        int y1 = a.removeLast();
        int y2 = a.removeLast();
        assertEquals(y1, 15);
        assertEquals(y2, 15);
        int y3 = a.removeFirst();
        assertEquals(y3, 4);
        a.printDeque();
    }
    @Test
    public void isUsageTest() {
        ArrayDeque<Integer> a = new ArrayDeque<>();
        for (int i = 0; i < 1000; i++) {
            a.addLast(i);
        }
        for (int i = 0; i < 980; i++) {
            a.removeFirst();
        }
        a.printDeque();
        System.out.println(a.itemLength());
    }
}
