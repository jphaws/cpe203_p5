import processing.core.PImage;

import java.util.List;

public class Obstacle extends AbstractEntity {


    public Obstacle(String id, Point position,
                  List<PImage> images)
    {
        super(id, position, images);
    }

    public static Obstacle createObstacle(String id, Point position,
                                        List<PImage> images)
    {
        return new Obstacle(id, position, images);
    }



}
