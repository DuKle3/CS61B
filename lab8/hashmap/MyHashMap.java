package hashmap;

import java.util.*;

/**
 *  A hash table-backed Map implementation. Provides amortized constant time
 *  access to elements via get(), remove(), and put() in the best case.
 *
 *  Assumes null keys will never be inserted, and does not resize down upon remove().
 *  @author YOUR NAME HERE
 */
public class MyHashMap<K, V> implements Map61B<K, V> {

    /**
     * Protected helper class to store key/value pairs
     * The protected qualifier allows subclass access
     */
    protected class Node {
        K key;
        V value;

        Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    /* Instance Variables */
    private static final int DEFAULT_SIZE = 16;
    private static final double DEFAULT_LOAD_FACTOR = 0.75;
    private Collection<Node>[] buckets;
    // You should probably define some more!
    private int size = 0;
    private double loadFactor;
    private Set<K> keySet;

    /** Constructors */
    public MyHashMap() {
        this(DEFAULT_SIZE, DEFAULT_LOAD_FACTOR);
    }

    public MyHashMap(int initialSize) {
        this(initialSize, DEFAULT_LOAD_FACTOR);
    }

    /**
     * MyHashMap constructor that creates a backing array of initialSize.
     * The load factor (# items / # buckets) should always be <= loadFactor
     *
     * @param initialSize initial size of backing array
     * @param maxLoad maximum load factor
     */
    public MyHashMap(int initialSize, double maxLoad) {
        buckets = createTable(initialSize);
        loadFactor = maxLoad;
        keySet = new HashSet<>();
    }

    /**
     * Returns a new node to be placed in a hash table bucket
     */
    private Node createNode(K key, V value) {
        return new Node(key, value);
    }

    /**
     * Returns a data structure to be a hash table bucket
     *
     * The only requirements of a hash table bucket are that we can:
     *  1. Insert items (`add` method)
     *  2. Remove items (`remove` method)
     *  3. Iterate through items (`iterator` method)
     *
     * Each of these methods is supported by java.util.Collection,
     * Most data structures in Java inherit from Collection, so we
     * can use almost any data structure as our buckets.
     *
     * Override this method to use different data structures as
     * the underlying bucket type
     *
     * BE SURE TO CALL THIS FACTORY METHOD INSTEAD OF CREATING YOUR
     * OWN BUCKET DATA STRUCTURES WITH THE NEW OPERATOR!
     */
    protected Collection<Node> createBucket() {
        return new LinkedList<>();
    }

    /**
     * Returns a table to back our hash table. As per the comment
     * above, this table can be an array of Collection objects
     *
     * BE SURE TO CALL THIS FACTORY METHOD WHEN CREATING A TABLE SO
     * THAT ALL BUCKET TYPES ARE OF JAVA.UTIL.COLLECTION
     *
     * @param tableSize the size of the table to create
     */
    private Collection<Node>[] createTable(int tableSize) {
        Collection<Node>[] table = new Collection[tableSize];
        for (int i = 0; i < tableSize; i++) {
            table[i] = createBucket();
        }
        return table;
    }

    @Override
    public void clear() {
        size = 0;
        keySet = new HashSet<>();
        buckets = createTable(DEFAULT_SIZE);
    }

    @Override
    public boolean containsKey(K key) {
        return get(key) != null;
    }

    @Override
    public V get(K key) {
        Node node = getNode(key);
        if (node == null) {
            return null;
        }
        return node.value;
    }

    private Node getNode(K key) {
        int index =  getIndex(key, buckets.length);
        for (Node node : buckets[index]) {
            if (node.key.equals(key)) {
                return node;
            }
        }
        return null;
    }

    private int getIndex(K key, int tableSize) {
        int keyHashCode = key.hashCode();
        return Math.floorMod(keyHashCode, tableSize);
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public void put(K key, V value) {
        int index = getIndex(key, buckets.length);
        Node node = getNode(key);
        keySet.add(key);
        if (node != null) {
            node.value = value;
            return;
        }
        node = createNode(key, value);
        buckets[index].add(node);
        size += 1;
        if (reachMaxLoad()) {
            resize();
        }
    }

    private boolean reachMaxLoad() {
        return ((double) size / buckets.length) > loadFactor;
    }

    private void resize() {
        Collection<Node>[] newBuckets = createTable(buckets.length * 2);
        Iterator<K> it = iterator();
        while (it.hasNext()) {
            K key = it.next();
            int index = getIndex(key, newBuckets.length);
            newBuckets[index].add(createNode(key, get(key)));
        }
        buckets = newBuckets;
    }
    @Override
    public Set<K> keySet() {
        return this.keySet;
    }

    @Override
    public Iterator<K> iterator() {
        return keySet().iterator();
    }


    /** Extra Question. */
    public V remove(K key) {
        throw new UnsupportedOperationException();
    }
    public V remove(K key, V value) {
        throw new UnsupportedOperationException();
    }

}
