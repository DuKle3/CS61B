package gitlet;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static gitlet.Utils.*;


/** Represents a gitlet repository.
 *  The main logic of the gitlet.
 *  @author DuKle3
 */
public class Repository {
    /**
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));

    /** The .gitlet directory. */
    public static final File GITLET_DIR = join(CWD, ".gitlet");

    /** Store the Active branch name. */
    public static final File HEAD = join(GITLET_DIR, "HEAD");

    /** Staging Area. */
    public static final File addStage = join(GITLET_DIR, "addStage");
    public static final File removeStage = join(GITLET_DIR, "removeStage");

    /** The Commits, Blobs directory. */

    /** init command
     *  Initialize the Repository.
     *  1. create GITLET Folder.
     *  2. make initial commit.
     *  3. store the initial commit in the .gitlet/objects/commits folder.
     */
    public static void init() {
        if (GITLET_DIR.exists()) {
            System.out.println("A Gitlet version-control system already exists in the current directory.");
            System.exit(0);
        }
        // Initialize Gitlet Folder
        Directory.initialFolders();

        /** Initialize Commit
         *  1. Create initial Commit
         *  2. store the initial Commit to the .gitlet/objects/commits/
         */
        Commit initialCommit = new Commit();
        initialCommit.save();

        // Initialize Staging Area
        initStagingArea();

        /** Initial the HEAD
         *  1. create master
         *  2. create HEAD which points to the master
         */
        File masterFile = join(Directory.HEADS_DIR, "master");
        Utils.writeContents(masterFile, initialCommit.getHashCode());
        Utils.writeContents(HEAD, "master");
    }

    /** Initialize Staging Area
     *  1. create empty addStage
     *  2. create empty removeStage
     */
    private static void initStagingArea() {
        AddStage addStage = new AddStage();
        RemoveStage removeStage = new RemoveStage();
        File addStageFile = join(GITLET_DIR, "addStage");
        File removeStageFile = join(GITLET_DIR, "removeStage");
        Utils.writeObject(addStageFile, addStage);
        Utils.writeObject(removeStageFile, removeStage);
    }

    /** Add the file to the staging area.
     *  1. staged
     *  2. if CWD version of the file is identical to the version in the currentCommit, don't stage it, and remove it
     *  from staging area if it is already there. (file changed, added, changed back.)
     *
     * @param fileName
     */
    public static void add(String fileName) {
        Commit currentCommit = getCurrentCommit();

        File addFile = join(CWD, fileName);
        if (!addFile.exists()) {
            System.out.println("File does not exits.");
            System.exit(0);
        }

        byte[] contents = Utils.readContents(addFile);
        Blob addBlob = new Blob(contents);
        AddStage stagingArea = Utils.readObject(addStage, AddStage.class);
        RemoveStage removeStageArea = Utils.readObject(removeStage, RemoveStage.class);

        // This File does not exist in current Commit.
        if (!currentCommit.contain(fileName)) {
            stagingArea.addToStage(fileName, addBlob);
            stagingArea.save();
        } else {
            // This File exist in current Commit.
            if (stagingArea.contain(fileName)) {
                stagingArea.remove(fileName);
            }

            // 1. same content (same hashCode)
            if (currentCommit.haveSameFile(fileName, addBlob.getHashCode())) {
                removeStageArea.remove(fileName);
                removeStageArea.save();
                stagingArea.save();
            }
            // 2. different content
            else {
                stagingArea.addToStage(fileName, addBlob);
                stagingArea.save();
            }
        }
    }
    /** Remove
     *  1. file is currently staged for addition
     *  2. file is tracked in the current commit
     *  3. file is neither staged nor tracked by the head commit.
     */
    public static void rm(String removeFileName) {
        AddStage stagingArea = Utils.readObject(addStage, AddStage.class);
        RemoveStage removedStage = Utils.readObject(removeStage, RemoveStage.class);
        Commit currentCommit = getCurrentCommit();
        if (stagingArea.contain(removeFileName)) {
            stagingArea.removeStagingArea(removeFileName);
            stagingArea.save();
        }
        else if (currentCommit.contain(removeFileName)) {
            String removeFileId = currentCommit.getBlobs().get(removeFileName);
            removedStage.addRemoveStage(removeFileName, removeFileId);
            removedStage.save();
        }
        else {
            System.out.println("No reason to remove the file.");
        }
    }

