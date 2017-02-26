import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {

    private SearchNode solutionNode;
    private Stack<Board> solution;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null)
            throw new NullPointerException();

        final Board initialBoard = initial;

        final MinPQ<SearchNode> mQueue = new MinPQ<SearchNode>();
        final MinPQ<SearchNode> mTwinedQueue = new MinPQ<SearchNode>();

        mQueue.insert(new SearchNode(initialBoard, null));
        mTwinedQueue.insert(new SearchNode(initialBoard.twin(), null));

        while (true) {

            solutionNode = insertNeighborsToQueueAndGetMin(mQueue);
            if (solutionNode != null || insertNeighborsToQueueAndGetMin(mTwinedQueue) != null)
                break;
            }
        }

    private SearchNode insertNeighborsToQueueAndGetMin(MinPQ<SearchNode> queue) {
        SearchNode min = queue.delMin();

        if (min.getBoard().isGoal())
            return min;

        Iterable<Board> iterable = min.getBoard().neighbors();
        for (Board board: iterable) {
            if ((min.getPreviousNode() == null) || !(board.equals(min.getPreviousNode().getBoard())))
                queue.insert(new SearchNode(board, min));
        }

        return null;
    }
    // is the initial board solvable?
    public boolean isSolvable() {
        return (solutionNode != null);
    }
    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (isSolvable())
            return solutionNode.moves;
        else
            return -1;
    }
    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!isSolvable())
            return null;

        if (solution == null) {
            solution = new Stack<Board>();
            SearchNode cur = solutionNode;
            while (true) {
                solution.push(cur.getBoard());
                if (cur.getPreviousNode() == null)
                    break;
                else
                    cur = cur.getPreviousNode();
            }
        }

        return solution;
    }

    private class SearchNode implements Comparable<SearchNode> {
        private Board mBoard;
        private SearchNode mPreviousNode;
        private int moves;
        private int priority;

        public SearchNode(Board thisBoard, SearchNode previousNode) {
            mBoard = thisBoard;
            mPreviousNode = previousNode;
            if (previousNode == null)
                moves = 0;
            else
                moves = previousNode.moves + 1;
            priority = moves + mBoard.manhattan();
        }

        public int priority() {
            return priority;
        }

        public Board getBoard() {
            return mBoard;
        }

        public SearchNode getPreviousNode() {
            return mPreviousNode;
        }

        @Override
        public int compareTo(SearchNode o) {
            int diff = priority - o.priority;

            if (diff == 0)
                diff = getBoard().hamming() - o.getBoard().hamming();

           return diff;
        }
    }

    // solve a slider puzzle (given below)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
