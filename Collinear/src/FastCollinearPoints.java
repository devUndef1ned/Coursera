import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by oleg on 12.02.17.
 */
public class FastCollinearPoints {

    private Point[] mPoints;
    private HashMap<Double, LineSegment> lineSegmentMap;

    // finds all line segments containing 4 or more mPoints
    public FastCollinearPoints(Point[] points) {
        if (points == null)
            throw new NullPointerException();

    this.mPoints = points.clone();

    Arrays.sort(mPoints);

    lineSegmentMap = new HashMap<Double, LineSegment>();

    for (int i = 0; i < mPoints.length; i++) {
        checkForCollinearity(i);
    }

    }
    // the number of line segments
    public int numberOfSegments() {
        return lineSegmentMap.size();
    }
    // the line segments
    public LineSegment[] segments() {
        LineSegment[] lineSegments = new LineSegment[lineSegmentMap.size()];
        return lineSegmentMap.values().toArray(lineSegments);

    }
    private void checkForCollinearity(int index) {

        Point origin = mPoints[index];
        Point second = null, third = null, fourth = null;

        if (origin == null)
            throw new NullPointerException();

        Point[] newPoints = new Point[mPoints.length - 1];

        for (int i = 0; i < newPoints.length; i++) {

            if (i != index && origin.compareTo(mPoints[i]) == 0)
                throw new IllegalArgumentException();

            if (mPoints[i] == null)
                throw new NullPointerException();

            if (i < index)
                newPoints[i] = mPoints[i];
            else if (i >= index)
                newPoints[i] = mPoints[i + 1];
        }

        Arrays.sort(newPoints, origin.slopeOrder());

        List<Point> slopePoints = new ArrayList<>();
        double slope;
        double lastSlope = Double.NEGATIVE_INFINITY;

        for (int i = 0; i < newPoints.length; i++) {
            Point comparePoint = newPoints[i];
            slope = origin.slopeTo(comparePoint);

            if (slope == lastSlope) {
                slopePoints.add(comparePoint);
            } else {
                if (slopePoints.size() >= 3) {
                    slopePoints.add(origin);
                    createNewLineSegment(slopePoints);
                }
                slopePoints.clear();
                slopePoints.add(comparePoint);
            }
            lastSlope = slope;
        }

        if (slopePoints.size() >= 3) {
            slopePoints.add(origin);
            createNewLineSegment(slopePoints);
        }
    }
    private void createNewLineSegment(List<Point> list) {
        Point min, max;

        Point[] thisPoints = new Point[list.size()];
        list.toArray(thisPoints);

        Arrays.sort(thisPoints);

        min = thisPoints[0];
        max = thisPoints[thisPoints.length - 1];

        LineSegment segment = new LineSegment(min, max);
        double slope = min.slopeTo(max);

        if (!lineSegmentMap.containsKey(slope))
            lineSegmentMap.put(slope, segment);
    }

    public static void main(String[] args) {

        // read the n mPoints from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the mPoints
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
