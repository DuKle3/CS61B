package bstmap;

import java.util.Iterator;
import java.util.Set;

public class BSTMap<K extends Comparable, V> implements Map61B<K, V> {
    /* This variable stores the root of the BST. */
    private Node root;
    /* The inner data structure. */
    private class Node {
        K key;
        V val;
        int size;
        Node left, right;
        Node(K k, V v, int size) {
            this.key = k;
            this.val = v;
            this.size = size;
        }
    }
    @Override
    public void clear() {
        root = null;
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
        return size(root);
    }
    private int size(Node n) {
        if (n == null) {
            return 0;
        } else {
            return n.size;
       }
    }
    @Override
    public void put(K key, V value) {
        root = put(root, key, value);
    }
    private Node put(Node x, K key, V value) {
        if (x == null) {
            return new Node(key, value, 1);
        }
        int cmp = x.key.compareTo(key);
        if (cmp > 0) {
            x.left = put(x.left, key, value);
        } else if (cmp < 0) {
            x.right = put(x.right, key, value);
        } else {
            x.val = value;
        }
        x.size = 1 + size(x.left) + size(x.right);
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
        throw new UnsupportedOperationException();
    }

    @Override
    public V remove(K key, V value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<K> keySet() {
        throw new UnsupportedOperationException();
    }
}
