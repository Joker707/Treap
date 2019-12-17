import com.sun.scenario.effect.Merge;
import javafx.util.Pair;

public class Treap {
    private int x;
    private int y;
    private Treap left;
    private Treap right;

    public Treap(int x, int y, Treap left, Treap right) {
        this.x = x;
        this.y = y;
        this.left = left;
        this.right = right;
    }

    public Treap merge(Treap left, Treap right) {
        if (left == null) {
            return right;
        }
        if (right == null) {
            return left;
        }

        if (left.y > right.y) {
            Treap newRight = merge(left.right, right);
            return new Treap(left.x, left.y, left.left, newRight);
        } else {
            Treap newLeft = merge(left, right.left);
            return new Treap(right.x, right.y, newLeft, right.right);
        }
    }


    public Pair<Treap, Treap> split(int x) {
        Treap newTree = null;
        Treap l, r;
        Pair<Treap, Treap> splitPair;

        if (this.x <= x) {
            if (right == null) {
                r = null;
            } else {
                splitPair = right.split(x);
                newTree = splitPair.getKey();
                r = splitPair.getValue();
            }
            l = new Treap(this.x, y, left, newTree);
        } else {
            if (left == null) {
                l = null;
            } else {
                splitPair = left.split(x);
                l = splitPair.getKey();
                newTree = splitPair.getValue();
            }
            r = new Treap(this.x, y, newTree, right);
        }
        return new Pair<Treap, Treap>(l, r);
    }


    public void add(int x) {
        Pair<Treap, Treap> treapPair = split(x);
        Treap l = treapPair.getKey();
        Treap r = treapPair.getValue();
        Treap newTreap = new Treap(x, (int)(Math.random() * 1000), null, null);
        merge(merge(l, newTreap), r);
    }



    public void remove(int x) {
        Treap l, r, rightl, rightr, treapToDelete;
        Pair<Treap, Treap> treapPair = split(x - 1);
        l = treapPair.getKey();
        r = treapPair.getValue();
        Pair<Treap, Treap> rightPair = split(x);
        merge(l, rightPair.getValue());
    }

}
