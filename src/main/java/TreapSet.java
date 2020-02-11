import com.sun.tools.javac.util.Pair;

import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TreapSet<T extends Comparable<T>> implements NavigableSet<T> {


    private Treap<T> treap = new Treap<>();

    Treap<T> root = null;

    int size = 0;

    @Override
    public boolean add(T t) {
        Treap<T> closest = find(t);
        int comparison = closest == null ? -1 : t.compareTo(closest.data);
        if (comparison == 0) {
            return false;
        }
        if (treap == null || treap.data == null) {
            treap = new Treap<>(t, (int)(Math.random() * 1000), null, null);
            size++;
            return true;
        }
        Pair<Treap<T>, Treap<T>> treapPair = treap.split(t);
        Treap<T> l = treapPair.fst;
        Treap<T> r = treapPair.snd;
        Treap<T> newTreap = new Treap<>(t, (int)(Math.random() * 1000), null, null);
        treap = treap.merge(treap.merge(l, newTreap), r);
        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        @SuppressWarnings("unchecked")
        T t = (T) o;
        if (!contains(t)) {
            return false;
        }
        Treap<T> l, r;
        Pair<Treap<T>, Treap<T>> treapPair = treap.splitExclusive(t);
        l = treapPair.fst;
        r = treapPair.snd;
        Pair<Treap<T>, Treap<T>> rightPair = r.split(t);
        treap = treap.merge(l, rightPair.snd);
        size--;
        return true;
    }


    public Treap<T> find(T t) {
        if (treap == null || treap.data == null) return null;
        return find(treap, t);
    }


    private Treap<T> find(Treap<T> start, T t) {
        int comparison = t.compareTo(start.data);
        if (comparison == 0) {
            return start;
        }
        else if (comparison < 0) {
            if (start.left == null) return start;
            return find(start.left, t);
        }
        else {
            if (start.right == null) return start;
            return find(start.right, t);
        }
    }

    private int height(Treap<T> treap) {
        if (treap.data == null) {
            return 0;
        }
        return 1 + Math.max(height(treap.left), height(treap.right));
    }

    public int height() {
        return height(treap);
    }

    private T min(Treap<T> treap) {
        Treap<T> current = treap;
        while (current.left != null) {
            current = current.left;
        }
        return current.data;
    }

    private T max(Treap<T> treap) {
        Treap<T> current = treap;
        while (current.right != null) {
            current = current.right;
        }
        return current.data;
    }

    @Override
    public T lower(T t) {
        return headSet(t).last();
    }

    @Override
    public T floor(T t) {
        if (contains(t)) return t;
        return headSet(t, false).last();
    }

    @Override
    public T ceiling(T t) {
        if (contains(t)) return t;
        return tailSet(t).first();
    }

    @Override
    public T higher(T t) {
        return tailSet(t, false).first();
    }

    @Override
    public T pollFirst() {
        T t = min(treap);
        if (t != null) {
            remove(t);
        }
        return t;
    }


    @Override
    public T pollLast() {
        T t = max(treap);
        if (t != null) {
            remove(t);
        }
        return t;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return treap.data == null;
    }

    @Override
    public boolean contains(Object o) {
        @SuppressWarnings("unchecked")
        T t = (T) o;
        Treap<T> closest = find(t);
        return closest != null && t.compareTo(closest.data) == 0;
    }


    public class TreapIterator implements Iterator<T> {
        Stack<Treap> stack;
        T result = null;

        private TreapIterator() {
            stack = new Stack<>();
            Treap<T> treapIt = treap;
            while (treapIt != null) {
                stack.push(treapIt);
                treapIt = treapIt.left;
            }
        }

        @Override
        public boolean hasNext() {
            return !stack.isEmpty();
        }

        @Override
        @SuppressWarnings("unchecked")
        public T next() {
            Treap<T> treap = stack.pop();
            result = treap.data;
            if (treap.right != null) {
                treap = treap.right;
                while (treap != null) {
                    stack.push(treap);
                    treap = treap.left;
                }
            }
            return result;
        }
    }


    @Override
    public Iterator<T> iterator() {
        return new TreapIterator();
    }

    @Override
    public Object[] toArray() {
        Object[] array = new Object[size()];
        Iterator<T> iterator = iterator();
        for (int i = 0; i < array.length; i++) {
            array[i] = iterator.next();
        }
        return array;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T1> T1[] toArray(T1[] a) {
        int size = size();
        T1[] newArr = a.length >= size ? a :
                (T1[]) Array.newInstance(a.getClass().getComponentType(), size);
        Iterator<T> iterator = iterator();

        IntStream.range(0, size).forEach(i -> newArr[i] = (T1) iterator.next());

        return newArr;
    }


    @Override
    public boolean containsAll(Collection<?> c) {
        if (c == null || c.isEmpty()) {
            return false;
        }
        for (Object element : c) {
            if (!contains(element)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        if (c != null) c.forEach(this::add);
        return c != null && !c.isEmpty();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        List<?> toRemove = null;
        if (c != null) {
            toRemove = this.stream().filter(it -> !c.contains(it)).collect(Collectors.toList());
            toRemove.forEach(this::remove);
        }
        return !containsAll(toRemove);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        c.stream().filter(this::contains).forEach(this::remove);
        return !containsAll(c);
    }

    @Override
    public void clear() {
        clearTree(treap);
    }

    private void clearTree(Treap<T> treap) {
        if (treap != null) {
            clearTree(treap.left);
            clearTree(treap.right);
            remove(treap.data);
        }
    }

    @Override
    public NavigableSet<T> descendingSet() {
        TreapSet<T> treapSet = new TreapSet<>();
        treapSet.addAll(this);
        reverseTree(treapSet.treap);
        return treapSet;
    }

    @Override
    public Iterator<T> descendingIterator() {
        return descendingSet().iterator();
    }



    private void reverseTree(Treap<T> treap) {
        Treap<T> additionalTreap = treap.right;
        treap.right = treap.left;
        treap.left = additionalTreap;

        if (treap.left != null) {
            reverseTree(treap.left);
        }

        if (treap.right != null) {
            reverseTree(treap.right);
        }
    }

    class subSetTreap extends TreapSet<T> {
        private TreapSet<T> treap;
        private T lowerLimit;
        private boolean fromInclusive;
        private T upperLimit;
        private boolean toInclusive;


        subSetTreap(TreapSet<T> t, T down, boolean downInc, T up, boolean upInc) {
            treap = t;
            lowerLimit = down;
            fromInclusive = downInc;
            upperLimit = up;
            toInclusive = upInc;
        }




        private boolean inSubSet(T element) {
            if (lowerLimit != null && upperLimit != null) {
                if (fromInclusive && !toInclusive) {
                    return element.compareTo(lowerLimit) >= 0 && element.compareTo(upperLimit) < 0;
                } else if (!fromInclusive && !toInclusive) {
                    return element.compareTo(lowerLimit) > 0 && element.compareTo(upperLimit) < 0;
                } else if (fromInclusive) {
                    return element.compareTo(lowerLimit) >= 0 && element.compareTo(upperLimit) <= 0;
                } else {
                    return element.compareTo(lowerLimit) > 0 && element.compareTo(upperLimit) <= 0;
                }
            } else if (upperLimit != null) {
                if (toInclusive) {
                    return element.compareTo(upperLimit) <= 0;
                } else {
                    return element.compareTo(upperLimit) < 0;
                }
            } else if (lowerLimit != null) {
                if (fromInclusive) {
                    return element.compareTo(lowerLimit) >= 0;
                } else {
                    return element.compareTo(lowerLimit) > 0;
                }
            } else {
                return true;
            }
        }


        public boolean add(T element) {
            if (inSubSet(element)) {
                treap.add(element);
                return true;
            } else {
                throw new IllegalArgumentException();
            }
        }

        @SuppressWarnings("unchecked")
        public boolean contains(Object o) {

            if (inSubSet((T) o)) {
                return treap.contains(o);
            }
            return false;
        }


        public boolean remove(Object o) {
            if (contains(o)) {
                return treap.remove(o);
            } else {
                throw new IllegalArgumentException();
            }
        }


        public int size() {
            int size = 0;
            for (T element : treap) {
                if (inSubSet(element)) {
                    size++;
                }
            }
            return size;
        }
    }



    @Override
    public NavigableSet<T> subSet(T fromElement, boolean fromInclusive, T toElement, boolean toInclusive) {
        return new subSetTreap(this, fromElement, fromInclusive, toElement, toInclusive);
    }

    @Override
    public NavigableSet<T> headSet(T toElement, boolean inclusive) {
        return new subSetTreap(this, null, false, toElement, inclusive);
    }

    @Override
    public NavigableSet<T> tailSet(T fromElement, boolean inclusive) {
        return new subSetTreap(this, fromElement, inclusive, null, false);
    }

    @Override
    public Comparator<? super T> comparator() {
        return null;
    }

    @Override
    public SortedSet<T> subSet(T fromElement, T toElement) {
        return new subSetTreap(this, fromElement, true, toElement, false);
    }

    @Override
    public SortedSet<T> headSet(T toElement) {
        return new subSetTreap(this, null, false, toElement, false);
    }

    @Override
    public SortedSet<T> tailSet(T fromElement) {
        return new subSetTreap(this, fromElement, true, null, false);
    }

    @Override
    public T first() {
        if (treap.data == null) throw new NoSuchElementException();
        Treap<T> current = treap;
        while (current.left != null) {
            current = current.left;
        }
        return current.data;
    }

    @Override
    public T last() {
        if (treap.data == null) throw new NoSuchElementException();
        Treap<T> current = treap;
        while (current.right != null) {
            current = current.right;
        }
        return current.data;
    }
}
