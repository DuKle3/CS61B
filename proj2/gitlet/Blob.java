package gitlet;


import java.io.File;
import java.io.Serializable;

import static gitlet.Utils.join;

public class Blob implements Serializable {
    /** Represent the blob in the gitlet. */
    private byte[] content;
    private String id;
    private String fileName;

    /** Constructor */
    public Blob(byte[] content, String fileName) {
        this.content = content;
        this.id = Utils.sha1(content);
        this.fileName = fileName;
    }

    /** Return Blob's fileName. */
    public String getFileName() {
        return this.fileName;
    }
    /** Return Blob's id. */
    public String getHashCode() {
        return this.id;
    }
    public byte[] getContent() {
        return content;
    }

    /** Some helper methods. */

    /** Save this blob to the BLOBS_DIR with its hashCode. */
    public void save() {
        File saveBlobFile = join(Directory.BLOB_DIR, this.id);
        Utils.writeObject(saveBlobFile, this);
    }
    /** Return Blob in the BLOB_DIR with this hashCode. */
    public static Blob readFromFile(String commitHashCode) {
        File file = join(Directory.BLOB_DIR, commitHashCode);
        return Utils.readObject(file, Blob.class);
    }
}
