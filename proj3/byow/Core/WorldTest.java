package byow.Core;

import byow.InputDemo.InputSource;
import byow.InputDemo.StringInputDevice;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import static org.junit.Assert.*;
import org.junit.Test;

public class WorldTest {
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;

    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        TETile[][] world = new TETile[WIDTH][HEIGHT];

        WorldGenerator g = new WorldGenerator(WIDTH, HEIGHT, 13);

        // Initialize world
        g.fillBoardWithNothing(world);

        // Generate World
        g.generateWorld(world);

        ter.renderFrame(world);
    }

    @Test
    public void testStringToSeed() {
        InputSource inputSource = new StringInputDevice("N12345S");
        long seed = 0;
        while (inputSource.possibleNextInput()) {
            char c = Character.toUpperCase(inputSource.getNextKey());
            if (c == 'N') {
                continue;
            }
            if (c == 'S') {
                break;
            }
            seed = seed * 10 + Character.getNumericValue(c);
        }
        assertEquals(seed, 12345);
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
