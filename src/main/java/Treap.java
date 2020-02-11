
import com.sun.tools.javac.util.Pair;

public class Treap<T extends Comparable<T>>  {

    T data;
    int priority;
    Treap<T> left;
    Treap<T> right;


    public Treap(T data, int priority, Treap<T> left, Treap<T> right) {
        this.data = data;
        this.priority = priority;
        this.left = left;
        this.right = right;
    }

    public Treap() {

    }

    public Treap(T data) {
        this.data = data;
    }


    Treap<T> merge(Treap<T> left, Treap<T> right) {
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


    Pair<Treap<T>, Treap<T>> split(T t) {
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

    Pair<Treap<T>, Treap<T>> splitExclusive(T t) {
        Treap<T> newTree = null;
        Treap<T> l, r;
        Pair<Treap<T>, Treap<T>> splitPair;

        if (data.compareTo(t) < 0) {
            if (right == null) {
                r = null;
            } else {
                splitPair = right.splitExclusive(t);
                newTree = splitPair.fst;
                r = splitPair.snd;
            }
            l = new Treap<>(data, priority, left, newTree);
        } else {
            if (left == null) {
                l = null;
            } else {
                splitPair = left.splitExclusive(t);
                l = splitPair.fst;
                newTree = splitPair.snd;
            }
            r = new Treap<>(data, priority, newTree, right);
        }
        return new Pair<>(l, r);
    }
}