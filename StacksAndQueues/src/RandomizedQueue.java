import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private Item[] array;
    private int count;
    private int capacity = 1;
    // construct an empty randomized queue
    public RandomizedQueue() {
        count = 0;
        array = (Item[]) new Object[capacity];
    }
    // is the queue empty?
    public boolean isEmpty() {
        return (count == 0);
    }
    // return the number of items on the queue
    public int size() {
        return count;
    }
    // add the item
    public void enqueue(Item item) {
        if (item == null)
            throw new NullPointerException();

        if (count == array.length)
            doubleArray();
        array[count] = item;
        count++;
    }
    // remove and return a random item
    public Item dequeue() {
        if (isEmpty())
            throw new NoSuchElementException();

        int rndIndex = StdRandom.uniform(count);

        Item item = array[rndIndex];
        count--;
        array[rndIndex] = array[count];
        array[count] = null;
        if (count < (capacity/4))
            shrinkArray();
        return item;
    }
    // return (but do not remove) a random item
    public Item sample() {
        if (isEmpty())
            throw new NoSuchElementException();

        int rndInex = StdRandom.uniform(count);
        Item item = array[rndInex];
        return item;
    }

    @Override
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    private void doubleArray() {
        capacity = 2 * capacity;
        Item[] newArray = (Item[]) new Object[capacity];
        for (int i = 0; i < count; i++) {
            newArray[i] = array[i];
        }
        array = newArray;
    }
    private void shrinkArray() {
        capacity = capacity/2;
        Item[] newObjectsArray = (Item[]) new Object[capacity];

        for (int i = 0; i < count; i++) {
            newObjectsArray[i] = array[i];
        }
        array = newObjectsArray;
    }

    private class RandomizedQueueIterator implements Iterator<Item> {

        private int[] order;
        private int current = 0;

        public RandomizedQueueIterator() {
            order = new int[count];
            for (int i = 0; i < count; i++)
                order[i] = i;

            StdRandom.shuffle(order);
        }
        @Override
        public boolean hasNext() {
            return (current < (count - 1));
        }

        @Override
        public Item next() {
            if (!hasNext())
                throw new NoSuchElementException();

            Item item = (Item) array[order[current]];
            current++;
            return item;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
