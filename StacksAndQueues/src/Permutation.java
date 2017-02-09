import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Permutation {
    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);
        RandomizedQueue<String> rndQueue = new RandomizedQueue<String>();
        String string;
        while (!StdIn.isEmpty()) {
            string = StdIn.readString();
            rndQueue.enqueue(string);
        }

        for (int i = 0; i < k; i++)
            StdOut.println(rndQueue.dequeue());
    }
}
