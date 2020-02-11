import org.junit.Test;

import java.util.LinkedList;
import java.util.NavigableSet;
import java.util.Random;
import java.util.SortedSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TreapTest {


    @Test
    public void testAdd() {
        NavigableSet<Integer> treap = new TreapSet<>();
        LinkedList<Integer> list = new LinkedList<>();
        treap.add(5);
        treap.add(1);
        treap.add(15);
        treap.add(20);
        assertTrue(treap.remove(1));
        assertFalse(treap.remove(1));
        treap.add(7);
        assertTrue(treap.contains(5));
        assertTrue(treap.contains(15));
        assertFalse(treap.contains(16));
        assertFalse(treap.contains(10));

        treap.clear();
        for (int i = 0; i < 20; i++) {
            list.add(i);
            treap.add(i);
        }
        assertTrue(treap.containsAll(list));
        list.add(30);
        assertFalse(treap.containsAll(list));


    }

    @Test
    public void testRemove() {
        NavigableSet<Integer> treap = new TreapSet<>();
        LinkedList<Integer> list = new LinkedList<>();

        int value;
        int sum1, sum2;
        int[] array;
        array = new int[100];

        while( list.size() != 100) {
            value = (int) (Math.random() * 1000);
            if (!list.contains(value)) {
                list.add(value);
                array[list.size() - 1] = value;
            }
        }
        System.out.println(list);

        for (int i = 0; i < 100; i++) {
            treap.add(array[i]);
        }
        sum1 = ((list.size() + 1) * (list.size()) / 2);
        sum2 = 0;
        for (int i = 0; i < list.size(); i++) {
            treap.remove(array[i]);
            if (treap.size() == 0 || !treap.contains(array[i])) {
                sum2++;
            } else {
                break;
            }
            for (int j = i + 1; j < list.size() ; j++) {
                if (treap.contains(array[j])) {
                    sum2++;
                } else {
                    break;
                }
            }
        }
        assertEquals(sum1, sum2);
    }

    @Test
    public void headSetTest() {
        NavigableSet<Integer> treap = new TreapSet<>();
        treap.add(5);
        treap.add(1);
        treap.add(2);
        treap.add(7);
        treap.add(9);
        treap.add(10);
        treap.add(8);
        treap.add(4);
        treap.add(3);
        treap.add(6);

        SortedSet<Integer> headSet = treap.headSet(5);
        assertTrue(headSet.contains(1));
        assertTrue(headSet.contains(2));
        assertTrue(headSet.contains(3));
        assertTrue(headSet.contains(4));
        assertFalse(headSet.contains(5));
        assertFalse(headSet.contains(6));
        assertFalse(headSet.contains(7));
        assertFalse(headSet.contains(8));
        assertFalse(headSet.contains(9));
        assertFalse(headSet.contains(10));
    }

    @Test
    public void tailSetTest() {
        NavigableSet<Integer> treap = new TreapSet<>();
        treap.add(6);
        treap.add(2);
        treap.add(1);
        treap.add(8);
        treap.add(7);
        treap.add(4);
        treap.add(9);
        treap.add(10);
        treap.add(3);
        treap.add(5);

        SortedSet<Integer> tailSet = treap.tailSet(5);
        assertFalse(tailSet.contains(1));
        assertFalse(tailSet.contains(2));
        assertFalse(tailSet.contains(3));
        assertFalse(tailSet.contains(4));
        assertTrue(tailSet.contains(5));
        assertTrue(tailSet.contains(6));
        assertTrue(tailSet.contains(7));
        assertTrue(tailSet.contains(8));
        assertTrue(tailSet.contains(9));
        assertTrue(tailSet.contains(10));
    }

}
