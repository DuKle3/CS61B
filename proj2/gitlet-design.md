# Gitlet Design Document

## Classes and Data Structures
### Persistence

```
.gitlet 
|
|-- objects 
|     |-- commits  <== All commit are stored in the directory
|     |-- blobs    <== All blob are stored in the directory
|
|-- HEAD
|-- addStage
|-- removeStage
|-- refs 
|     |-- heads      <== Store the branch file (point to commit)
|
|-- staging
|     |-- stagedBlobs <== Store the added blob
```


### Repository
#### Static Variables
```java
/** The current working directory. */
public static final File CWD = new File(System.getProperty("user.dir"));

/** The .gitlet directory. */
public static final File GITLET_DIR = join(CWD, ".gitlet");

/** Store the Active branch name. */
public static final File HEAD = join(GITLET_DIR, "HEAD");

/** Staging Area. */
public static final File ADD_STAGE = join(GITLET_DIR, "addStage");
public static final File REMOVE_STAGE = join(GITLET_DIR, "removeStage");
```

#### Static methods
```java
/** Initialize the .gielet folders. */
public static void init();

/** Add the file to the staging Area. */ 
public static void add(String fileName);

/** Saves a sanpshot of tracked files in the current commit and staging area. */ 
public static void commit(String[] args);

/** Unstage the file if it is staged for addition, 
    or stage it for removal and remove the file in CWD. */
public static void rm(String removeFileName);

/** Display information about each commit backwards along the commit tree. */
public static void log();

/** Like log, but displays all commits. The order does not matter. */
public static void global-log();

/** Prints out the ides of all commits that have the given commit message. */
public static void fine(String message);

/** Displays active branch, staged, removal, modification, untracked file. */
public static void status();

/** 1. checkout branch.
    2. checkout file with given commit id.
    3. checkout file with head commit. */
public static void checkout(String[] args);

/** Create new branch with the given name. */ 
public static void branch(String[] args);

/** Deletes the branch with the given name. */
public static void rmBranch(String[] args);

/** Checks out all the files tracked by the given commit. */ 
public static void reset(String[] args);
```


### Commit 
This class represents a `Commit` in gitlet. 
#### Instance Variables
```java
/** The sha1 id of the commit. */
private String id;

/** The commit message of this commit. */
private String message;

/** The parent commit hash id. */
private List<String> parentCommits;

/** The date of the commit, initial commit have the fixed value. */
private Date date;

/** The timestamp of the commit. */
private String timestamp;

/** All the file traced by the commit. 
  * key-value --> fileName-HashCode */
private Map<String, String> blobs;
```
#### Constructor
- Initial Commit Constructor
```java
public Commit();
```
- Regular Commit Constructor
```java
public Commit(String message, List<String> parentCommits, Map<String, String> blobs); 
```
#### Instance Methods
```java
/** Save the file to the .gitlet/objects/commits with it's hashCode. */
public void save();

/** Return a Commit which has the given HashCode. */
public static Commit readFromFile(String commitHashCode);

/** Return a Commit which is a child of the Instance, and copy the blobs of it. */
public Commit copyWithMessage(String message);

/* Return Commit Instance Variables. */
public Type get{Instance Variables}();

/** Return true if the commit contain the given file. */
public boolean contain(String fileName);

/** Return true if the commit have the same content of the file. */ 
public boolean haveSameFile(String fileName, String fileHashCode);

/** Generate the sha1 id of the commit. */
private String generateSHAId();

/** Convert the date to the String. */
private static dateToTimestamp();
```

### Blob 
Represent the `Blob` that will be stored.
#### Instance Variables
```java
/** Store the file content. */
private byte[] content;

/** The sha1 id of the blob (depend by the file's content). */
private String id;
```
#### Constructor
```java
public Blob(byte[] content);
```

#### Instance Methods
```java
/** Save the file to the .gitlet/objects/blobs with it's hashCode. */
public void save();

/** Return Blob with the given hash code. */
public static Blob readFromFile(String blobHashCode);

/** Return Blob's variables. */ 
public String getHashCode();
public byte[] getContent();
```


### addStage 
This class track the file which have been add but not commit yet.
#### Instance Variables
```java
/** Store the staging area file. 
  * fileName-HashCode
  */
public Map<String, String> addStage;
```

