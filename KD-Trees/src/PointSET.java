import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;

import java.util.Arrays;
import java.util.TreeSet;

public class PointSET {

    private final TreeSet<Point2D> mTree;

    // construct an empty set of points
    public PointSET() {
        mTree = new TreeSet<Point2D>();
    }
    // is the set empty?
    public boolean isEmpty() {
        return mTree.isEmpty();
    }
    // number of points in the set
    public int size() {
        return mTree.size();
    }
    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null)
            throw new NullPointerException();

        mTree.add(p);
    }
    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null)
            throw new NullPointerException();

        return mTree.contains(p);

    }
    // draw all points to standard draw
    public void draw() {
        if (isEmpty() || size() == 0)
            return;

        for (Point2D point: mTree)
            point.draw();
    }
    // all points that are inside the rectangle
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null)
            throw new NullPointerException();

        Point2D min = new Point2D(rect.xmin(), rect.ymin());
        Point2D max = new Point2D(rect.xmax(), rect.ymax());

        SET<Point2D> setRange = new SET<Point2D>();

        for (Point2D p: mTree) {
            if (p.x() >= min.x() && p.y() >= min.y() &&
                    p.x() <= max.x() && p.y() <= max.y())
                setRange.add(p);
        }

        return setRange;

    }
    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null)
            throw new NullPointerException();

        if (isEmpty())
            return null;

        Point2D[] arr = new Point2D[mTree.size()];
        mTree.toArray(arr);
        Arrays.sort(arr, p.distanceToOrder());

        return arr[0];
    }

}