    /** Commit
     *  0. if no files have been staged, print some message
     *  1. copy parent commit.
     *  2. StagingArea
     * @param args
     */
    public static void commit(String[] args) {
        if (args.length == 1 || args[1].isEmpty()) {
            System.out.println("Please enter a commit message.");
            System.exit(0);
        }
        String commitMessage = args[1];
        AddStage stagingArea = Utils.readObject(addStage, AddStage.class);
        RemoveStage removeStageArea = Utils.readObject(removeStage, RemoveStage.class);
        Commit currentCommit = getCurrentCommit();

        if (stagingArea.isEmpty() && removeStageArea.isEmpty()) {
            System.out.println("No changes added to the commit.");
            System.exit(0);
        }

        /**
         *  1. Update the currentCommit with the files in the staging Area.
         *  2. Untracked the file in the removeStageArea.
         */

        Map<String, String> Blobs = currentCommit.getBlobs();

        for (String fileName : stagingArea.getAddStage().keySet()) {
            Blobs.put(fileName, stagingArea.getAddStage().get(fileName));
        }

        for (String removeFileName : removeStageArea.getRemoveStage().keySet()) {
            Blobs.remove(removeFileName);
        }

        stagingArea.saveBlobs();
        Commit newCommit = currentCommit.copyWithMessage(commitMessage);
        String newCommitHashCode = newCommit.getHashCode();
        moveActiveBranch(newCommitHashCode);
        newCommit.save();
        stagingArea.clean();
        removeStageArea.clean();
        stagingArea.save();
        removeStageArea.save();
    }



    /** Log the commit information, and recursively log it's parentCommit. */
    public static void log() {
        Commit currentCommit = getCurrentCommit();
        recursiveCallLogOut(currentCommit);
    }
    /** Recursively call the commit parent to log. */
    private static void recursiveCallLogOut(Commit commit) {
        printCommitInformation(commit);
        if (commit.getParentHashCode().isEmpty()) {
            return;
        }
        String firstParentHashCode = commit.getParentHashCode().get(0);
        recursiveCallLogOut(Commit.readFromFile(firstParentHashCode));
    }
    /** Log out all the commits in the COMMIT_DIR. */
    public static void globalLog() {
        List<String> eachCommitHashCodes = Utils.plainFilenamesIn(Directory.COMMIT_DIR);
        for (String eachCommitHashCode : eachCommitHashCodes) {
            Commit eachCommit = Commit.readFromFile(eachCommitHashCode);
            printCommitInformation(eachCommit);
        }
    }

    private static void printCommitInformation(Commit eachCommit) {
        System.out.println("===");
        System.out.print("commit ");
        System.out.println(eachCommit.getHashCode());
        List<String> parentHashCode = eachCommit.getParentHashCode();
        if (parentHashCode.size() == 2) {
            System.out.print("Merge: ");
            String firstParent = parentHashCode.get(0).substring(0, 7);
            String secondParent = parentHashCode.get(1).substring(0, 7);
            System.out.print(firstParent);
            System.out.print(" ");
            System.out.println(secondParent);
        }
        System.out.print("Date: ");
        System.out.println(eachCommit.getTimestamp());
        System.out.println(eachCommit.getMessage());
        System.out.println();
    }

