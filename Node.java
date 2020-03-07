import java.util.Objects;

public class Node {
    private Point point;
    private int g;  // Distance from start
    private int h;  // Distance to target
    private int f;  // Total distance
    private Node parent;

    public Node(Point point, int h, Node parent){
        this.point = point;
        this.h = h;
        this.parent = parent;
        g = parent.g + 1;
        f = g + h;
    }
    public Node(Point point, int h){
        this.point = point;
        this.h = h;
        parent = null;
        g = 0;
        f = g + h;
    }

    public int hashCode(){
        return Objects.hash(point, f, h, g, parent);
    }

    public boolean equals(Object other){
        if(other == null){
            return false;
        }
        if(other.getClass() != this.getClass()){
            return false;
        }
        Node n = (Node)other;
        //Do I want this to check everything or just the (x,y) pair?
        return ((n.point == point) && (n.f == f) && (n.h == h) && (n.parent == parent));
    }

    public Point getPoint() {
        return point;
    }

    public int getG() {
        return g;
    }

    public int getH() {
        return h;
    }

    public int getF() {
        return f;
    }

    public Node getParent() {
        return parent;
    }
}
