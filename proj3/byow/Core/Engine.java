package byow.Core;

import byow.InputDemo.InputSource;
import byow.InputDemo.KeyboardInputSource;
import byow.InputDemo.StringInputDevice;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.io.*;

public class Engine {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 50;
    public static final int SCREENWIDTH = WIDTH;
    public static final int SCREENHEIGHT = HEIGHT + 10;
    public static final int KEYBOARD = 1;
    public static final int STRING = 2;
    int inputType;
    GameState gameState;
    Game game;

    private enum GameState {
        MENU,
        PLAY,
        QUIT
    }

    /**
     * Method used for exploring a fresh world. This method should handle all
     * inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
        // 1. new game or load game
        InputSource inputSource = new KeyboardInputSource();
        this.inputType = KEYBOARD;
        play(inputSource);
    }

    /**
     * Method used for autograding and testing your code. The input string will be a
     * series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The
     * engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     *
     * Recall that strings ending in ":q" should cause the game to quite save. For
     * example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the
     * first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     *
     * In other words, both of these calls:
     * - interactWithInputString("n123sss:q")
     * - interactWithInputString("lww")
     *
     * should yield the exact same world state as:
     * - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) {
        // TODO: Fill out this method so that it run the engine using the input
        // passed in as an argument, and return a 2D tile representation of the
        // world that would have been drawn if the same inputs had been given
        // to interactWithKeyboard().
        //
        // See proj3.byow.InputDemo for a demo of how you can make a nice clean
        // interface
        // that works for many different input types.

        InputSource inputSource = new StringInputDevice(input);
        this.inputType = STRING;
        return play(inputSource);
    }

    public TETile[][] play(InputSource inputSource) {
        if (inputType == KEYBOARD) {
            ter.initialize(SCREENWIDTH, SCREENHEIGHT);
            menu();
        }

        while (inputSource.possibleNextInput()) {
            Character c = inputSource.getNextKey();
            switch (c) {
                case 'N' -> {
                    // start new
                    long seed = solicitSeed(inputSource);
                    this.game = new Game(seed);
                    if (this.inputType == KEYBOARD) {
                        ter.renderFrame(game.world);
                    }
                }
                case 'L' -> {
                    // load
                    this.game = loadGame();
                    if (this.inputType == KEYBOARD) {
                        ter.renderFrame(game.world);
                    }
                }
                case 'Q' -> {
                    // quit
                    return null;
                }
                case ':' -> {
                    if (inputSource.getNextKey() == 'Q') {
                        // save
                        saveGame();
                        return game.getWorld();
                    }
                }
                default -> {
                    // interaction
                    this.game.interaction(c);
                    if (this.inputType == KEYBOARD) {
                        this.ter.renderFrame(game.world);
                    }
                }
            }
        }
        return game.getWorld();
    }

    public void menu() {
        StdDraw.clear(Color.BLACK);
        Font TitleFont = new Font("JetBrains Mono", Font.BOLD, 45);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.setFont(TitleFont);
        StdDraw.text(SCREENWIDTH / 2.0, SCREENHEIGHT - 15, "CS61B: The Game");

        Font ContentFont = new Font("JetBrains Mono", Font.PLAIN, 30);
        StdDraw.setFont(ContentFont);
        StdDraw.text(SCREENWIDTH / 2.0, 21, "New Game (N)");
        StdDraw.text(SCREENWIDTH / 2.0, 18, "Load Game (L)");
        StdDraw.text(SCREENWIDTH / 2.0, 15, "Quit (Q)");
        StdDraw.show();
    }

    public long solicitSeed(InputSource inputSource) {
        if (inputType == KEYBOARD) {
            drawFrame("Enter Seed: ");
        }

        // Parse the Seed.
        String typed = "";
        while (inputSource.possibleNextInput()) {
            Character c = inputSource.getNextKey();
            if (c >= '0' && c <= '9') {
                typed += c;
                if (inputType == KEYBOARD) {
                    drawFrame("Enter Seed: " + typed);
                }
            }
            if (c == 'S') {
                break;
            }
        }
        return Long.parseLong(typed);
    }

    public void drawFrame(String s) {
        // Take the string and display it in the center of the screen
        Font font = new Font("JetBrains Mono", Font.BOLD, 30);
        StdDraw.clear(Color.BLACK);
        StdDraw.setFont(font);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(SCREENWIDTH / 2.0, SCREENHEIGHT / 2.0, s);
        StdDraw.text(SCREENWIDTH / 2.0, SCREENHEIGHT / 2.0 - 5, "Start(S)");

        StdDraw.show();
    }

    private void saveGame() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("lastGame.txt"))) {
            writer.println(this.game.seed);
            writer.println(this.game.player.x);
            writer.println(this.game.player.y);
            System.out.println("success save");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Game loadGame() {
        String seed = null;
        String playerX = null;
        String playerY = null;
        try (BufferedReader reader = new BufferedReader(new FileReader("lastGame.txt"))) {
            seed = reader.readLine();
            playerX = reader.readLine();
            playerY = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Game loadGame = new Game(Long.parseLong(seed));
        int x = Integer.parseInt(playerX);
        int y = Integer.parseInt(playerY);
        loadGame.playerMoveTo(x, y);
        loadGame.player.x = x;
        loadGame.player.y = y;
        return loadGame;
    }
}
