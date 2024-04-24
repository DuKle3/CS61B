package byow.lab13;

import byow.Core.RandomUtils;
import edu.princeton.cs.introcs.In;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.Color;
import java.awt.Font;
import java.util.Random;

public class MemoryGame {
    /** The width of the window of this game. */
    private int width;
    /** The height of the window of this game. */
    private int height;
    /** The current round the user is on. */
    private int round;
    /** The Random object used to randomly generate Strings. */
    private Random rand;
    /** Whether or not the game is over. */
    private boolean gameOver;
    /** Whether or not it is the player's turn. Used in the last section of the
     * spec, 'Helpful UI'. */
    private boolean playerTurn;
    /** The characters we generate random Strings from. */
    private static final char[] CHARACTERS = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    /** Encouraging phrases. Used in the last section of the spec, 'Helpful UI'. */
    private static final String[] ENCOURAGEMENT = {"You can do this!", "I believe in you!",
                                                   "You got this!", "You're a star!", "Go Bears!",
                                                   "Too easy for you!", "Wow, so impressive!"};
    private static final String[] GAMESTATES = {"Watch !", "Typed !"};

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Please enter a seed");
            return;
        }

        long seed = Long.parseLong(args[0]);
        MemoryGame game = new MemoryGame(50, 50, seed);
        game.startGame();
    }

    public MemoryGame(int width, int height, long seed) {
        /* Sets up StdDraw so that it has a width by height grid of 16 by 16 squares as its canvas
         * Also sets up the scale so the top left is (0,0) and the bottom right is (width, height)
         */
        this.width = width;
        this.height = height;
        StdDraw.setCanvasSize(this.width * 16, this.height * 16);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, this.width);
        StdDraw.setYscale(0, this.height);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();

        rand = new Random(seed);
        this.round = 1;
    }

    public String generateRandomString(int n) {
        String s = "";
        for (int i = 0; i < n; i++) {
            int index = rand.nextInt(CHARACTERS.length);
            s = s + CHARACTERS[index];
        }
        return s;
    }

    public void drawFrame(String s) {
        // Take the string and display it in the center of the screen
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.clear(Color.BLACK);
        StdDraw.setFont(font);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text( width / 2.0, height / 2.0, s);

        // If game is not over, display relevant game information at the top of the screen
        if (!gameOver) {
            // show the round number
            String displayRound = "Round: " + Integer.toString(round);
            StdDraw.text( 5, height - 2, displayRound);

            // Watch ! or Typed !
            String gameState = playerTurn ? GAMESTATES[1] : GAMESTATES[0];
            StdDraw.text(width / 2.0, height - 2, gameState);

            // Encouragement
            int index = rand.nextInt(ENCOURAGEMENT.length);
            StdDraw.text(width - 10, height - 2, ENCOURAGEMENT[index]);
            StdDraw.line(0, height - 3, width, height - 3);
        }

        StdDraw.show();
    }

    public void flashSequence(String letters) {
        // Display each character in letters, making sure to blank the screen between letters
        for (int i = 0; i < letters.length(); i++) {
            try {
                // show the single char for 1 sec.
                drawFrame(Character.toString(letters.charAt(i)));
                Thread.sleep(1000);

                // 0.5s
                drawFrame("");
                Thread.sleep(500);

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public String solicitNCharsInput(int n) {
        // Read n letters of player input
        String typed = "";
        while (typed.length() < n) {
            if (StdDraw.hasNextKeyTyped()) {
                Character c = StdDraw.nextKeyTyped();
                typed = typed + c;
                drawFrame(typed);
            }
        }
        return typed;
    }

    public void startGame() {
        // Set any relevant variables before the game starts
        round = 1;
        gameOver = false;

        // Establish Engine loop
        while (!gameOver) {
            drawFrame("Round: " + Integer.toString(round));
            playerTurn = false;
            String ans = generateRandomString(round);
            flashSequence(ans);
            playerTurn = true;
            drawFrame("");
            String playerAns = solicitNCharsInput(round);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if (!playerAns.equals(ans)) {
                gameOver = true;
            }
            round += 1;
        }
        drawFrame("Game Over ! You made it to Round: " + Integer.toString(round));
    }
}
