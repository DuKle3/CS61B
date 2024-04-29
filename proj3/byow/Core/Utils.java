package byow.Core;

import byow.InputDemo.InputSource;
import byow.InputDemo.StringInputDevice;

import java.nio.file.Path;
import java.nio.file.Files;

public class Utils {
    /**
     * Parse the seed from the input
     * input : "N12345Swwaassdd"
     * output : 12345
     */
    public static long parseTheSeed(InputSource inputSource) {
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

    /**
     *
     * input : N12345Swwaassdd:Q
     * output : WWAASSDD:Q
     */
    public static String parseTheAction(String input) {
        InputSource inputSource = new StringInputDevice(input);
        String actionString = "";
        while (inputSource.possibleNextInput()) {
            char c = Character.toUpperCase(inputSource.getNextKey());
            if (c == 'S') {
                break;
            }
        }
        while (inputSource.possibleNextInput()) {
            char c = Character.toUpperCase(inputSource.getNextKey());
            actionString = actionString + Character.toString(c);
        }
        return actionString;
    }
}
