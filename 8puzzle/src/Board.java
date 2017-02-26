import edu.princeton.cs.algs4.Stack;

public class Board {

    private static final int BLANK = 0;
    private static final int NOT_INITIALIZED_VALUE = -10;

    private final int[] mBlocks;
    private final int blocksSize;

    private int blankIndex = NOT_INITIALIZED_VALUE;
    private int manhattan = NOT_INITIALIZED_VALUE;
    private int hamming = NOT_INITIALIZED_VALUE;

    // construct a board from an n-by-n array of blocks
    // (where blocks[i][j] = block in row i, column j)
    public Board(int[][] blocks) {
        if (blocks == null)
            throw new NullPointerException();

        blocksSize = blocks.length;
        mBlocks = new int[blocksSize * blocksSize];

        for (int i = 0; i < blocksSize; i++) {
            for (int j = 0; j < blocksSize; j++) {
                mBlocks[i * blocksSize + j] = blocks[i][j];
            }
        }
    }
    private Board(int[] blocks) {
        blocksSize = (int) Math.sqrt(blocks.length);
        mBlocks = new int[blocks.length];
        for (int i = 0; i < blocks.length; i++)
            mBlocks[i] = blocks[i];
    }
    // board dimension n
    public int dimension() {
        return blocksSize;
    }
    // number of blocks out of place
    public int hamming() {
        if (hamming == NOT_INITIALIZED_VALUE) {
            hamming = 0;
            for (int i = 0; i < blocksSize * blocksSize; i++) {
                    if (mBlocks[i] != BLANK && (i + 1) != mBlocks[i])
                        hamming++;
            }
        }
        return hamming;
    }
    // sum of Manhattan distances between blocks and goal
    public int manhattan() {
        if (manhattan == NOT_INITIALIZED_VALUE) {
            manhattan = 0;
            for (int i = 0; i < blocksSize * blocksSize; i++) {
                if (mBlocks[i] != BLANK && mBlocks[i] != (i + 1)) {
                    int curRow =  i / blocksSize;
                    int curCol =  i % blocksSize;
                    int expRow = (mBlocks[i] - 1) / blocksSize;
                    int expCol = (mBlocks[i] - 1) % blocksSize;
                    manhattan += Math.abs(expRow - curRow);
                    manhattan += Math.abs(expCol - curCol);
                }

            }
        }
        return manhattan;
    }
    // is this board the goal board?
    public boolean isGoal() {
        return manhattan() == 0;
    }
    // a board that is obtained by exchanging any pair of blocks
    public Board twin() {

        for (int i = 0; i < blocksSize * blocksSize - 1; i++) {
            if (mBlocks[i] != BLANK && mBlocks[i + 1] != BLANK)
                return new Board(exchangedBoard(i, i + 1));
        }

        return null;
    }
    // does this board equal y?
    public boolean equals(Object y) {
        if (y == null)
            return false;

        if (y == this)
            return true;

        if (y.getClass() != this.getClass())
            return false;

        Board that = (Board) y;

        if (this.dimension() != that.dimension())
            return false;

        int[] thatBlocks = that.mBlocks;

        for (int i = 0; i < dimension() * dimension(); i++) {
                if (mBlocks[i] != thatBlocks[i])
                    return false;
        }
        return true;
    }
    // all neighboring boards
    public Iterable<Board> neighbors() {
        Stack<Board> mNeighbors = new Stack<Board>();
            int blankRow = getRowIndexOfBlank();
            int blankColumn = getColumnIndexOfBlank();
            // exchange blank with top
            if (blankRow > 0)
                mNeighbors.push(new Board(
                        exchangedBoard(blankRow * dimension() + blankColumn,
                                (blankRow - 1) * dimension() + blankColumn)));
            // exchange blank with bottom
            if (blankRow < maxIndex())
                mNeighbors.push(new Board(
                        exchangedBoard(blankRow * dimension() + blankColumn,
                                (blankRow + 1) * dimension() + blankColumn)));
            // exchange blank with left
            if (blankColumn > 0)
                mNeighbors.push(new Board(
                        exchangedBoard(blankRow * dimension() + blankColumn,
                                blankRow * dimension() + blankColumn - 1)));
            // exchange blank with right
            if (blankColumn < maxIndex())
                mNeighbors.push(new Board(
                        exchangedBoard(blankRow * dimension() + blankColumn,
                                blankRow * dimension() + blankColumn + 1)));

        return mNeighbors;
    }
    // string representation of this board (in the output format specified below)
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(dimension()).append('\n');
        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++) {
                builder.append(String.format("%2d ", mBlocks[i * dimension() + j]));
            }
            builder.append('\n');
        }
        return builder.toString();
    }
    private int maxIndex() {
        return dimension() - 1;
    }
    private int getRowIndexOfBlank() {
        return getBlankIndex() / dimension();
    }
    private int getColumnIndexOfBlank() {
        return getBlankIndex() % dimension();
    }
    private int getBlankIndex() {
        if (blankIndex == NOT_INITIALIZED_VALUE) {
            for (int i = 0; i < mBlocks.length; i++) {
                if (mBlocks[i] == BLANK) {
                    blankIndex = i;
                    break;
                }
            }
        }
        return blankIndex;
    }
    private int[] exchangedBoard(int i1, int i2) {
        int[] exchangedBlocks = new int[dimension() * dimension()];
        for (int i = 0; i < dimension() * dimension(); i++) {
                exchangedBlocks[i] = mBlocks[i];
        }

        int tmp = mBlocks[i1];
        exchangedBlocks[i1] = mBlocks[i2];
        exchangedBlocks[i2] = tmp;

        return exchangedBlocks;

    }
}
