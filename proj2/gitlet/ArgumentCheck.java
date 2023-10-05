package gitlet;


/** This class will check all the command have the right number of format of operands. */
public class ArgumentCheck {
    public static void initArgumentCheck(String[] args) {
        if (args.length != 1) {
            wrongOperands();
        }
    }
    public static void argumentCheck(String[] args, int argumentNumber) {
        if (nonInitialized()) {
            nonInitializedMessage();
        }
        if (args.length != argumentNumber) {
            wrongOperands();
        }
    }
    public static void checkoutArgument(String[] args) {
        int length = args.length;
        if (nonInitialized()) {
            nonInitializedMessage();
        }
        switch (length) {
            case 2:
                break;
            case 3:
                if (!args[1].equals("--")) {
                    wrongOperands();
                }
                break;
            case 4:
                if (!args[2].equals("--")) {
                    wrongOperands();
                }
                break;
            default:
                wrongOperands();
                break;
        }
    }
    /** Printout error message and Exit. */
    private static void wrongOperands() {
        System.out.println("Incorrect operands.");
        System.exit(0);
    }
    /** Printout error message and Exit. */
    private static void nonInitializedMessage() {
        System.out.println("Not in an initialized Gitlet directory.");
        System.exit(0);
    }
    /** Return true if .gitlet folder not exist. */
    private static boolean nonInitialized() {
        return !Repository.GITLET_DIR.exists();
    }
}
