package byow.Core;

import byow.TileEngine.TETile;
import edu.princeton.cs.introcs.StdDraw;
import org.junit.Test;
import static org.junit.Assert.*;

public class WorldTest {

    public static void main(String[] args) {
        Engine g1 = new Engine();
        g1.interactWithKeyboard();
    }

    @Test
    public void testSameInputWithMovement() {
        Engine g1 = new Engine();
        Engine g2 = new Engine();

        g1.interactWithInputString("n7313251667695476404sasdw");
        g2.interactWithInputString("n7313251667695476404sasdw");

        assertEquals(g1.game.getWorld(), g2.game.getWorld());
    }

    @Test
    public void testSameInputSameWorld() {
        Engine g = new Engine();
        String seed = "N1298573S";
        TETile[][] world = g.interactWithInputString(seed);
        TETile[][] world2 = g.interactWithInputString(seed);

        boolean flag = true;
        int width = world.length;
        int height = world[0].length;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (world[i][j] != world2[i][j]) {
                    flag = false;
                    break;
                }
            }
        }
        assertTrue(flag);
    }
}
