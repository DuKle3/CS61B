package gitlet;


/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author TODO
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Please enter a command.");
            System.exit(0);
        }
        String firstArg = args[0];
        switch(firstArg) {
            case "init":
                ArgumentCheck.initArgumentCheck(args);
                Repository.init();
                break;
            case "add":
                ArgumentCheck.argumentCheck(args, 2);
                String addFileName = args[1];
                Repository.add(addFileName);
                break;
            case "commit":
                ArgumentCheck.argumentCheck(args, 2);
                Repository.commit(args);
                break;
            case "rm":
                ArgumentCheck.argumentCheck(args, 2);
                String removeFileName = args[1];
                Repository.rm(removeFileName);
                break;
            case "log":
                ArgumentCheck.argumentCheck(args, 1);
                Repository.log();
                break;
            case "global-log":
                ArgumentCheck.argumentCheck(args, 1);
                Repository.globalLog();
                break;
            case "find":
                ArgumentCheck.argumentCheck(args, 2);
                String message = args[1];
                Repository.find(message);
                break;
            case "status":
                ArgumentCheck.argumentCheck(args, 1);
                Repository.status();
                break;
            case "checkout":
                ArgumentCheck.checkoutArgument(args);
                Repository.checkout(args);
                break;
            // TODO: FILL THE REST IN
            case "branch":
                ArgumentCheck.argumentCheck(args, 2);
                Repository.branch(args);
                break;
            case "rm-branch":
                ArgumentCheck.argumentCheck(args, 2);
                Repository.rmBranch(args);
                break;
            case "reset":
                ArgumentCheck.argumentCheck(args, 2);
                Repository.reset(args);
                break;
            case "merge" :
                //ArgumentCheck.argumentCheck(args, 2);
                //Repository.merge(args);
                break;
            default:
                System.out.println("No command with that name exist.");
                break;
        }
    }
}
