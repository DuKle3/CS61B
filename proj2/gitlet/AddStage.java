package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static gitlet.Utils.join;

public class AddStage implements Serializable {
    public static final File STAGING_DIR = join(Directory.GITLET_DIR, "staging");
    private Map<String, String> addStage;
    public AddStage() {
        this.addStage = new HashMap<>();
    }

    /** Add the file to the staging Area.
     *  1. store the Blob in the .gitlet/addStage.
     *  2. store the key-value pair in the addStage Map.
     * @param addBlob
     */
    public void addToStage(Blob addBlob) {
        this.addStage.put(addBlob.getFileName(), addBlob.getHashCode());
        File stagedBlob = join(STAGING_DIR, addBlob.getHashCode());
        Utils.writeObject(stagedBlob, addBlob);
    }
    /** save the addStage back to the file. */
    public void save() {
        Utils.writeObject(Repository.addStage, this);
    }

    /** Remove the file in the staging Area.
     *  1. remove the Blob in the directory.
     *  2. update the addStage.
     */
    public void remove(String fileName) {
        String hashCode = addStage.get(fileName);
        File stagedBlob = join(STAGING_DIR, hashCode);
        stagedBlob.delete();
        this.addStage.remove(fileName);
    }

    /** Clean the Staging Area.
     *  1. remove all the files in the directory.
     *  2. initialize the addStage Map.
     */
    public void clean() {
        addStage.clear();
        List<String> fileNames = Utils.plainFilenamesIn(STAGING_DIR);
        for (String fileName : fileNames) {
            File deletedFile = join(STAGING_DIR, fileName);
            deletedFile.delete();
        }
    }

    /** Save all the blobs in staging area to the BlOB_DIR. */
    public void saveBlobs() {
        for (String eachBlobFileName : addStage.keySet()) {
            String eachBlobHashCode = addStage.get(eachBlobFileName);
            File eachBlobFile = join(STAGING_DIR, eachBlobHashCode);
            Blob eachblob = Utils.readObject(eachBlobFile, Blob.class);
            eachblob.save();
        }
    }
    /** Return True if the Staging Area is Empty. */
    public boolean isEmpty() {
        return addStage.isEmpty();
    }

    /** Return the addStage Map. */
    public Map<String, String> getAddStage() {
        return addStage;
    }

}
