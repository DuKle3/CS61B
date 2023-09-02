package deque;

import java.util.Iterator;

public class ArrayDeque<T> implements Deque<T>, Iterable<T> {
    private T[] items;
    private int size;
    private int nextFront;
    private int nextLast;

    public ArrayDeque() {
        items = (T[]) new Object[8];
        size = 0;
        nextFront = 0;
        nextLast = 1;
    }
    /** Return the right index. */
    private int moveRight(int index) {
        if (index == items.length - 1) {
            index = 0;
        } else {
            index += 1;
        }
        return index;
    }
    /** Return the left index. */
    private int moveLeft(int index) {
        if (index == 0) {
            index = items.length - 1;
        } else {
            index -= 1;
        }
        return index;
    }
    /** Resize the arrayDeque. */
    private void resize(int capacity) {
        T[] a = (T[]) new Object[capacity];
        int index = 0;
        nextFront = moveRight(nextFront);
        nextLast = moveLeft(nextLast);
        while (nextFront != nextLast) {
            a[index] = items[nextFront];
            index += 1;
            nextFront = moveRight(nextFront);
        }
        a[index] = items[nextFront];
        nextFront = capacity - 1;
        nextLast = size;
        items = a;
    }
    public void addLast(T item) {
        if (size() == items.length) {
            resize(size * 2);
        }
        items[nextLast] = item;
        size += 1;
        nextLast = moveRight(nextLast);
    }
    /** Add the item to the front of the deque. */
    public void addFirst(T item) {
        if (size == items.length) {
            resize(size * 2);
        }
        items[nextFront] = item;
        size += 1;
        nextFront = moveLeft(nextFront);
    }
    public boolean isUsageLow() {
        return size < (items.length / 4);
    }
    /** Remove the returns the item at the front of the deque. */
    public T removeFirst() {
        if (isEmpty()) {
            return null;
        }
        nextFront = moveRight(nextFront);
        T removeItem = items[nextFront];
        items[nextFront] = null;
        size -= 1;
        if (isUsageLow() && size > 0) {
            resize(items.length / 4);
        }
        return removeItem;
    }
    /** Remove and returns the item at the back of the deque. */
    public T removeLast() {
        if (isEmpty()) {
            return null;
        }
        nextLast = moveLeft(nextLast);
        T removeItem = items[nextLast];
        items[nextLast] = null;
        size -= 1;
        if (isUsageLow() && size > 0) {
            resize(items.length / 4);
        }
        return removeItem;
    }
    /** Return the number of the items in the list. */
    public int size() {
        return size;
    }
    /** Return the length of the items. */
    public int itemLength() {
        return items.length;
    }
    /** Print the deque. */
    public void printDeque() {
        for (int i = 0; i < size; i++) {
            int printIndex = (nextFront + 1 + i) % items.length;
            System.out.print(items[printIndex] + " ");
        }
        System.out.println(" ");
    }
    /** Return true if deque is empty */
    //    public boolean isEmpty() {
    //        return items[moveRight(nextFront)] == null;
    //    }
    public T get(int index) {
        if (index > size) {
            return null;
        }
        int realIndex = (index + 1 + nextFront) % items.length;
        return items[realIndex];
    }
    public Iterator<T> iterator() {
        return new ArrayDequeIterator();
    }

    private class ArrayDequeIterator implements Iterator<T> {
        private int position;
        public ArrayDequeIterator() {
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
//    public boolean equals(Object o) {
//        if (this == o) {
//            return true;
//        }
//        if (o instanceof ArrayDeque oaard) {
//            for (int i = 0; i < size; i++) {
//                if (!this.get(i).equals(oaard.get(i))) {
//                    return false;
//                }
//            }
//            return true;
//        }
//        return false;
//    }
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (!(o instanceof Deque)) {
            return false;
        }
        Deque<T> ol = (Deque<T>) o;
        if (ol.size() != this.size()) {
            return false;
        }
        for (int i = 0; i < size; i++) {
            if (!(ol.get(i).equals(this.get(i)))) {
                return false;
            }
        }
        return true;
    }
}
