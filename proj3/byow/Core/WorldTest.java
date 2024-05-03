package byow.Core;

import byow.InputDemo.InputSource;
import byow.InputDemo.KeyboardInputSource;
import byow.InputDemo.StringInputDevice;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;

import static byow.Core.Engine.*;
import static org.junit.Assert.*;

import com.sun.tools.internal.ws.wsdl.document.Input;
import jdk.jshell.execution.Util;
import org.checkerframework.checker.units.qual.K;
import org.junit.Test;

public class WorldTest {

    public static void main(String[] args) {
    }

    @Test
    public void testSameInputWithMovement() {
        Engine g1 = new Engine();
        Engine g2 = new Engine();
        
        g1.interactWithInputString("N12345SWWAASSDD");
        g2.interactWithInputString("N12345SWWAASSDD");

        assertEquals(g1.game.getWorld(), g2.game.getWorld());
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