#### Instance Methods
```java
/** Add the file to the staging Area. */
public void addToStage(Blob addBlob);

/** Delete all file in the staging Area, and initialize the addStage. */
public void clean();

/** Return true if the staging Area is empty. */
public void isEmpty();

/** Save the staging Area back to the file. */
public void save();

/** Remove the file in the staging Area. */
public void remove(String fileName);

/** Save all the blobs in staging Area to the BLOB_DIR. */
public void saveBlobs();

/** Return the addStage Map. */
public void getAddStage();
```
### removeStage
This class trace the file which have been remove but not commit yet.
#### Instance Variables
```java
/** Store the file have been rm. */
public Map<String, String> removeStage;
```
#### Instance Methods
```java
/** Add the file to the removeStage. */
public void addRemoveStage(String fileName, String fileId);

/** Untracked the file. */
public void remove();

/** Clean the removeStage. */
public void clean();

/** Return true if the remove Stage is empty. */
public boolean isEmpty();

/** Return true if the file is staged for removed. */
public boolean contains(String fileName);

/** Save the removeStage back to the file. */
public void save();

/** Return the remove Stage. */
public Map<String, String> getRemoveStage();
```
### Directory
This class stores the directory in the .gitlet folder.
#### Instance Variables
```java
public static final File CWD = new File(System.getProperty("user.dir"));

/** The .gitlet directory. */
public static final File GITLET_DIR = join(CWD, ".gitlet");
public static final File OBJECT_DIR = join(GITLET_DIR, "objects");
public static final File COMMIT_DIR = join(OBJECT_DIR, "Commits");
public static final File BLOB_DIR = join(OBJECT_DIR, "Blobs");

/** Staging Area. */
public static final File STAGING_DIR = join(GITLET_DIR, "staging");
public static final File REFS_DIR = join(GITLET_DIR, "refs");
public static final File HEADS_DIR = join(REFS_DIR, "heads");
```
#### Instance Methods
```java
/** Create the folders. */
public static void initialFolders();
```

### SplitPoint
#### Instance Variables 
```java 
/** Store the commit hash code <-> Depth. */
private static Map<String, Integer> headMap;
private static Map<String, Integer> branchMap;
```
#### Instance Methods
```java 
/** Return the latest split point of the given two commits. */
public static Commit splitPoint(Commit head, Commit branch);

/** Return the Map<parentHashCode, Depth> relative to the given commit. */
private static Map<String, Integer> commitPathWithDepth(Commit startingCommit);
```

### ArgumentCheck
#### Instance Methods
```java
/** Check the arguments valid or not. */
public static void initArgumentCheck(String args[]);
public static void argumentCheck(String args, int argumentNumber);
public static void checkoutArgument(String args);

/** Printout "Incorrect operands" and exit. */
private static void wrongOperands();

/** Printout "Not in an initialized Gitlet directory." and exit. */ 
private static void nonInitializedMessage();

/** Return true if .gitlet folder not exist. */
private static boolean nonInitialized();
```

## Algorithms
### init
```java
/** Initialize the .gielet folders. */
public static void init();
```
- Initialize the Repository.
    - Create .gitlet folder.
    - Initialize Staging Area.
    - Make initial commit and save it.
    - Initial the HEAD file.
- Helper Method :
    - `initStagingArea()`

### add
```java
public static void add(String fileName);
```
Add the file to the staging area.
- file exist in current commit : `stagingArea.remove(fileName)` first
    - same content --> `removeStageArea.remove(fileName)`
    - diff content --> add to staged.
- file not exist : 
    - just staged it.

### rm
```java
public static void rm(String fileName);
```
Remove or untracked the file. 
1. file is currently staged for addition. -> `stagingArea.remove(fileName)`
2. file is tracked in the current commit. -> `removeStage.addRemoveStage(fileName, fileId)`
3. file is neither staged nor tracked by the head commit. -> `"No reason to remove the file."`

### commit 
```java
public static void commit();
```
Save a sanpshot of tracked files in the current commit and staging area.
1. update the current commit tracking blobs.
2. untracked the file in the removeStage.
3. use `currentCommit.copyWithMessage` to create new commit. 
4. save the `new commit`, `stagingArea`, `removeStage`.

### log 
```java
public static void log();
```
Starting at the current head commit, display information backwards to initial commit.

- helper methods 
```java
/** Recursively call the `printCommitInformation` to commit's parent. */
public static void recursiveCallLogOut(Commit commit);

/** Print out the commit information in log format. */
private static void printCommitInformation(Commit commit);
```

