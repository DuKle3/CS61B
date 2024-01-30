package bstmap;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {
    /* This variable stores the root of the BST. */
    private Node root;
    private int size;

    /* The inner data structure. */
    private class Node {
        K key;
        V val;
        Node left, right;
        Node(K k, V v) {
            this.key = k;
            this.val = v;
        }
    }
    @Override
    public void clear() {
        root = null;
        size = 0;
    }
    @Override
    public boolean containsKey(K key) {
        return containsKey(root, key);
    }
    private boolean containsKey(Node x, K key) {
        if (x == null) {
            return false;
        }
        int cmp = x.key.compareTo(key);
        if (cmp > 0) {
            return containsKey(x.left, key);
        } else if (cmp < 0) {
            return containsKey(x.right, key);
        } else {
            return true;
        }
    }
    @Override
    public V get(K key) {
        return get(root, key);
    }
    private V get(Node x, K key) {
        if (x == null) { return null; }
        int cmp = x.key.compareTo(key);
        if (cmp > 0) {
            return get(x.left, key);
        } else if (cmp < 0) {
            return get(x.right, key);
        } else {
            return x.val;
        }
    }
    @Override
    public int size() {
        return size;
    }

    @Override
    public void put(K key, V value) {
        if (!containsKey(key)) {
            size += 1;
        }
        root = put(root, key, value);
    }
    private Node put(Node x, K key, V value) {
        if (x == null) {
            return new Node(key, value);
        }
        int cmp = x.key.compareTo(key);
        if (cmp > 0) {
            x.left = put(x.left, key, value);
        } else if (cmp < 0) {
            x.right = put(x.right, key, value);
        } else {
            x.val = value;
        }
        return x;
    }

    public void printInOrder() {
        printInOrder(root);
    }
    private void printInOrder(Node x) {
        if (x == null) {
            return;
        }
        printInOrder(x.left);
        System.out.println(x.key.toString() + "->" + x.val.toString());
        printInOrder(x.right);
    }
    /* The followings methods are not supported yet. */
    public Iterator<K> iterator() {
        throw new UnsupportedOperationException();
    }
    @Override
    public V remove(K key) {
        if (containsKey(key)) {
            V targetVal = get(key);
            root = remove(root, key);
            return targetVal;
        }
        return null;
    }
    private Node remove(Node x, K key) {
        if (x == null) {
            return null;
        }
        int cmp = x.key.compareTo(key);
        if (cmp > 0) {
            x.left = remove(x.left, key);
        }
        if (cmp < 0) {
            x.right = remove(x.right, key);
        }
        return x;
    }

    @Override
    public V remove(K key, V value) {
        throw new UnsupportedOperationException();
    }

    private Node getMinNode(Node x) {
        if (x.left == null) {
            return x;
        }
        return getMinNode(x.left);
    }

    @Override
    public Set<K> keySet() {
        HashSet<K> set = new HashSet<>();
        addKeys(root, set);
        return set;
    }
    private void addKeys(Node x, Set<K> s) {
        if (x == null) {
            return;
        }
        s.add(x.key);
        addKeys(x.left, s);
        addKeys(x.right, s);
    }
}
