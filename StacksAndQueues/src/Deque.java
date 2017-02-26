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

        if (count == 1) {
            addFirstItemIntoQueue(item);
        } else if (first == null) {
            first = new Node<Item>();
            first.item = item;
            first.next = last;
            last = first;
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

    private void addFirstItemIntoQueue(Item item) {
        Node<Item> node = new Node<Item>();
        node.item = item;
        first = node;
        last = node;
    }
    public Item removeFirst() {
        if (first == null)
            throw new java.util.NoSuchElementException();

        count--;

        if (count == 1) {
            return staySingleItemInQueue(true);
        } else if (count == 0) {
            return removeTheLastOne(true);
        }

        Item item = first.item;
        first = first.next;
        first.previous = null;

        return item;
    }
    public Item removeLast() {
        if (last == null)
            throw new java.util.NoSuchElementException();

        count--;

        if (count == 1) {
            return staySingleItemInQueue(false);
        } else if (count == 0)
            return removeTheLastOne(false);

        Item item = last.item;
        last = last.previous;
        last.next = null;
        return item;

    }
    private Item staySingleItemInQueue(boolean isFromFirst) {
        Item item;
        if (isFromFirst) {
            item = first.item;
            first = last;
            first.previous = null;
            first.next = null;
        } else {
            item = last.item;
            last = first;
            last.next = null;
            last.previous = null;
        }
        return item;
    }

    private Item removeTheLastOne(boolean isFromFirst) {
        Item item;
        if (isFromFirst)
            item = first.item;
        else
            item = last.item;
        first = null;
        last = null;
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
