package byow.Core;

import byow.InputDemo.InputSource;
import byow.InputDemo.KeyboardInputSource;
import byow.InputDemo.StringInputDevice;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import static org.junit.Assert.*;
import static byow.Core.Engine.WIDTH;
import static byow.Core.Engine.HEIGHT;

import com.sun.tools.internal.ws.wsdl.document.Input;
import jdk.jshell.execution.Util;
import org.checkerframework.checker.units.qual.K;
import org.junit.Test;

public class WorldTest {

    public static void main(String[] args) {
        WorldGenerator wg = new WorldGenerator(50, 50, 123455);
        WorldGenerator wg2 = new WorldGenerator(50, 50, 432623);
        TERenderer ter = new TERenderer();
        ter.initialize(100, 50 ,10, 10);
        TETile[][] world = wg.generateWorld();
        TETile[][] world2 = wg2.generateWorld();
        ter.renderFrame(world);

        InputSource s = new KeyboardInputSource();
        while (s.possibleNextInput()) {
            if (s.getNextKey() == 'N') {
                ter.renderFrame(world2);
            }
        }
    }

    @Test
    public void testStringToSeed() {
        long seed = Utils.parseTheSeed(new StringInputDevice("N12345S"));
        assertEquals(seed, 12345);
    }

    @Test
    public void testStringToAction() {
        String actionString = Utils.parseTheAction("N12345Swwaassdd:Q");
        assertEquals("WWAASSDD:Q", actionString);
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
