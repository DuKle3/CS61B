package byow.Core;

import byow.InputDemo.InputSource;
import byow.InputDemo.StringInputDevice;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.io.*;
import java.util.Random;

import static byow.Core.Engine.HEIGHT;
import static byow.Core.Engine.WIDTH;
import static byow.Core.Engine.SCREENWIDTH;
import static byow.Core.Engine.SCREENHEIGHT;

public class Game {
    public static final int MENU = 1;
    public static final int ENTERSEED = 2;
    public static final int PLAY = 3;
    public static final int KEYBOARD = 1;
    public static final int STRING = 2;
    int gameState;
    Player player;
    long seed;
    // The display / interaction world.
    TETile[][] world;
    // This copyWorld save the original world of given seed.
    TETile[][] copyWorld;

    // start new
    public Game(long seed) {
        this.gameState = MENU;
        this.seed = seed;
        initGame(seed);
    }

    /**
     * Create World. and Set variables.
     */
    public void initGame(long seed) {
        WorldGenerator g = new WorldGenerator(WIDTH, HEIGHT, seed);
        // Generate World
        world = g.generateWorld();
        player = g.player;
        copyWorld = TETile.copyOf(world);
        copyWorld[g.player.x][g.player.y] = Tileset.FLOOR;
    }

    public void interaction(Character c) {
        playerMove(c);
    }

    public void playerMove(Character input) {
        switch (input) {
            case 'W' -> playerMoveTo(player.x, player.y + 1);
            case 'A' -> playerMoveTo(player.x - 1, player.y);
            case 'S' -> playerMoveTo(player.x, player.y - 1);
            case 'D' -> playerMoveTo(player.x + 1, player.y);
        }
    }

    public void playerMoveTo(int x, int y) {
        if (isWalkable(x, y)) {
            world[x][y] = player.tile;
            world[player.x][player.y] = copyWorld[player.x][player.y];
            player.x = x;
            player.y = y;
        }
    }

    public boolean isWalkable(int x, int y) {
        return (world[x][y].equals(Tileset.FLOOR) || world[x][y].equals(Tileset.UNLOCKED_DOOR));
    }

    public TETile[][] getWorld() {
        return this.world;
    }
}
