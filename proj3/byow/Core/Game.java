package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import static byow.Core.Engine.HEIGHT;
import static byow.Core.Engine.WIDTH;

public class Game {
    Player player;
    TETile[][] world;
    TETile[][] copyWorld;
    long seed;
    String gameState;

    // start new
    public Game(long seed) {
    }


    // load
    public Game() {
    }

    public void initGame(long seed) {
        WorldGenerator g = new WorldGenerator(WIDTH, HEIGHT, seed);
        TETile[][] world = new TETile[WIDTH][HEIGHT];
        // Initialize world
        g.fillBoardWithNothing(world);
        // Generate World
        g.generateWorld(world);
        player = g.player;
        copyWorld = TETile.copyOf(world);
    }



    public void playerMove(Character input) {
        switch (input) {
            case 'w' -> {
                playerMoveTo(player.x, player.y + 1);
            }
            case 'a' -> {
                playerMoveTo(player.x - 1, player.y);
            }
            case 's' -> {
                playerMoveTo(player.x, player.y - 1);
            }
            case 'd' -> {
                playerMoveTo(player.x + 1, player.y);
            }
        }
    }

    public void playerMoveTo(int x, int y) {
        if (isWalkable(x, y)) {
            world[x][y] = player.tile;
            world[player.x][player.y] = copyWorld[player.x][player.x];
        }
    }

    public boolean isWalkable(int x, int y) {
        if (world[x][y].equals(Tileset.FLOOR) || world[x][y].equals(Tileset.UNLOCKED_DOOR)) {
            return true;
        }
        return false;
    }
}
