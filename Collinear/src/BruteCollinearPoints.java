import java.util.Arrays;

/**
 * Created by oleg on 09.02.17.
 */
public class BruteCollinearPoints {


    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        if (points == null)
            throw new NullPointerException();

        for (Point p: points){
            if (p == null)
                throw new NullPointerException();
        }

        Arrays.sort(points);


    }
    // the number of line segments
    public int numberOfSegments() {

    }
    // the line segments
    public LineSegment[] segments() {

    }
}
