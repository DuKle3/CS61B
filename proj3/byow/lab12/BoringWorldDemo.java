package byow.lab12;

import byow.Core.DepthFirstPaths;
import byow.Core.Position;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.awt.*;


/**
 *  Draws a world that is mostly empty except for a small region.
 */
public class BoringWorldDemo {

    private static final int WIDTH = 60;
    private static final int HEIGHT = 30;

    public static void main(String[] args) {
        // initialize the tile rendering engine with a window of size WIDTH x HEIGHT
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        // initialize tiles
        TETile[][] world = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }

        // fills in a block 14 tiles wide by 4 tiles tall
        for (int x = 0; x < 7; x += 1) {
            for (int y = 0; y < 7; y += 1) {
                world[x][y] = Tileset.WALL;
            }
        }

        for (int x = 1; x < 6; x += 1) {
            for (int y = 1; y < 6; y++) {
                world[x][y] = Tileset.FLOOR;
            }
        }

        for (int x = 2; x < 6; x++) {
            world[x][2] = Tileset.WALL;
        }

        for (int x = 1; x < 5; x++) {
            world[x][4] = Tileset.WALL;
        }

        world[1][5] = Tileset.AVATAR1;
        world[5][1] = Tileset.ENEMY;

        // draws the world to the screen
        Position playerP = new Position(1, 5);
        Position enemyP = new Position(5, 1);

        ter.renderFrame(world);
    }
}
