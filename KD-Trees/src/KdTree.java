import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

import java.util.ArrayList;
import java.util.List;

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
        return 0;
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
    private void insertNewPoint(Point2D p) {
        int level = 0;
        boolean dimenType;
        Node cur = root;

        while(true) {
            if (cur.point.equals(p))
                return;

            dimenType = (level % DIMENSION != 0);
            if (isPointLess(p, cur.point, dimenType)) {
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
        if  (dimenType == VERTICAL_TYPE)
            return p1.y() < p2.y();
        else
            return p1.x() < p2.x();
    }
    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null)
            throw new NullPointerException();

        if (root == null)
            return false;

        return (findPoint(p) != null);
    }

    private Point2D findPoint(Point2D p) {
        int level = 0;
        boolean dimenType;
        Node cur = root;

        while(true) {
            if (cur.point.equals(p))
                return cur.point;

            dimenType = (level % DIMENSION != 0);
            if (isPointLess(p, cur.point, dimenType)) {
                if (cur.left == null)
                    return null;
                else
                    cur = cur.left;
            } else {
                if (cur.right == null)
                    return null;
                else
                    cur = cur.right;
            }
            level++;
        }
    }
    // draw all points to standard draw
    public void draw() {

    }
    // all points that are inside the rectangle
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null)
            throw new NullPointerException();

        return new ArrayList<Point2D>();
    }
    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null)
            throw new NullPointerException();
        return new Point2D(1, 1);
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

    public static void main(String[] args) {
        KdTree tree = new KdTree();
        Point2D p1 = new Point2D(0.5, 0.5);
        Point2D p2 = new Point2D(0.25, 0.75);
        Point2D p3 = new Point2D(0.7, 0.25);
        Point2D p4 = new Point2D(0.2, 0.3);
        Point2D p5 = new Point2D(0.8, 0.9);
        Point2D fake = new Point2D(0.01, 0.02);
        tree.insert(p1);
        tree.insert(p2);
        tree.insert(p3);
        tree.insert(p4);
        tree.insert(p5);

        System.out.println("tree.contains(p1)? " + tree.contains(p1));
        System.out.println("tree.contains(p2)? " + tree.contains(p2));
        System.out.println("tree.contains(p3)? " + tree.contains(p3));
        System.out.println("tree.contains(p4)? " + tree.contains(p4));
        System.out.println("tree.contains(p5)? " + tree.contains(p5));
        System.out.println("tree.contains(fake)? " + tree.contains(fake));

    }

}