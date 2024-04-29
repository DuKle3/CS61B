package byow.Core;

import byow.InputDemo.InputSource;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;

import static byow.Core.Engine.HEIGHT;
import static byow.Core.Engine.WIDTH;

public class Game {
    Player player;
    TETile[][] world;
    TETile[][] copyWorld;

    InputSource inputSource;
    long seed;

    // start new
    public Game(InputSource s) {
        this.inputSource = s;
    }

    public void play() {
        // 1. New Game or Load
        while (inputSource.possibleNextInput()) {
            Character c = inputSource.getNextKey();
            switch (c) {
                case 'N' -> {
                    // TODO: Start New Game
                }
                case 'L' -> {
                    // TODO: Load
                }
                case ':' -> {
                    if (inputSource.getNextKey() == 'Q') {
                        // TODO: Save
                    }
                }
                default -> {
                    // TODO:
                    // 1. Move
                    playerMove(c);
                    // 2. Update Frame
                    // 3. Render Frame
                }
            }
        }
        // 2. Parse Seed or Load game state
        // 3. Generate world
        // 4. Interaction
    }

    public void initGame(long seed) {
        WorldGenerator g = new WorldGenerator(WIDTH, HEIGHT, seed);
        // Generate World
        world = g.generateWorld();
        player = g.player;


        copyWorld = TETile.copyOf(world);
    }

    public void drawFrame(String s) {
        // Take the string and display it in the center of the screen
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.clear(Color.BLACK);
        StdDraw.setFont(font);
        StdDraw.setPenColor(Color.WHITE);
//        StdDraw.text( width / 2.0, height / 2.0, s);

        // If game is not over, display relevant game information at the top of the screen
        // TODO: Information

        StdDraw.show();
    }

    public void playerMove(Character input) {
        switch (input) {
            case 'W' -> {
                playerMoveTo(player.x, player.y + 1);
            }
            case 'A' -> {
                playerMoveTo(player.x - 1, player.y);
            }
            case 'S' -> {
                playerMoveTo(player.x, player.y - 1);
            }
            case 'D' -> {
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