    /** Print out the commit id which commit have the message.
     *
     * @param message
     */
    public static void find(String message) {
        List<String> eachCommitHashCodes = Utils.plainFilenamesIn(Directory.COMMIT_DIR);
        int flag = 0;
        for (String eachCommitHashCode : eachCommitHashCodes) {
            Commit eachCommit = Commit.readFromFile(eachCommitHashCode);
            if (eachCommit.getMessage().equals(message)) {
                flag++;
                System.out.println(eachCommitHashCode);
            }
        }
        if (flag == 0) {
            System.out.println("Find no commit with that message.");
        }
    }
    /** Display active branch and Staged files, Removal files.
     *  1. active branch
     *  2. addStaged
     *  3. removalStaged
     *  4. Modification but not staged for commit.
     *  5. Untracked files.
     */
    public static void status() {
        // 1. Active branch
        branchStatus();
        // 2. Staged Files
        stagedStatus();
        // 3. Removed Staged
        removalStatus();
        // 4. Modifications
        modificationNotStagedStatus();
        // 5. Untracked Files
        untrackedFileStatus();
    }
    /** Display the current branch status. */
    private static void branchStatus() {
        System.out.println("=== Branches ===");
        String activeBranchName = Utils.readContentsAsString(HEAD);
        List<String> branchesNames = Utils.plainFilenamesIn(Directory.HEADS_DIR);
        System.out.println("*" + activeBranchName);
        for (String branchName : branchesNames) {
            if (!branchName.equals(activeBranchName)) {
                System.out.println(branchName);
            }
        }
        System.out.println();
    }
    /** Display the current Staging Area Status. */
    private static void stagedStatus() {
        System.out.println("=== Staged Files ===");
        AddStage stagingArea = Utils.readObject(addStage, AddStage.class);
        for (String addFileName : stagingArea.getAddStage().keySet()) {
            System.out.println(addFileName);
        }
        System.out.println();
    }
    /** Display the current removal Files Status. */
    private static void removalStatus() {
        System.out.println("=== Removed Files ===");
        RemoveStage removeStageArea = Utils.readObject(removeStage, RemoveStage.class);
        for (String removeFile : removeStageArea.getRemoveStage().keySet()) {
            System.out.println(removeFile);
        }
        System.out.println();
    }
    // TODO: Complete Extra Points.

    /** a1. Tracked in the current commit, changed in the working directory, but not staged
     *  a2. Staged for addition, but with different contents than in the working directory
     *  b1. Staged for addition, but deleted in the working directory
     *  b2. Not staged for removal, but tracked in the current commit and deleted from the working directory.
     */
    private static void modificationNotStagedStatus() {
        System.out.println("=== Modifications Not Staged For Commit ===");
        Commit currentCommit = getCurrentCommit();
        AddStage stagingArea = Utils.readObject(addStage, AddStage.class);
        RemoveStage removedStage = Utils.readObject(removeStage, RemoveStage.class);
        List<String> fileNames = Utils.plainFilenamesIn(CWD);
        // a. modified
        for (String fileName : fileNames) {
            File file = join(CWD, fileName);
            String hashCode = Utils.sha1(Utils.readContents(file));
            if (currentCommit.contain(fileName) && !stagingArea.contain(fileName) && !currentCommit.getBlobs().get(fileName).equals(hashCode)) {
                modificationStatus(fileName);
            } else if (stagingArea.contain(fileName) && !stagingArea.getAddStage().get(fileName).equals(hashCode)) {
                modificationStatus(fileName);
            }
        }

        // b. deleted
        // b1. In staging area, but deleted
        // b2. tracked in currentCommit, not staged for removal, but deleted.
        for (String fileName : stagingArea.getAddStage().keySet()) {
            if (!fileNames.contains(fileName)) {
                deletedStatus(fileName);
            }
        }

        for (String fileName : currentCommit.getBlobs().keySet()) {
            if (!fileNames.contains(fileName) && !removedStage.contains(fileName)) {
                deletedStatus(fileName);
            }
        }

        System.out.println();
    }
    private static void modificationStatus(String fileName) {
        System.out.println(fileName + " (modified)");
    }
    private static void deletedStatus(String fileName) {
        System.out.println(fileName + " (deleted)");
    }
    //** TODO: */
    private static void untrackedFileStatus() {
        System.out.println("=== Untracked Files ===");
        Commit currentCommit = getCurrentCommit();
        AddStage stagingArea = Utils.readObject(addStage, AddStage.class);
        List<String> fileNames = Utils.plainFilenamesIn(CWD);
        for (String fileName : fileNames) {
            if (!currentCommit.contain(fileName) && !stagingArea.contain(fileName)) {
                System.out.println(fileName);
            }
        }
        System.out.println();
    }

