package byow.Core;

import byow.InputDemo.InputSource;
import byow.InputDemo.StringInputDevice;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;

import java.util.Random;

public class Test {
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;
    public static void main(String[] args) {
        //DifferentSeedTest("n5197880843569031643s");
        //DifferentSeedTest("n6737513353940735140s");
        sameSeedTest("n1824155662461692906s");
    }

    public static void sameSeedTest(String seed) {
        Engine engine = new Engine();
        TETile[][] world = engine.interactWithInputString(seed);
        TETile[][] world2 = engine.interactWithInputString(seed);

        if (checkSameWorld(world, world2)) {
            return;
        }

        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        ter.renderFrame(world);
    }

    public static void SeedTest(String input) {
        InputSource inputSource = new StringInputDevice(input);
        long seed = 0;
        while (inputSource.possibleNextInput()) {
            char c = Character.toUpperCase(inputSource.getNextKey());
            if (c == 'N') {
                continue;
            }
            if (c == 'S') {
                break;
            }
            seed = seed * 10 + Character.getNumericValue(c);
        }
        System.out.println(seed);
    }

    public static boolean checkSameWorld(TETile[][] tiles1, TETile[][] tiles2) {
        boolean flag = true;
        int width = tiles1.length;
        int height = tiles1[0].length;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (tiles1[i][j] != tiles2[i][j]) {
                    flag = false;
                    break;
                }
            }
        }
        return flag;
    }
}
