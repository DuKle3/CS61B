package gitlet;

import java.io.File;

import static gitlet.Utils.join;

/** This class will check all the command have the right number of format of operands. */
public class ArgumentCheck {
    public static void initArgumentCheck(String[] args) {
        if (args.length != 1) {
            wrongOperands();
        }
    }
    public static void argumentCheck(String[] args, int argumentNumber) {
        if (args.length != argumentNumber && nonInitialized()) {
            wrongOperands();
        }
    }
    public static void checkoutArgument(String[] args) {
        if (args.length > 4 || args.length < 2 && nonInitialized()) {
            wrongOperands();
        }
    }
    /** Printout error message and Exit. */
    private static void wrongOperands() {
        System.out.println("Incorrect operands.");
        System.exit(0);
    }
    /** Return true if .gitlet folder exist. */
    private static boolean nonInitialized() {
        return !Repository.GITLET_DIR.exists();
    }
}
