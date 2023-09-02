package deque;

import java.util.Iterator;

public class LinkedListDeque<T> implements Deque<T>, Iterable<T> {
    private int size;
    private Node sentinel;
    private class Node {
        private T item;
        private Node next;
        private Node prev;

        public Node(T i, Node p, Node n) {
            item = i;
            prev = p;
            next = n;
        }
    }
    /* invariant:
       sentinel's prev is point to the last Node.
       sentinel's next is point to the next Node.
    */

    public LinkedListDeque() {
        size = 0;
        sentinel = new Node(null, null, null);
        sentinel.prev = sentinel;
        sentinel.next = sentinel;
    }
    /** Add the item to the front of the deque. */
    public void addFirst(T item) {
        size += 1;
        Node p = sentinel.next;
        sentinel.next = new Node(item, sentinel, p);
        p.prev = sentinel.next;
    }
    /** Add the item to the back of the deque. */
    public void addLast(T item) {
        size += 1;
        sentinel.prev.next = new Node(item, sentinel.prev, sentinel);
        sentinel.prev = sentinel.prev.next;
    }
    /** Return True if deque is empty */
    //    public boolean isEmpty() {
    //        return sentinel.next == sentinel;
    //    }
    /** Return the number of items in the deque. */
    public int size() {
        return size;
    }
    /** Prints the items in the deque. */
    public void printDeque() {
        Node p = sentinel.next;
        while (p != sentinel) {
            System.out.print(p.item + " ");
            p = p.next;
        }
        System.out.println(" ");
    }
    /**
     * Remove and returns the item at the front of the deque, return null if no item.
     */
    public T removeFirst() {
        if (this.isEmpty()) {
            return null;
        }
        size -= 1;
        T firstItem = sentinel.next.item;
        sentinel.next = sentinel.next.next;
        sentinel.next.prev = sentinel;
        return firstItem;
    }
    /** Remove and returns the item at the back of the deque, return null if no items. */
    public T removeLast() {
        if (this.isEmpty()) {
            return null;
        }
        size -= 1;
        T lastItem = sentinel.prev.item;
        sentinel.prev = sentinel.prev.prev;
        sentinel.prev.next = sentinel;
        return lastItem;
    }
    /** Get the item at the given index. */
    public T get(int index) {
        if (index >= size()) {
            return null;
        }
        Node p = sentinel.next;
        while (index != 0) {
            p = p.next;
            index -= 1;
        }
        return p.item;
    }
    private T getRecursiveHelper(Node p, int index) {
        if (index == 0) {
            return p.item;
        }
        return getRecursiveHelper(p.next, index - 1);
    }
    public T getRecursive(int index) {
        if (index >= size()) {
            return null;
        }
        return getRecursiveHelper(sentinel.next, index);
    }
    public Iterator<T> iterator() {
        return new LinkedListDequeIterator();
    }
    private class LinkedListDequeIterator implements Iterator<T> {
        private int position;
        public LinkedListDequeIterator() {
            position = 0;
        }
        public boolean hasNext() {
            return position < size;
        }
        public T next() {
            T returnItem = get(position);
            position += 1;
            return returnItem;
        }
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o instanceof LinkedListDeque olld) {
            for (int i = 0; i < this.size; i++) {
                if (!this.get(i).equals(olld.get(i))) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
}
