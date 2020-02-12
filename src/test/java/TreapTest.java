import org.junit.Test;

import java.util.*;

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


    @Test
    public void subSetTest() {
        NavigableSet<Integer> treap = new TreapSet<>();
        for (int i = 1; i < 21; i++) {
            treap.add(i);
        }
        SortedSet<Integer> subSet = treap.subSet(5, 15);
        assertFalse(subSet.contains(1));
        assertFalse(subSet.contains(2));
        assertFalse(subSet.contains(3));
        assertFalse(subSet.contains(4));
        assertTrue(subSet.contains(5));
        assertTrue(subSet.contains(6));
        assertTrue(subSet.contains(7));
        assertTrue(subSet.contains(8));
        assertTrue(subSet.contains(9));
        assertTrue(subSet.contains(10));
        assertTrue(subSet.contains(11));
        assertTrue(subSet.contains(12));
        assertTrue(subSet.contains(13));
        assertTrue(subSet.contains(14));
        assertFalse(subSet.contains(15));
        assertFalse(subSet.contains(16));
        assertFalse(subSet.contains(17));
        assertFalse(subSet.contains(18));
        assertFalse(subSet.contains(19));
        assertFalse(subSet.contains(20));

        NavigableSet<Integer> navigableSubSet = treap.subSet(7, true, 13, true);
        assertFalse(navigableSubSet.contains(1));
        assertFalse(navigableSubSet.contains(2));
        assertFalse(navigableSubSet.contains(3));
        assertFalse(navigableSubSet.contains(4));
        assertFalse(navigableSubSet.contains(5));
        assertFalse(navigableSubSet.contains(6));
        assertTrue(navigableSubSet.contains(7));
        assertTrue(navigableSubSet.contains(8));
        assertTrue(navigableSubSet.contains(9));
        assertTrue(navigableSubSet.contains(10));
        assertTrue(navigableSubSet.contains(11));
        assertTrue(navigableSubSet.contains(12));
        assertTrue(navigableSubSet.contains(13));
        assertFalse(navigableSubSet.contains(14));
        assertFalse(navigableSubSet.contains(15));
        assertFalse(navigableSubSet.contains(16));
        assertFalse(navigableSubSet.contains(17));
        assertFalse(navigableSubSet.contains(18));
        assertFalse(navigableSubSet.contains(19));
        assertFalse(navigableSubSet.contains(20));
    }


    @Test
    public void compareWithTreeSetTest() {
        NavigableSet<Integer> treapSet = new TreapSet<>();
        NavigableSet<Integer> treeSet = new TreeSet<>();
        for (int i = 0; i < 100; i++) {
            treapSet.add(i);
            treeSet.add(i);
        }
        assertEquals(treeSet, treapSet);
        treeSet.remove(50);
        treapSet.remove(50);
        assertEquals(treeSet, treapSet);
        for (int i = 0; i < 5; i++) {
            treeSet.remove(70 - i);
            treapSet.remove(70 - i);
        }
        assertEquals(treeSet, treapSet);
    }
}
