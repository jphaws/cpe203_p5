import processing.core.PImage;

import java.util.List;
import java.util.Random;

public class Player extends AbstractAnimatedEntity{
    private int imageIndex;

    public Player(String id, Point position,int actionPeriod, int animationPeriod, List<PImage> images) {
        super(id, position, images, actionPeriod, animationPeriod);
    }
    public void move(int dx, int dy, WorldModel world){
        Point newP = new Point(getPosition().x + dx, getPosition().y + dy);
        if(!world.isOccupied(newP) && world.withinBounds(newP)){
            setPosition(newP);
        }
    }

    @Override
    protected void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {

    }
}
