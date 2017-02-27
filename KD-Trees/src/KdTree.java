import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

public class KdTree {

    private static final int DIMENSION = 2;
    private static final boolean VERTICAL_TYPE = true;
    private static final boolean HORIZONTAL_TYPE = false;

    private Node root;

    // construct an empty set of points
    public KdTree() {

    }
    // is the set empty?
    public boolean isEmpty() {
        if (root == null)
            return true;

        return false;
    }
    // number of points in the set
    public int size() {

    }
    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null)
            throw new NullPointerException();

        if (isEmpty()) {
            root = new Node(p);
        }

        insertNewPoint(p);
    }
    private void insertNewPoint(Point2D p){
        int level = 0;
        boolean dimenType;
        Node cur = root;

        while(true) {
            if (cur.point.equals(p))
                return;

            dimenType = (level % DIMENSION != 0);
            if (isPointLess(p, cur, dimenType)) {
                if (cur.left == null) {
                 Node newNode = new Node(p);
                 cur.left = newNode;
                 return;
                } else {
                    cur = cur.left;
                }
            } else {
                if (cur.right == null) {
                    Node newNode = new Node(p);
                    cur.right = newNode;
                    return;
                } else {
                    cur = cur.right;
                }
            }
            level++;
        }
    }

    private boolean isPointLess(Point2D p1, Point2D p2, boolean dimenType) {
        switch (dimenType) {
            case VERTICAL_TYPE:
                return p1.y() < p2.y();
                break;
            case HORIZONTAL_TYPE:
                return p1.x() < p2.x();
                break;
            default:
                return false;
        }
    }
    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null)
            throw new NullPointerException();

    }
    // draw all points to standard draw
    public void draw() {

    }
    // all points that are inside the rectangle
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null)
            throw new NullPointerException();

    }
    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null)
            throw new NullPointerException();
    }

    private static class Node {

        private Point2D point;
        private RectHV rect;
        private Node left;
        private Node right;
        private boolean type;

        private Node(Point2D point) {
            this.point = point;
        }

    }

}