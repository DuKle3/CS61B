# Gitlet Design Document

**Name**:

## Classes and Data Structures
### Data Structures 

```
.gitlet 
|
|-- objects 
|     |-- commits
|     |-- blobs
|
|-- HEAD
|-- addStage
|-- removeStage
|-- refs 
|     |-- heads
|
|-- staging
|     |-- addedBlobs
|     |-- ...
```

### Repository
#### Static Variables
#### Static methods
```java
/** Initialize the .gielet folders. */
public static void init();
```
```java
public static void add(String fileName);
```
Situation: 
1. file does not tracked by current Commit.
2. file is tracked by current Commit.
    - addFile is identical to the current Commit.
        - remove the file in the staging area if it exist.
    - addFile is different to the current Commit.
        - add to the staging area.


### Commit 
#### Instance Variables
```java
private String id;

private String message;

private List<String> parentCommits;

private Date date;

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

public Commit copyWithMessage(String message);

/* Return Commit Instance Variables. */
public Type get{Instance Variables}();

private String generateSHAId();

private static dateToTimestamp();
```

### Blob 
#### Instance Variables
```java
private byte[] content;

private String id;

private String fileName;
```
#### Instance Methods
```java
/** Save the file to the .gitlet/objects/blobs with it's hashCode. */
public void save();
```
### addStage 
#### Instance Variables
```java
Map<String, String> addStage;
```
#### Instance Methods
```java
public void addToStage(Blob addBlob);
public void clean();
public void isEmpty();
public void save();
public void remove(String fileName);
public void saveBlobs();
public void getAddStage();
```
### removeStage
#### Instance Variables
```java
Map<String, String> removeStage;
```
#### Instance Methods
```java
public void addRemoveStage(String fileName, String fileId);
public void clean();
public void isEmpty();
public void save();
public void getRemoveStage();
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
public static void initialFolders();
```


## Algorithms

### Merge
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


## Persistence
