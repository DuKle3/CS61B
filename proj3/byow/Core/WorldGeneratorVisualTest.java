package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.lab12.HexWorld;

public class WorldGeneratorVisualTest {
    private static final int WIDTH = 50;
    private static final int HEIGHT = 50;


    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        TETile[][] world = new TETile[WIDTH][HEIGHT];

        // Initialize world
        WorldGenerator.fillBoardWithNothing(world);

        // Generate World
        WorldGenerator.generateWorld(world);

        ter.renderFrame(world);
    }
}
