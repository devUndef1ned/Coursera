import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by oleg on 09.02.17.
 */
public class BruteCollinearPoints {

    private List<LineSegment> lineSegments;
    private Point[] mPoints;

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        if (points == null)
            throw new NullPointerException();

        mPoints = points.clone();

        Arrays.sort(mPoints);

        if (hasDuplicate(mPoints))
            throw new IllegalArgumentException();

        lineSegments = new ArrayList<LineSegment>();

        for (int i = 0; i < mPoints.length - 3; i++) {
         Point first = mPoints[i];
         for (int j = i + 1; j < mPoints.length - 2; j++) {
             Point second = mPoints[j];

             for (int k = j + 1; k < mPoints.length - 1; k++) {
                 Point third = mPoints[k];

                 for (int l = k + 1; l < mPoints.length; l++) {
                     Point fourth = mPoints[l];
                     checkForCollinearity(first, second, third, fourth);
                 }
             }
         }
        }
    }
    // the number of line segments
    public int numberOfSegments() {
        return lineSegments.size();
    }
    // the line segments
    public LineSegment[] segments() {
        LineSegment[] result = new LineSegment[lineSegments.size()];
        return lineSegments.toArray(result);
    }

    private boolean hasDuplicate(Point[] points) {
        for (int i = 0; i < points.length - 1; i++) {
            if (points[i].compareTo(points[i + 1]) == 0) {
                return true;
            }
        }
        return false;

    }

    private void checkForCollinearity(Point first, Point second, Point third, Point fourth) {
        if (first == null || second == null || third == null || fourth == null)
            throw new NullPointerException();

        boolean collinear = false;

        double slope12, slope13, slope14;
        slope12 = first.slopeTo(second);
        slope13 = first.slopeTo(third);
        slope14 = first.slopeTo(fourth);

        collinear = (slope12 == slope13 && slope13 == slope14);

        if (!collinear)
            return;

        Point min = first, max = first;
        Point[] points = new Point[3];
        points[0] = second;
        points[1] = third;
        points[2] = fourth;

        for (int i = 0; i < points.length; i++) {
            int minComparation = min.compareTo(points[i]);
            int maxComparation = max.compareTo(points[i]);
            if (minComparation == 1)
                min = points[i];
            if (maxComparation == -1)
                max = points[i];
        }

        LineSegment lineSegment = new LineSegment(min, max);
        lineSegments.add(lineSegment);
    }
}
