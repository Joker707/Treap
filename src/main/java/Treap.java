
import com.sun.tools.javac.util.Pair;

import javax.xml.soap.Node;
import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Treap<T extends Comparable<T>> implements NavigableSet<T> {

    private T data;
    private int priority;
    private Treap<T> left;
    private Treap<T> right;


    public Treap(T data, int priority, Treap<T> left, Treap<T> right) {
        this.data = data;
        this.priority = priority;
        this.left = left;
        this.right = right;
    }

    public Treap() {

    }

    private Treap<T> root = null;

    private int size = 0;

    private Treap<T> merge(Treap<T> left, Treap<T> right) {
        if (left == null) {
            return right;
        }
        if (right == null) {
            return left;
        }

        if (left.priority > right.priority) {
            Treap<T> newRight = merge(left.right, right);
            return new Treap<>(left.data, left.priority, left.left, newRight);
        } else {
            Treap<T> newLeft = merge(left, right.left);
            return new Treap<>(right.data, right.priority, newLeft, right.right);
        }
    }


    private Pair<Treap<T>, Treap<T>> split(T t) {
        Treap<T> newTree = null;
        Treap<T> l, r;
        Pair<Treap<T>, Treap<T>> splitPair;

        if (data.compareTo(t) <= 0) {
            if (right == null) {
                r = null;
            } else {
                splitPair = right.split(t);
                newTree = splitPair.fst;
                r = splitPair.snd;
            }
            l = new Treap<>(data, priority, left, newTree);
        } else {
            if (left == null) {
                l = null;
            } else {
                splitPair = left.split(t);
                l = splitPair.fst;
                newTree = splitPair.snd;
            }
            r = new Treap<>(data, priority, newTree, right);
        }
        return new Pair<>(l, r);
    }


    @Override
    public boolean add(T t) {
        Treap<T> closest = find(t);
        int comparison = closest == null ? -1 : t.compareTo(closest.data);
        if (comparison == 0) {
            return false;
        }
        Pair<Treap<T>, Treap<T>> treapPair = split(t);
        Treap<T> l = treapPair.fst;
        Treap<T> r = treapPair.snd;
        Treap<T> newTreap = new Treap<>(t, (int)(Math.random() * 1000), null, null);
        merge(merge(l, newTreap), r);
        size++;
        return true;
    }


    @Override
    public boolean remove(Object o) {
        @SuppressWarnings("unchecked")
        T t = (T) o;
        if (contains(t)) {
            return !contains(removeT(t));
        }
        return false;
    }


    private Treap<T> removeT(T t) {
        if (data.compareTo(t) == 0) {
            return merge(left, right);
        } else if (data.compareTo(t) > 0) {
            if (left != null) {
                left = left.removeT(t);
            }
        } else {
            if (right != null)
                right = right.removeT(t);
        }
        size--;
        return this;
    }


    private Treap<T> find(T t) {
        if (root == null) return null;
        return find(root, t);
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
        T t = min(root);
        if (t != null) {
            remove(t);
        }
        return t;
    }


    @Override
    public T pollLast() {
        T t = max(root);
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
        return root == null;
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
            Treap<T> treap = root;
            while (treap != null) {
                stack.push(treap);
                treap = treap.left;
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
        return c != null && !c.isEmpty() && c.stream().anyMatch(this::contains);
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
        clearTree(root);
    }

    private void clearTree(Treap<T> treap) {
        if (treap != null) {
            clearTree(treap.left);
            clearTree(treap.right);
            remove(treap);
            size--;
        }
    }

    @Override
    public NavigableSet<T> descendingSet() {
        Treap<T> treap = new Treap<>();
        treap.addAll(this);
        reverseTree(treap.root);
        return treap;
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

    class subSetTreap extends Treap<T> {
        private Treap<T> treap;
        private T lowerLimit;
        private boolean fromInclusive;
        private T upperLimit;
        private boolean toInclusive;


        public subSetTreap(Treap<T> t, T down, boolean downInc, T up, boolean upInc) {
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
        if (root == null) throw new NoSuchElementException();
        Treap<T> current = root;
        while (current.left != null) {
            current = current.left;
        }
        return current.data;
    }

    @Override
    public T last() {
        if (root == null) throw new NoSuchElementException();
        Treap<T> current = root;
        while (current.right != null) {
            current = current.right;
        }
        return current.data;
    }
}