    /** Checkout.
     *        0      1    2        3
     *  1. checkout -- [fileName]
     *  2. checkout [commit id] -- [fileName]
     *  3. checkout [branchName]
     */
    public static void checkout(String[] args) {
        // checkout [branchName]
        if (args.length == 2) {
            checkoutBranchFile(args[1]);
        }
        // checkout -- [fileName]
        else if (args[1].equals("--") && args.length == 3) {
            String fileName = args[2];
            checkoutHeadFile(fileName);
        }
        // checkout [commit id] -- [fileName]
        else if (args[2].equals("--") && args.length == 4) {
            checkoutCommitFile(args[1], args[3]);
        }
        else {
            System.exit(0);
        }
    }
    /** Take the version of the file exist in the head Commit, and put it in the working directory. */
    private static void checkoutHeadFile(String fileName) {
        Commit currentCommit = getCurrentCommit();
        String blobHashCode = currentCommit.getBlobs().get(fileName);
        checkoutCommitFileName(currentCommit, fileName);
    }
    /** Takes all files in the commit at the head of the given branch, and puts them in the working directory
     *  overwriting the versions of the files if they exist.
     *  1. The given branch will now be considered the current branch.
     *  2. Any files that are tracked in the current branch but are not present in the checked-out branch are deleted.
     *  3. Staging Area is cleared, unless checkout-out branch is the current branch.
     */
    private static void checkoutBranchFile(String branchName) {
        File branchFile = join(Directory.HEADS_DIR, branchName);
        String activeBranchName = Utils.readContentsAsString(HEAD);
        if (!branchFile.exists()) {
            System.out.println("No such branch exists.");
            System.exit(0);
        } else if (activeBranchName.equals(branchName)) {
            System.out.println("No need to checkout the current branch.");
            System.exit(0);
        }

        String branchCommitHashCode = Utils.readContentsAsString(branchFile);
        Commit branchCommit = Commit.readFromFile(branchCommitHashCode);

        Commit currentCommit = getCurrentCommit();
        AddStage stagingArea = Utils.readObject(addStage, AddStage.class);
        List<String> fileNames = Utils.plainFilenamesIn(CWD);

        // file is untracked in the current branch and would be overwritten by the checkout.
        // 1. branch.contain
        // 2. current.notContain
        // file is tracked in the current branch and not present in the checked-out branch are deleted.
        for (String fileName : fileNames) {
            if (!currentCommit.contain(fileName) && branchCommit.contain(fileName)) {
                System.out.println("There is an untracked file in the way; delete it, or add and commit it first.");
                System.exit(0);
            } else if (currentCommit.contain(fileName) && !branchCommit.contain(fileName)) {
                File file = join(CWD, fileName);
                file.delete();
            }
        }
        stagingArea.clean();
        stagingArea.save();
        checkoutCommit(branchCommitHashCode);
        changeActiveBranch(branchName);
    }


    /** Checkout all the files with the given commitHashCode. */
    private static void checkoutCommit(String commitHashCode) {
        File commitFile = join(Directory.COMMIT_DIR, commitHashCode);
        if (!commitFile.exists()) {
            System.out.println("No commit with that id exits.");
            System.exit(0);
        }
        Commit commit = Commit.readFromFile(commitHashCode);
        for (String fileName : commit.getBlobs().keySet()) {
            checkoutCommitFileName(commit, fileName);
        }
    }
    private static void checkoutCommitFile(String commitHashCode, String fileName) {
        File commitFile = join(Directory.COMMIT_DIR, commitHashCode);
        if (!commitFile.exists()){
            System.out.println("No commit with that id exits.");
            System.exit(0);
        }
        checkoutCommitFileName(Commit.readFromFile(commitHashCode), fileName);
    }
    /** Take the version of the file exist in the commit with the given id. */
    private static void checkoutCommitFileName(Commit commit , String fileName) {
        checkFileExistInCommit(commit, fileName);
        String blobHashCode = commit.getBlobs().get(fileName);
        Blob checkoutBlob = Blob.readFromFile(blobHashCode);

        File checkoutFile = join(CWD, fileName);
        Utils.writeContents(checkoutFile, checkoutBlob.getContent());
    }

    private static void checkFileExistInCommit(Commit commit, String fileName) {
        if (!commit.contain(fileName)) {
            System.out.println("File does not exist in that commit.");
            System.exit(0);
        }
    }

    /** Create new branch with the given name, and points it at the current head commit. */
    public static void branch(String[] args) {
        String branchName = args[1];
        File branchFile = join(Directory.HEADS_DIR, branchName);
        if (branchFile.exists()) {
            System.out.println("A branch with that name already exists.");
            System.exit(0);
        } else {
            Commit currentCommit = getCurrentCommit();
            Utils.writeContents(branchFile, currentCommit.getHashCode());
        }
    }

