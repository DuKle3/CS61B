package deque;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArrayDequeTest {
    @Test
    public void addIsEmptySizeTest() {
        ArrayDeque<String> lld2 = new ArrayDeque<>();
        assertTrue("new deque should be empty", lld2.isEmpty());
        lld2.addFirst("front");
        
        assertEquals(1, lld2.size());
        assertFalse("lld2 should contain 2 item", lld2.isEmpty());

        lld2.addLast("middle");
        assertEquals(2, lld2.size());

        lld2.addLast("back");
        assertEquals(3, lld2.size());

        System.out.println("Printing out deque: ");
        lld2.printDeque();
    }
    
    @Test
    public void addRemoveTest() {
        ArrayDeque<Integer> lld2 = new ArrayDeque<>();
        assertTrue("lld2 should be empty", lld2.isEmpty());
        
        lld2.addFirst(100);
        assertFalse("lld2 should be empty", lld2.isEmpty());
        
        lld2.removeFirst();
        assertTrue("lld2 should be empty", lld2.isEmpty());
    }
    
    @Test
    public void removeEmptyTest() {
        ArrayDeque<Integer> lld2 = new ArrayDeque<>();
        lld2.addFirst(100);

        lld2.removeFirst();
        lld2.removeFirst();
        lld2.removeFirst();
        lld2.removeFirst();
        
        int size = lld2.size();
        assertEquals("Bad size, return: " + size,0, size);
    }
    
    @Test
    public void multipleParamTest() {
        ArrayDeque<String> lld1 = new ArrayDeque<>();
        ArrayDeque<Double> lld2 = new ArrayDeque<>();
        ArrayDeque<Boolean> lld3 = new ArrayDeque<>();
        
        lld1.addFirst("string");
        lld2.addFirst(3.14159);
        lld3.addFirst(true);
        
        String s = lld1.removeFirst();
        double d = lld2.removeFirst();
        boolean b = lld3.removeFirst();
    }
    @Test
    public void emptyNullReturnTest() {
        ArrayDeque<Integer> lld1 = new ArrayDeque<>();
        
        assertEquals("Should return null, when removeFirst is called on an empty Deque.", null, lld1.removeFirst());
        assertEquals("Should return null, when removeFirst is called on an empty Deque.", null, lld1.removeLast());
    }
    @Test
    public void bigADequeTest() {
        ArrayDeque<Integer> lld1 = new ArrayDeque<>();
        for (int i = 0; i < 100000; i++) {
            lld1.addLast(i);
        }
        for (int i = 0; i < 80000; i++) {
            assertEquals("Should have the same value", i, (int) lld1.removeFirst());
        }
    }
}
