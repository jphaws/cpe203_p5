import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class AStarPathingStrategy
        implements PathingStrategy
{
    public List<Point> computePath(Point start, Point end,
                                   Predicate<Point> canPassThrough,
                                   BiPredicate<Point, Point> withinReach,
                                   Function<Point, Stream<Point>> potentialNeighbors)
    {
        LinkedList<Point> path = new LinkedList<>();
        HashMap<Point, Node> closedMap = new HashMap<>();
        HashMap<Point, Node> openMap = new HashMap<>();
        PriorityQueue<Node> openQueue = new PriorityQueue<>(Comparator.comparingInt(Node::getF));
        Node currentNode = new Node(start, (Math.abs(start.x - end.x) + Math.abs(start.y - end.y)));
        while(!withinReach.test(currentNode.getPoint(), end)) {
            Node finalCurrentNode = currentNode;
            List<Node> neighbors = potentialNeighbors.apply(currentNode.getPoint())
                    .filter(canPassThrough)
                    .filter(pt -> !pt.equals(start) && !pt.equals(end))
                    .map(pt -> new Node(pt, (Math.abs(pt.x - end.x) + Math.abs(pt.y - end.y) ), finalCurrentNode))
                    .collect(Collectors.toList());
            for (Node n : neighbors){
                if(!closedMap.containsKey(n.getPoint()) && !openMap.containsKey(n.getPoint())) {
                    openMap.put(n.getPoint(), n);
                    openQueue.add(n);
                }
                else if(openMap.containsKey(n.getPoint())){
                    if(n.getF() < (openMap.get(n.getPoint())).getF()){
                        openQueue.remove(openMap.get(n.getPoint()));
                        openMap.replace(n.getPoint(), n);
                        openQueue.add(n);
                    }
                }
            }
            closedMap.put(finalCurrentNode.getPoint(), finalCurrentNode);
            if(openQueue.isEmpty())
                return path;
            currentNode = openQueue.remove();
        }
        while(currentNode.getParent() != null){
            path.addFirst(currentNode.getPoint());
            currentNode = currentNode.getParent();
        }
        return path;
    }
}