    /** Deletes the branch with the given name. */
    public static void rmBranch(String[] args) {
        String branchName = args[1];
        String currentBranchName = Utils.readContentsAsString(HEAD);
        File branchFile = join(Directory.HEADS_DIR, branchName);
        checkBranchExist(branchName);
        if (currentBranchName.equals(branchName)) {
            System.out.println("Cannot remove the current branch.");
        } else {
            branchFile.delete();
        }
    }

    /** Checks out all the files tracked by the given commit.
     *  Removes tracked files that are not present in that commit.
     * @param args
     */
    public static void reset(String[] args) {
        String commitHashCode = args[1];
        AddStage stagingArea = Utils.readObject(addStage, AddStage.class);
        // Remove file.
        List<String> fileNames = Utils.plainFilenamesIn(CWD);
        for (String fileName : fileNames) {
            File file = join(CWD, fileName);
            Utils.restrictedDelete(file);
        }
        checkoutCommit(commitHashCode);
        stagingArea.clean();
        stagingArea.save();
        Utils.writeContents(HEAD, commitHashCode);
    }

    /** TODO: Merge.
     *  Find split point.
     *  a. modified in one, and the other not.
     */
    public static void merge(String[] args) {
        String branchName = args[1];
        checkBranchExist(branchName);
        File branchFile = join(Directory.HEADS_DIR, branchName);
        String branchHashCode = Utils.readContentsAsString(branchFile);

        Commit head = getCurrentCommit();
        Commit branch = Commit.readFromFile(branchHashCode);
        Commit splitPoint = SplitPoint.splitPoint(head, branch);
        mergeAncestorMessage(head, branch, splitPoint);
        AddStage stagingArea = Utils.readObject(addStage, AddStage.class);

        Map<String, String> headMap = head.getBlobs();
        Map<String, String> branchMap = branch.getBlobs();
        Map<String, String> splitMap = splitPoint.getBlobs();

        boolean encounterConflict = false;
        // Iterate SplitPoint Files.
        for (String fileName : splitPoint.getBlobs().keySet()) {
            String splitFileHashCode = splitPoint.getBlobs().get(fileName);
            boolean inHead = headMap.containsKey(fileName);
            boolean inBranch = branchMap.containsKey(fileName);

            boolean modifiedInHead = inHead && !headMap.get(fileName).equals(splitFileHashCode);
            boolean modifiedInBranch = inBranch && !branchMap.get(fileName).equals(splitFileHashCode);

            if (inHead && inBranch) {
                // 1. modified in head but not other -> head
                if (modifiedInHead && !modifiedInBranch) {
                    continue;
                }
                // 2. modified in other but not head -> other
                else if (!modifiedInHead && modifiedInBranch) {
                    headMap.put(fileName, headMap.get(fileName));
                    checkoutCommitFileName(branch, fileName);
                }
                /** 3. modified in both:
                 *    a. in same way.
                 *    b. conflict
                 */
                else if (modifiedInHead && modifiedInBranch) {
                    if (headMap.get(fileName).equals(branchMap.get(fileName))) {
                        continue;
                    } else {
                        Blob mergeBlob = mergeConflict(fileName, headMap.get(fileName), branchMap.get(fileName));
                        encounterConflict = true;
                        headMap.put(fileName, mergeBlob.getHashCode());
                    }
                }
            }
            // 6. unmodified in head but not present in others.
            else if (inHead && !inBranch) {
                if (!modifiedInHead) {
                    headMap.remove(fileName);
                    File file = join(CWD, fileName);
                    file.delete();
                } else {
                    Blob mergeBlob = mergeConflict(fileName, headMap.get(fileName), null);
                    encounterConflict = true;
                    headMap.put(fileName, mergeBlob.getHashCode());
                }
            }
            // 7. unmodified in branch but not present in head.
            else if (!inHead && inBranch) {
                if (!modifiedInBranch) {
                    continue;
                } else {
                    Blob mergeBlob = mergeConflict(fileName, null, branchMap.get(fileName));
                    encounterConflict = true;
                    headMap.put(fileName, mergeBlob.getHashCode());
                }
            }
        }

        // 4. not in split nor other but in head -> head
        // 5. not in split nor head but in other -> other
        for (String fileName : branchMap.keySet()) {
            boolean notInSplit = !splitMap.containsKey(fileName);
            boolean notInHead = !headMap.containsKey(fileName);
            if (notInHead && notInSplit) {
                headMap.put(fileName, branchMap.get(fileName));
                checkoutCommitFileName(branch, fileName);
            }
        }

        if (encounterConflict) {
            System.out.println("Encountered a merge conflict.");
        }
        String currentBranchName = Utils.readContentsAsString(HEAD);
        String mergeMessage = "Merged " + args[1] + " into " + currentBranchName + ".";
        Commit newCommit = head.copyWithMessage(mergeMessage);
        String newCommitHashCode = newCommit.getHashCode();
        newCommit.updateBlobs(headMap);
        newCommit.addSecondParent(branch.getHashCode());

        moveActiveBranch(newCommitHashCode);

        newCommit.save();
        stagingArea.save();
    }

