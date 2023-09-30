package gitlet;

import java.util.Map;
import java.util.*;
import java.util.TreeMap;


public class SplitPoint {
    /** Store the Commit hashCode <-> Depth. */
    private static Map<String, Integer> headMap;
    private static Map<String, Integer> branchMap;

    /** Return the latest splitPoint of the given two commits. */
    public static Commit splitPoint(Commit head, Commit branch) {
        headMap = commitPathWithDepth(head);
        branchMap = commitPathWithDepth(branch);
        String minCommitHashCode = null;
        int minCommitDepth = branchMap.size();
        for (String commitHashCode : headMap.keySet()) {
            boolean branchContain = branchMap.containsKey(commitHashCode);
            if (branchContain && branchMap.get(commitHashCode) < minCommitDepth) {
                minCommitHashCode = commitHashCode;
                minCommitDepth = branchMap.get(commitHashCode);
            }
        }
        if (minCommitHashCode == null) {
            return null;
        } else {
            return Commit.readFromFile(minCommitHashCode);
        }
    }

    private static Map<String, Integer> commitPathWithDepth(Commit startingCommit) {
        List<String> fringe = new LinkedList<>();
        Map<String, Integer> commitDepth = new TreeMap<>();
        fringe.add(startingCommit.getHashCode());
        commitDepth.put(startingCommit.getHashCode(), 0);

        while (!fringe.isEmpty()) {
            String commitHashCode = fringe.remove(0);
            Commit commit = Commit.readFromFile(commitHashCode);
            for (String parentHashCode : commit.getParentHashCode()) {
                fringe.add(parentHashCode);
                commitDepth.put(parentHashCode, commitDepth.get(commitHashCode) + 1);
            }
        }
        return commitDepth;
    }
}
