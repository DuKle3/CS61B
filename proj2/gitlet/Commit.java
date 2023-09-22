package gitlet;

// TODO: any imports you need here

import jdk.jshell.execution.Util;

import java.io.File;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static gitlet.Utils.join;

/** Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author Dukle3
 */
public class Commit implements Serializable {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    /** The sha1 id of the commit. */
    private String id;
    /** The message of this Commit. */
    private String message;
    /** The parent commit hash id. */
    private List<String> parentCommits;
    /** The date of the commit, initial commit have the fixed value. */
    private Date date;
    /** The timestamp of the commit. */
    private String timestamp;
    /** Store the Key-Value pair, (FileName - FileSHAId). */
    private Map<String, String> blobs;


    /* TODO: fill in the rest of this class. */

    /** Initial Commit constructor. */
    public Commit() {
        this.date = new Date(0); // January 1, 1970, 00:00:00 GMT.
        this.timestamp = dateToTimeStamp(this.date);
        this.message = "initial commit";
        this.parentCommits = new ArrayList<>();
        this.blobs = new TreeMap<>();
        this.id = this.generateSHAId();
    }

    /** Commit Constructor. */
    public Commit(String message, List<String> parentCommits, Map<String, String> blobs) {
        this.message = message;
        this.parentCommits = parentCommits;
        this.blobs = blobs;
        this.date = new Date();
        this.timestamp = dateToTimeStamp(this.date);
        this.id = this.generateSHAId();
    }

    /** Return a new Commit with identical blobs, and update the date, parent, message. */
    public Commit copyWithMessage(String message) {
        List<String> parentCommits = new ArrayList<>();
        parentCommits.add(this.id);
        return new Commit(message, parentCommits, this.blobs);
    }
    /** Return Commit in the COMMIT_DIR with this hashCode. */
    public static Commit readFromFile(String commitHashCode) {
        File file = join(Directory.COMMIT_DIR, commitHashCode);
        return Utils.readObject(file, Commit.class);
    }
    /** Save this Commit with it's hashCode. */
    public void save() {
        File commitFile = join(Directory.COMMIT_DIR, this.id);
        Utils.writeObject(commitFile, this);
    }

    /** Some function to use out of this class. */
    public Map<String, String> getBlobs() {
        return this.blobs;
    }
    public String getMessage() {
        return this.message;
    }
    public Date getDate() {
        return this.date;
    }
    public String getHashCode() {
        return this.id;
    }
    public List<String> getParentHashCode() {
        return this.parentCommits;
    }
    public String getTimestamp() {
        return this.timestamp;
    }


    /** Helper Methods. */
    /* Generate the SHA id of the commit. */
    private String generateSHAId() {
        return Utils.sha1(message, blobs.toString(), parentCommits.toString(), timestamp);
    }
    /* Convert the date to the String. */
    private static String dateToTimeStamp(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy Z", Locale.US);
        return dateFormat.format(date);
    }
}
