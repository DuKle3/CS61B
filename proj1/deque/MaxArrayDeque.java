package deque;

import java.util.Comparator;
public class MaxArrayDeque<T> extends ArrayDeque<T> {
    private Comparator<T> buildInCmp;
    public MaxArrayDeque(Comparator<T> c) {
        super();
        buildInCmp = c;
    }
    public T max() {
        if (this.isEmpty()) {
            return null;
        } else {
            int maxIndex = 0;
            for (int i = 0; i < this.size(); i++) {
                if (buildInCmp.compare(get(i), get(maxIndex)) > 0) {
                    maxIndex = i;
                }
            }
            return get(maxIndex);
        }
    }
    public T max(Comparator<T> cmp) {
        if (this.isEmpty()) {
            return null;
        } else {
            int maxIndex = 0;
            for (int i = 0; i < this.size(); i++) {
                if (cmp.compare(get(i), get(maxIndex)) > 0) {
                    maxIndex = i;
                }
            }
            return get(maxIndex);
        }
    }
}
