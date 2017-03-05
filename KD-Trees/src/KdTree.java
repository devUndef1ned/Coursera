import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;
import java.util.List;

public class KdTree {

    private static final int DIMENSION = 2;
    private static final int MAX_COORDINATE_VALUE = 1;
    private static final int MIN_COORDINATE_VALUE = 0;
    private static final boolean VERTICAL_DIVIDER_TYPE = true;
    private static final boolean HORIZONTAL_DIVIDER_TYPE = false;

    private Node root;
    private int size;

    // construct an empty set of points
    public KdTree() {
        size = 0;
    }

    // is the set empty?
    public boolean isEmpty() {
        if (root == null)
            return true;

        return false;
    }

    // number of points in the set
    public int size() {
        return size;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null)
            throw new NullPointerException();

        if (isEmpty()) {
            root = new Node(p, null, VERTICAL_DIVIDER_TYPE);
            root.prepareRectHV();
            size++;
            return;
        }

        insertNewPoint(p);
    }

    private void insertNewPoint(Point2D p) {
        int level = 0;
        boolean dimenType;
        Node cur = root;

        while (true) {
            if (cur.point.equals(p))
                return;

            dimenType = (level % DIMENSION != 0);
            if (isPointLess(p, cur.point, !dimenType)) {
                if (cur.left == null) {
                    Node newNode = new Node(p, cur, dimenType);
                    cur.left = newNode;
                    newNode.prepareRectHV();
                    size++;
                    return;
                } else {
                    cur = cur.left;
                }
            } else {
                if (cur.point.equals(p))
                    return;
                if (cur.right == null) {
                    Node newNode = new Node(p, cur, dimenType);
                    cur.right = newNode;
                    newNode.prepareRectHV();
                    size++;
                    return;
                } else {
                    cur = cur.right;
                }
            }
            level++;
        }
    }

    private static boolean isPointLess(Point2D point, Point2D basePoint, boolean baseDividerType) {
        if (baseDividerType != VERTICAL_DIVIDER_TYPE)
            return point.y() < basePoint.y();
        else
            return point.x() < basePoint.x();
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
        boolean dimenType;
        Node cur = root;

        while (true) {
            if (cur.point.equals(p))
                return cur.point;

            if (isPointLess(p, cur.point, cur.dividerType)) {
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
        }
    }

    // draw all points to standard draw
    public void draw() {
        StdDraw.setXscale(0, MAX_COORDINATE_VALUE);
        StdDraw.setYscale(0, MAX_COORDINATE_VALUE);
        for (Node node: getNodeSet())
            node.draw();
    }

    // all points that are inside the rectangle
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null)
            throw new NullPointerException();

        List<Point2D> pointsInRect = new ArrayList<Point2D>();
        if (isEmpty())
            return pointsInRect;

        recursivelyFindAllPointsInRectAndInsertInList(root, rect, pointsInRect);

        return pointsInRect;
    }

    private void recursivelyFindAllPointsInRectAndInsertInList(Node rootNode, final RectHV rect,
                                                               final List<Point2D> list) {
        if (rect.contains(rootNode.point)) {
            list.add(rootNode.point);
            if (rootNode.left != null)
                recursivelyFindAllPointsInRectAndInsertInList(rootNode.left, rect, list);
            if (rootNode.right != null)
                recursivelyFindAllPointsInRectAndInsertInList(rootNode.right, rect, list);
        } else if (isPointLess(new Point2D(rect.xmax(), rect.ymax()), rootNode.point, rootNode.dividerType) &&
                rootNode.left != null) {
            recursivelyFindAllPointsInRectAndInsertInList(rootNode.left, rect, list);
        } else if (isPointLess(new Point2D(rect.xmin(), rect.ymin()), rootNode.point, rootNode.dividerType) &&
                rootNode.left != null) {
            recursivelyFindAllPointsInRectAndInsertInList(rootNode.left, rect, list);
            if (rootNode.right != null)
                recursivelyFindAllPointsInRectAndInsertInList(rootNode.right, rect, list);
        } else if (rootNode.right != null) {
            recursivelyFindAllPointsInRectAndInsertInList(rootNode.right, rect, list);
        }
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null)
            throw new NullPointerException();

        if (isEmpty())
            return null;

        Node nearest = findNearestPointRecursively(p, root, root);

        return nearest.point;
    }
    private Node findNearestPointRecursively(Point2D queryPoint, Node rootNode, Node bestNode) {

        if (queryPoint.distanceSquaredTo(bestNode.point) > queryPoint.distanceSquaredTo(rootNode.point)) {
            bestNode = rootNode;
        }

        if (isPointLess(queryPoint, rootNode.point, rootNode.dividerType) && rootNode.left != null) {
            bestNode = findNearestPointRecursively(queryPoint, rootNode.left, bestNode);

            if (canBeInAnotherSubTree(queryPoint, rootNode, bestNode) && rootNode.right != null)
                bestNode = findNearestPointRecursively(queryPoint, rootNode.right, bestNode);

        } else if (rootNode.right != null) {
            bestNode = findNearestPointRecursively(queryPoint, rootNode.right, bestNode);

            if (canBeInAnotherSubTree(queryPoint, rootNode, bestNode) && rootNode.left != null)
                bestNode = findNearestPointRecursively(queryPoint, rootNode.left, bestNode);
        }

        return bestNode;
    }

    private boolean canBeInAnotherSubTree(Point2D queryPoint, Node node, Node bestNode) {
        double distanceToSplittingLine;
        if (node.dividerType == VERTICAL_DIVIDER_TYPE)
            distanceToSplittingLine = queryPoint.distanceSquaredTo(new Point2D(node.point.x(), queryPoint.y()));
        else
            distanceToSplittingLine = queryPoint.distanceSquaredTo(new Point2D(queryPoint.x(), node.point.y()));

        return distanceToSplittingLine < queryPoint.distanceSquaredTo(bestNode.point);
    }

    private List<Node> getNodeSet() {
        ArrayList<Node> points = new ArrayList<Node>();

        if (root != null)
            insertNodeAndAllChildToList(root, points);

        return points;
    }
    private void insertNodeAndAllChildToList(Node rootNode, final List<Node> list) {
        list.add(rootNode);

        if (rootNode.left != null) {
            insertNodeAndAllChildToList(rootNode.left, list);
        }
        if (rootNode.right != null)
            insertNodeAndAllChildToList(rootNode.right, list);
        if (rootNode.left == null && rootNode.right == null)
            return;
    }


    private static class Node {

        private Point2D point;
        private RectHV rect;
        private Node left;
        private Node right;
        private Node parent;
        private boolean dividerType;

        private Node(Point2D point, Node parent, boolean dividerType) {
            this.point = point;
            this.dividerType = dividerType;
            this.parent = parent;
        }

        private void prepareRectHV() {
            if (parent == null) {
                rect = new RectHV(MIN_COORDINATE_VALUE, MIN_COORDINATE_VALUE, point.x(), MAX_COORDINATE_VALUE);
                return;
            } else {
                if (dividerType == HORIZONTAL_DIVIDER_TYPE) {
                    if (isPointLess(point, parent.point, !dividerType))
                        rect = new RectHV(parent.rect.xmin(), parent.rect.ymin(), parent.rect.xmax(), point.y());
                    else if (parent.parent == null)
                        rect = new RectHV(parent.rect.xmax(), parent.rect.ymin(), MAX_COORDINATE_VALUE, point.y());
                    else
                        rect = new RectHV(parent.rect.xmax(), parent.rect.ymin(), parent.parent.rect.xmax(), point.y());
                } else {
                    if (isPointLess(point, parent.point, !dividerType))
                        rect = new RectHV(parent.rect.xmin(), parent.rect.ymin(), point.x(), parent.point.y());
                    else
                        rect = new RectHV(parent.rect.xmin(), parent.rect.ymax(), point.x(), parent.parent.rect.ymax());
                }
            }

        }

        public void draw() {
            StdDraw.setPenRadius();

            if (dividerType == VERTICAL_DIVIDER_TYPE) {
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.line(rect.xmax(), rect.ymin(), rect.xmax(), rect.ymax());
            }
            else {
                StdDraw.setPenColor(StdDraw.BLUE);
                StdDraw.line(rect.xmin(), rect.ymax(), rect.xmax(), rect.ymax());
            }


            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(0.01);
            StdDraw.point(point.x(), point.y());
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
            tree.insert(new Point2D(0.9, 0));
            tree.insert(new Point2D(0.8, 0.005));

            Point2D queryPoint = new Point2D(0.9, 0.001);

            System.out.println("tree.contains(p1)? " + tree.contains(p1));
            System.out.println("tree.contains(p2)? " + tree.contains(p2));
            System.out.println("tree.contains(p3)? " + tree.contains(p3));
            System.out.println("tree.contains(p4)? " + tree.contains(p4));
            System.out.println("tree.contains(p5)? " + tree.contains(p5));
            System.out.println("tree.contains(fake)? " + tree.contains(fake));

            System.out.println("The nearest point to " + queryPoint + " is point " + tree.nearest(queryPoint));

            tree.draw();

        }
}