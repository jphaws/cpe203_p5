import processing.core.PImage;

import java.util.List;

public abstract class AbstractObstacle extends AbstractEntity {


    public AbstractObstacle(String id, Point position,
                    List<PImage> images)
    {
        super(id, position, images);
    }

}
