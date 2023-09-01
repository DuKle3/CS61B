package deque;

public interface Deque<T> {
    public void addFirst(T item);
    public void addLast(T item);
    public T removeFirst();
    public T removeLast();
    public T get(int i);
    public int size();
    public void printDeque();
    default public boolean isEmpty() {
        return size() == 0;
    }
}

