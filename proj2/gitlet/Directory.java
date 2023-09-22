package gitlet;

import java.io.File;

import static gitlet.Utils.join;

public class Directory {

    /** The current working directory. */
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


    /** Create the folders. */
    public static void initialFolders() {
        GITLET_DIR.mkdirs();
        COMMIT_DIR.mkdirs();
        BLOB_DIR.mkdirs();
        HEADS_DIR.mkdirs();
        STAGING_DIR.mkdirs();
    }
}
