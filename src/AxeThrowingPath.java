import java.util.LinkedList;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class AxeThrowingPath implements PathingStrategy {

    @Override
    public List<Point> computePath(Point start, Point end, Predicate<Point> canPassThrough,
                                   BiPredicate<Point, Point> withinReach, Function<Point,
            Stream<Point>> potentialNeighbors) {
        Point nextPos;
        if (end.x == 0) {
            nextPos = new Point(start.x, start.y + 1);
        } else if (end.x == 2) {
            nextPos = new Point(start.x - 1, start.y);
        } else if (end.x == 3) {
            nextPos = new Point(start.x + 1, start.y);
        } else {
            nextPos = new Point(start.x, start.y - 1);
        }
        List<Point> p = new LinkedList<>();
        p.add(nextPos);
        return p;
    }
}
