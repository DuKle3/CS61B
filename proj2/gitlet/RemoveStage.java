package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import static gitlet.Utils.join;

public class RemoveStage implements Serializable {
    private Map<String, String> removeStage;
    public RemoveStage() {
        this.removeStage = new HashMap<>();
    }

    /** add the file to the RemoveStage and delete the file if it exits in the CWD. */
    public void addRemoveStage(String fileName, String fileId) {
        removeStage.put(fileName, fileId);
        File removeFile = join(Repository.CWD, fileName);
        if (removeFile.exists()) {
            removeFile.delete();
        }
    }

    /** Unremoved file with the given fileName. */
    public void remove(String fileName) {
        removeStage.remove(fileName);
    }

    /** Return the removeStage. */
    public Map<String, String> getRemoveStage() {
        return removeStage;
    }

    /** Save the RemoveStage back to the file. */
    public void save() {
        Utils.writeObject(Repository.removeStage, this);
    }

    /** Return True if removeStage is Empty. */
    public boolean isEmpty() {
        return removeStage.isEmpty();
    }

    /** Return true if the file is staged for removed. */
    public boolean contains(String fileName) {
        return removeStage.containsKey(fileName);
    }
    /** Clean up the removeStage. */
    public void clean() {
        removeStage.clear();
    }
}