- Just simply call the `recursiveCallLogOut`, it will recursively call the `printCommitInformation` of each commit backwards along the commit tree until the intial commit..

### global-log
```java
public static void globalLog();
```
Display all the commits information. 
- Simply iterate through the `COMMIT_DIR` and reuse the `printCommitInformation`.

### find 
```java
public static void find(String message);
```
Prints out the ids of all commits that have the given commit message.
- Iterate through `COMMIT_DIR` and check each commit's message.

### status 
```java
public static void status();
```
Display current information of active commit, staging area, remove staged, modified file, untracked file.

- helper methods
```java
private static void branchStatus();
private static void stagedStatus();
private static void removalStatus();
private static void modificationNotStagedStatus();
private static void untrackedFileStatus();
```
- `branchStatus`, `stagedStatus` and `removalStatus` : Iterate and display.
- `modificationNotStagedStatus` : 
    - `boolean inCommit`
    - `boolean inStagingArea` 
    - `boolean commitHasSameFile` 
    - `boolean stagingAreaHasSameFile`
    - modified : 
        1. tracked in current commit, changed in `CWD`, but not staged.
        2. staged for addition, but changed in `CWD`.
    - deleted : 
        1. not staged for removal, but tracked in current commit and deleted in CWD.
        2. staged for addition, but deleted in `CWD`.
- `untrackedFileStatus` : 
    - Iterate `currentCommit` and `staginArea` both not `contain(fileName)`;

### checkout
```java
public static void checkout(Stirng[] args);
```
- 3 conditions.
```java
1. checkout -- [fileName]               -> checkoutHeadFile(String fileName);
2. checkout [commit id] -- [fileName]   -> checkoutCommitFile(commidId, fileName);
3. checkout [branchName]                -> checkoutBranchFile(branchName);
```

- helper methods :
```java
/** Take the version of the file in the commit with the given id to CWD. */
private static void checkoutCommitFileName(Commit commit, String fileName);

/** Checkout all the files with the given commit id. */
private static void checkoutCommit(String commitHashCode);
```

- `checkoutHeadFile` : simply call the `checkoutCommitFileName`.
- `checkoutCommitFile` : also just call the `checkoutCommitFile`.
- `checkoutBranchFile` : call the `checkoutCommit`
    - but we need to iterate through two `currentCommit` and `branchCommit`, to check the if the "untraced file" exist.

### branch 
```java
public static void branch(String[] args);
```
Create a new branch with the given name.
- create new file in `HEADS_DIR` which store the currentCommit hash code.

### rmBranch
```java
public static void rmBranch(String[] args);
```
Deletes the branch with the given name.
- delete the file in `HEADS_DIR` with given name.

### reset 
```java
public static void reset(String[] args);
```
Checks out all the files tracked by the given commit.

Removes tracked files that are not present in that commmit.

- `delete` all the file in `CWD`
- call the `checkoutCommit`


### merge
```java
public static void merge(String[] args);
```
Merges files from the given branch into the current branch.

0. find split point
1. modified in `other` but not `HEAD` -> `other`
2. modified in `HEAD` but not `other` -> `HEAD`
3. modified in `other` and `HEAD` -> 
   - in same way : DNM(same)
   - in diff way : Conflict
4. not in `split` nor `other` but in `HEAD` -> `HEAD`
5. not in `split` nor `HEAD` but in `other` -> `other`
6. unmodified in `HEAD` but not present in `other` -> Remove
7. unmodified in `other` but not present in `HEAD` -> Remain Removed


| Split | HEAD | Branch | Result   | 
|-------|------|--------|----------|
| A     | !A   | X      | conflict |
| B     | B    | !B     | !B       |
| X     | C    | X      | C        |
| X     | X    | D      | D        |
| E     | X    | E      | X        |
| F     | F    | X      | X        |

#### Find the split point.
**In SplitPoint class**
```java
public static Commit splitPoint(Commit head, Commit branch);
```
- create two map using `commitPathWithDepth`
- `headMap = commitPathWithDepth(head)`
- `branchMap = commitPathWithDepth(branch)`
- Iterate one of the Map, find the commit in both map and have the `min depth value` --> that commit is `splitPoint commit`.

```java
private static Map<String, Integer> commitPathWithDepth(Commit startingCommit);
```
- use the **BFS** to record the `<commit, depth>`
