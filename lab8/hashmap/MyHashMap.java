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
    private Collection<Node>[] buckets;
    // You should probably define some more!
    private int bucketSize;
    private int size;
    private double loadFactor;
    private Set<K> keySet;

    /** Constructors */
    public MyHashMap() {
        size = 0;
        bucketSize = 16;
        loadFactor = 0.75;
        keySet = new HashSet<>();
        buckets = new Collection[bucketSize];
        for (int i = 0; i < bucketSize; i++) {
            buckets[i] = createBucket();
        }
    }

    public MyHashMap(int initialSize) {
        size = 0;
        bucketSize = initialSize;
        loadFactor = 0.75;
        keySet = new HashSet<>();
        buckets = new Collection[initialSize];
        for (int i = 0; i < bucketSize; i++) {
            buckets[i] = createBucket();
        }
    }

    /**
     * MyHashMap constructor that creates a backing array of initialSize.
     * The load factor (# items / # buckets) should always be <= loadFactor
     *
     * @param initialSize initial size of backing array
     * @param maxLoad maximum load factor
     */
    public MyHashMap(int initialSize, double maxLoad) {
        size = 0;
        bucketSize = initialSize;
        loadFactor = maxLoad;
        keySet = new HashSet<>();
        buckets = new Collection[initialSize];
        for (int i = 0; i < bucketSize; i++) {
            buckets[i] = createBucket();
        }
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
        return new HashSet<>();
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
        return null;
    }

    @Override
    public void clear() {
        size = 0;
        for (int i = 0; i < bucketSize; i++) {
            buckets[i] = createBucket();
        }
    }

    @Override
    public boolean containsKey(K key) {
        return !(get(key) == null);
    }

    @Override
    public V get(K key) {
        int index = index(key.hashCode());
        Iterator<Node> it = buckets[index].iterator();
        while (it.hasNext()) {
            Node e = it.next();
            if (e.key.equals(key)) {
                return e.value;
            }
        }
        return null;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public void put(K key, V value) {
        int index = index(key.hashCode());
        if (containsKey(key)) {
            Iterator<Node> it = buckets[index].iterator();
            while (it.hasNext()) {
                Node e = it.next();
                if (e.key.equals(key)) {
                    buckets[index].remove(e);
                    break;
                }
            }
            buckets[index].add(createNode(key, value));
        } else {
            keySet.add(key);
            size += 1;
            buckets[index].add(createNode(key, value));
        }

        if ((double) (size / bucketSize) >= loadFactor) {
            resize();
        }
    }

    private void resize() {
        MyHashMap<K, V> newMap = new MyHashMap<>(bucketSize * 2, loadFactor);
        for (K key : keySet()) {
            V value = get(key);
            newMap.put(key, value);
        }
        buckets = newMap.buckets;
        bucketSize *= 2;
    }
    @Override
    public Set<K> keySet() {
        return this.keySet;
    }

    @Override
    public Iterator<K> iterator() {
        return keySet().iterator();
    }

    private int index(Integer i) {
        int index = i % bucketSize;
        if (index < 0) {
            index = -index;
        }
        return index;
    }

    /** Extra Question. */
    public V remove(K key) {
        throw new UnsupportedOperationException();
    }
    public V remove(K key, V value) {
        throw new UnsupportedOperationException();
    }

}