    /** Deal with the Merge Conflict. */
    public static Blob mergeConflict(String fileName, String headBlobHashCode, String branchBlobHashCode) {
        String headContent = "";
        String branchContent = "";

        if (headBlobHashCode != null) {
            Blob headBlob = Blob.readFromFile(headBlobHashCode);
            headContent = new String(headBlob.getContent(), StandardCharsets.UTF_8);
        }
        if (branchBlobHashCode != null) {
            Blob branchBlob = Blob.readFromFile(branchBlobHashCode);
            branchContent = new String(branchBlob.getContent(), StandardCharsets.UTF_8);
        }

        String mergeContent = "<<<<<<< HEAD" + "\n" + headContent + "=======\n" + branchContent + ">>>>>>>\n";
        byte[] mergeByteContent = mergeContent.getBytes();
        File mergeFile = join(CWD, fileName);
        Utils.writeContents(mergeFile, mergeByteContent);
        return new Blob(mergeByteContent);
    }
    private static void mergeAncestorMessage(Commit head, Commit branch, Commit splitPoint) {
        // head == branch
        if (head.getHashCode().equals(branch.getHashCode())) {
            System.out.println("Cannot merge a branch with itself.");
        }
        // branch == splitPoint
        else if (branch.getHashCode().equals(splitPoint.getHashCode())) {
            System.out.println("Given branch is an ancestor of the current branch.");
            System.exit(0);
        }
        // head == splitPoint
        else if (head.getHashCode().equals(splitPoint.getHashCode())) {
            System.out.println("Current branch fast-forwarded.");
            checkoutCommit(branch.getHashCode());
            System.exit(0);
        }
        // uncommitted file
        AddStage stagingArea = Utils.readObject(addStage, AddStage.class);
        RemoveStage removedStage = Utils.readObject(removeStage, RemoveStage.class);
        if (!stagingArea.isEmpty() || !removedStage.isEmpty()) {
            System.out.println("You have uncommitted changes.");
            System.exit(0);
        }
        // untracked file
        List<String> fileNames = Utils.plainFilenamesIn(CWD);
        for (String fileName : fileNames) {
            if (!head.contain(fileName) && branch.contain(fileName)) {
                System.out.println("There is an untracked file in the way; delete it, or add and commit it first.");
                System.exit(0);
            }
        }
    }
    /* Return the CurrentCommit which HEAD is pointing to. */
    /**
     *  1. read the HEAD
     *  2. read the active branch
     *  3. read the current commit
     * @return
     */
    private static Commit getCurrentCommit() {
        String activeBranch = Utils.readContentsAsString(HEAD);

        File activeBranchFile = join(Directory.HEADS_DIR, activeBranch);
        String currentCommitId = Utils.readContentsAsString(activeBranchFile);

        File currentCommitFile = join(Directory.COMMIT_DIR, currentCommitId);
        return Utils.readObject(currentCommitFile, Commit.class);
    }
    /** Move the active Branch with the commitHashCode. */
    private static void moveActiveBranch(String commitHashCode) {
        String activeBranchName = Utils.readContentsAsString(HEAD);
        File activeBranchFile = join(Directory.HEADS_DIR, activeBranchName);
        Utils.writeContents(activeBranchFile, commitHashCode);
    }

    /** Change the active branch to the given branchName. */
    private static void changeActiveBranch(String branchName) {
        Utils.writeContents(HEAD, branchName);
    }

    /** Check the given branchName exist, if not, print out error message and exit. */
    private static void checkBranchExist(String branchName) {
        File branchFile = join(Directory.HEADS_DIR, branchName);
        if (!branchFile.exists()) {
            System.out.println("A branch with that name does not exist.");
            System.exit(0);
        }
    }
}