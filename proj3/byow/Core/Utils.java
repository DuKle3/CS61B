package byow.Core;

import byow.InputDemo.InputSource;
import byow.InputDemo.StringInputDevice;

import java.nio.file.Path;
import java.nio.file.Files;

public class Utils {
    public static long parseTheSeed(String input) {
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
        return seed;
    }
}
