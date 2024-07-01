package byow.Core;

import byow.InputDemo.InputSource;
import byow.InputDemo.KeyboardInputSource;
import byow.InputDemo.StringInputDevice;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;
import net.sf.saxon.trans.SymbolicName;
import org.checkerframework.checker.units.qual.C;

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
    String actionString = "";
    Game game;
    TETile avatar = Tileset.AVATAR1;

    private enum GameState {
        MENU,
        PLAY,
        SAVE,
        CHANGE,
        CHOOSESLOT,
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

        InputSource inputSource = new StringInputDevice(input.toUpperCase());
        this.inputType = STRING;
        return play(inputSource);
    }

    public TETile[][] play(InputSource inputSource) {
        GameState state = GameState.MENU;

        if (inputType == KEYBOARD) {
            ter.initialize(SCREENWIDTH, SCREENHEIGHT);
            drawStartMenu();
        }


        while (inputSource.possibleNextInput()) {
            Character c = inputSource.getNextKey();
            actionString = actionString + c;
            switch (state) {
                case MENU -> {
                    switch (c) {
                        case 'N' -> {
                            // start new
                            long seed = solicitSeed(inputSource);
                            this.game = new Game(seed, avatar);
                            if (inputType == KEYBOARD) {
                                ter.renderFrame(game.getWorld());
                            }
                            state = GameState.PLAY;
                        }
                        case 'C' -> {
                            // change avatar appearance
                            state = GameState.CHANGE;
                            if (inputType == KEYBOARD) {
                                drawChangeAvatar();
                            }
                        }
                        case 'L' -> {
                            // load
                            this.game = loadGame();
                            if (this.inputType == KEYBOARD) {
                                ter.renderFrame(game.getWorld());
                            }
                            state = GameState.PLAY;
                        }
                        case 'T' -> {
                            state = GameState.CHOOSESLOT;
                            if (inputType == KEYBOARD) {
                                drawLoadSlot();
                            }
                        }
                        case 'Q' -> {
                            // quit
                            return null;
                        }
                    }
                }
                case SAVE -> {
                    switch (c) {
                        case 'Q' -> {
                            // save
                            saveGame();
                            return game.getWorld();
                        }
                        case '1' -> {
                            // save slot 1
                            saveGame("1");
                            return game.getWorld();
                        }
                        case '2' -> {
                            // save slot 1
                            saveGame("2");
                            return game.getWorld();
                        }
                        case '3' -> {
                            // save slot 1
                            saveGame("3");
                            return game.getWorld();
                        }
                    }
                }
                case PLAY -> {
                    switch (c) {
                        case ':' -> {
                            state = GameState.SAVE;
                            if (inputType == KEYBOARD) {
                                drawSaveMenu();
                            }
                        }
                        default -> {
                            // interaction
                            game.interaction(c);

                            // TODO:　enemy move

                            if (inputType == KEYBOARD) {
                                ter.renderFrame(game.getWorld());
                                drawHUD(getMousePos(), game.getWorld());
                            }
                        }
                    }
                }
                case CHANGE -> {
                    switch (c) {
                        case '1' -> {
                            avatar = Tileset.AVATAR1;
                        }
                        case '2' -> {
                            avatar = Tileset.AVATAR2;
                        }
                        case '3' -> {
                            avatar = Tileset.AVATAR3;
                        }
                    }
                    drawStartMenu();
                    state = GameState.MENU;
                }
                case CHOOSESLOT -> {
                    switch (c) {
                        case '1' -> {
                            this.game = loadGame("1");
                            state = GameState.PLAY;
                            if (this.inputType == KEYBOARD) {
                                ter.renderFrame(game.getWorld());
                            }
                        }
                        case '2' -> {
                            this.game = loadGame("2");
                            state = GameState.PLAY;
                            if (this.inputType == KEYBOARD) {
                                ter.renderFrame(game.getWorld());
                            }
                        }
                        case '3' -> {
                            this.game = loadGame("3");
                            state = GameState.PLAY;
                            if (this.inputType == KEYBOARD) {
                                ter.renderFrame(game.getWorld());
                            }
                        }
                        case 'B' -> {
                            state = GameState.MENU;
                            if (inputType == KEYBOARD) {
                                drawStartMenu();
                            }
                        }
                    }
                }
            }
        }

        if (game != null) {
            return game.getWorld();
        }
        return null;
    }

    public long solicitSeed(InputSource inputSource) {
        if (inputType == KEYBOARD) {
            drawFrame("Enter Seed: ");
        }

        // Parse the Seed.
        String typed = "";
        while (inputSource.possibleNextInput()) {
            Character c = inputSource.getNextKey();
            actionString = actionString + c;
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

    public void drawStartMenu() {
        StdDraw.clear(Color.BLACK);
        Font TitleFont = new Font("JetBrains Mono", Font.BOLD, 45);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.setFont(TitleFont);
        StdDraw.text(SCREENWIDTH / 2.0, SCREENHEIGHT - 15, "CS61B: The Game");

        Font ContentFont = new Font("JetBrains Mono", Font.PLAIN, 30);
        StdDraw.setFont(ContentFont);
        StdDraw.text(SCREENWIDTH / 2.0, 21, "New Game (N)");
        StdDraw.text(SCREENWIDTH / 2.0, 18, "Load Default Game (L)");
        StdDraw.text(SCREENWIDTH / 2.0, 15, "Load Slot Game (T)");
        StdDraw.text(SCREENWIDTH / 2.0, 12, "Change Avatar (C)");
        StdDraw.text(SCREENWIDTH / 2.0, 9, "Quit (Q)");
        StdDraw.show();
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

    public void drawChangeAvatar() {
        Font font = new Font("JetBrains Mono", Font.BOLD, 30);
        StdDraw.clear(Color.BLACK);
        StdDraw.setFont(font);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(SCREENWIDTH / 2.0, SCREENHEIGHT / 2.0 + 5, "Choose Avatar");
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(SCREENWIDTH / 2.0 - 10, SCREENHEIGHT / 2.0, Character.toString(Tileset.AVATAR1.character()));
        StdDraw.setPenColor(Color.MAGENTA);
        StdDraw.text(SCREENWIDTH / 2.0, SCREENHEIGHT / 2.0, Character.toString(Tileset.AVATAR2.character()));
        StdDraw.setPenColor(Color.PINK);
        StdDraw.text(SCREENWIDTH / 2.0 + 10, SCREENHEIGHT / 2.0, Character.toString(Tileset.AVATAR3.character()));
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(SCREENWIDTH / 2.0 - 10, SCREENHEIGHT / 2.0 - 5, "1");
        StdDraw.text(SCREENWIDTH / 2.0, SCREENHEIGHT / 2.0 - 5, "2");
        StdDraw.text(SCREENWIDTH / 2.0 + 10, SCREENHEIGHT / 2.0 - 5, "3");
        StdDraw.show();
    }

    public void drawSaveMenu() {
        Font font = new Font("JetBrains Mono", Font.PLAIN, 30);
        StdDraw.clear(Color.BLACK);
        StdDraw.setFont(font);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(SCREENWIDTH / 2.0, SCREENHEIGHT / 2.0 + 5, "Choose Slot");
        File defaultSave = new File("default.txt");
        StdDraw.text(SCREENWIDTH / 2.0 - 20, SCREENHEIGHT / 2.0, "default");
        StdDraw.text(SCREENWIDTH / 2.0 - 20, SCREENHEIGHT / 2.0 - 5, "(Q)");
        File slot1 = new File("slot1.txt");
        StdDraw.text(SCREENWIDTH / 2.0 - 10, SCREENHEIGHT / 2.0, "Slot 1");
        StdDraw.text(SCREENWIDTH / 2.0 - 10, SCREENHEIGHT / 2.0 - 5, "(1)");
        if (!slot1.exists()) {
            StdDraw.text(SCREENWIDTH / 2.0 - 10, SCREENHEIGHT / 2.0 - 10, "Empty");
        }
        File slot2 = new File("slot2.txt");
        StdDraw.text(SCREENWIDTH / 2.0, SCREENHEIGHT / 2.0, "Slot 2");
        StdDraw.text(SCREENWIDTH / 2.0, SCREENHEIGHT / 2.0 - 5, "(2)");
        if (!slot2.exists()) {
            StdDraw.text(SCREENWIDTH / 2.0, SCREENHEIGHT / 2.0 - 10, "Empty");
        }
        File slot3 = new File("slot3.txt");
        StdDraw.text(SCREENWIDTH / 2.0 + 10, SCREENHEIGHT / 2.0, "Slot 3");
        StdDraw.text(SCREENWIDTH / 2.0 + 10, SCREENHEIGHT / 2.0 - 5, "(3)");
        if (!slot3.exists()) {
            StdDraw.text(SCREENWIDTH / 2.0 + 10, SCREENHEIGHT / 2.0 - 10, "Empty");
        }
        StdDraw.show();
    }

    public void drawLoadSlot() {
        Font font = new Font("JetBrains Mono", Font.PLAIN, 30);
        StdDraw.clear(Color.BLACK);
        StdDraw.setFont(font);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(SCREENWIDTH / 2.0, SCREENHEIGHT / 2.0 + 5, "Choose Slot");
        File slot1 = new File("slot1.txt");
        if (slot1.exists()) {
            StdDraw.text(SCREENWIDTH / 2.0 - 10, SCREENHEIGHT / 2.0, "Slot 1");
            StdDraw.text(SCREENWIDTH / 2.0 - 10, SCREENHEIGHT / 2.0 - 5, "(1)");
        }
        File slot2 = new File("slot2.txt");
        if (slot2.exists()) {
            StdDraw.text(SCREENWIDTH / 2.0, SCREENHEIGHT / 2.0, "Slot 2");
            StdDraw.text(SCREENWIDTH / 2.0, SCREENHEIGHT / 2.0 - 5, "(2)");
        }
        File slot3 = new File("slot3.txt");
        if (slot3.exists()) {
            StdDraw.text(SCREENWIDTH / 2.0 + 10, SCREENHEIGHT / 2.0, "Slot 3");
            StdDraw.text(SCREENWIDTH / 2.0 + 10, SCREENHEIGHT / 2.0 - 5, "(3)");
        }
        StdDraw.text(SCREENWIDTH / 2.0, SCREENHEIGHT / 2.0 - 10, "Back (B)");
        StdDraw.show();
    }

    public void drawHUD(Position p, TETile[][] world) {
        Font font = new Font("JetBrains Mono", Font.PLAIN, 20);
        StdDraw.setFont(font);
        StdDraw.setPenColor(Color.WHITE);
        String s = world[p.x][p.y].description();
        StdDraw.textLeft(1, SCREENHEIGHT - 2, s);
        StdDraw.show();
    }

    public Position getMousePos() {
        int x = (int) StdDraw.mouseX();
        int y = (int) StdDraw.mouseY();

        return new Position(Math.min(x, WIDTH - 1), Math.min(y, HEIGHT - 1));
    }

    public void saveGame() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("default.txt"))) {
            writer.println(actionString);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveGame(String n) {
        String fileName = "slot" + n + ".txt";
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            writer.println(actionString);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Game loadGame() {
        String actionString = null;
        try (BufferedReader reader = new BufferedReader(new FileReader("default.txt"))) {
            actionString = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Engine g = new Engine();
        g.interactWithInputString(actionString);
        return g.game;
    }

    private Game loadGame(String n) {
        String actionString = null;
        String fileName = "slot" + n + ".txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            actionString = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Engine g = new Engine();
        g.interactWithInputString(actionString);
        return g.game;
    }
}
