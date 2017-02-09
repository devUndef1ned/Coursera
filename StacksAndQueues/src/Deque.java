import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

    private Node<Item> first;
    private Node<Item> last;
    private int count;

    public Deque() {
        first = null;
        last = null;
        count = 0;
    }
    public boolean isEmpty() {
        return (first == null && last == null);
    }
    public int size() {
        return count;
    }
    public void addFirst(Item item) {
        if (item == null)
            throw new NullPointerException();

        count++;

        if (first == null) {
            first = new Node<Item>();
            first.item = item;
            if (last != null)
                first.next = last;
            else {
                last = first;
            }
        } else {
            Node<Item> oldFirst = first;
            first = new Node<Item>();
            first.item = item;
            first.next = oldFirst;
            oldFirst.previous = first;
            if (last == null) {
                last = oldFirst;
            }
        }
    }
    public void addLast(Item item) {
        if (item == null)
            throw new NullPointerException();
        if (first == null)
            addFirst(item);
        else {
            count++;
            Node<Item> oldLast = last;
            last = new Node<Item>();
            oldLast.next = last;
            last.item = item;
            last.previous = oldLast;
        }
    }
    public Item removeFirst() {
        if (first == null)
            throw new java.util.NoSuchElementException();

        count--;
        Item item = first.item;

        first = first.next;

        if (first == null)
            last = null;
        else {
            first.previous = null;
        }

        return item;
    }
    public Item removeLast() {
        if (last == null)
            throw new java.util.NoSuchElementException();

        count--;
        Item item = last.item;
        last = last.previous;
        last.next = null;
        if (last == null)
            first = null;

        return item;
    }
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    private class DequeIterator implements Iterator<Item> {

        private Node<Item> currentItem = first;
        @Override
        public boolean hasNext() {
            return currentItem == null;
        }

        @Override
        public Item next() {
            if (currentItem == null)
                throw new NoSuchElementException();

            Item item = currentItem.item;
            currentItem = currentItem.next;
            return item;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    private class Node<Item> {
       private Item item;
       private Node<Item> next;
       private Node<Item> previous;
    }
}
