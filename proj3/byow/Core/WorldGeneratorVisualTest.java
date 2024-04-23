package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.lab12.HexWorld;

public class WorldGeneratorVisualTest {
    private static final int WIDTH = 80;
    private static final int HEIGHT = 50;


    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        TETile[][] world = new TETile[WIDTH][HEIGHT];

        WorldGenerator g = new WorldGenerator(WIDTH, HEIGHT, 1235);

        // Initialize world
        g.fillBoardWithNothing(world);

        // Generate World
        g.generateWorld(world);

        ter.renderFrame(world);
    }
}
