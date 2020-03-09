import processing.core.PImage;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.IOException;
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


    public void useWeapon(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Point p = new Point(this.getPosition().x+1, this.getPosition().y);
        Ability f = new Ability("ability", p, imageStore.getImageList("ability"),  2, 5);
        world.addEntity(f);
        f.scheduleActions(scheduler, world, imageStore);
    }

}
