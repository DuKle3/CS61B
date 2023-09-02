package flik;

import org.junit.Test;
import static org.junit.Assert.*;

public class FilkTest {
    @Test
    public void testTheFilk() {
        for (int i = 0, j = 0; i < 1000; i++, j++) {
            boolean isSame = Flik.isSameNumber(i, j);
            assertTrue(isSame);
        }
    }
}
