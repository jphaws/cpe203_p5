import processing.core.PImage;

import javax.swing.*;
import java.awt.image.PixelInterleavedSampleModel;
import java.util.LinkedList;
import java.util.List;

public class Player extends AbstractAnimatedEntity{
    private int direction = 0;
    /*  0 = down
        1 = up
        2 = left
        3 = right
     */
    private int goldCount = 0;
    private List<PImage> fullList = super.getImages();

    public Player(String id, Point position,int actionPeriod, int animationPeriod, List<PImage> images, int goldCount) {
        super(id, position, images, actionPeriod, animationPeriod);
        this.goldCount = goldCount;
    }

    @Override
    protected void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {

    }

    public void scheduleActions(EventScheduler scheduler,
                                WorldModel world, ImageStore imageStore)
    {
        scheduler.scheduleEvent(this,
                Animation.createAnimationAction(this, 1),
                this.getAnimationPeriod());
    }
    public void useWeapon(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Point p;
        if(direction == 0) {
            p = new Point(this.getPosition().x, this.getPosition().y + 1);
        } else if(direction == 1) {
            p = new Point(this.getPosition().x, this.getPosition().y - 1);
        } else if(direction == 2) {
            p = new Point(this.getPosition().x - 1, this.getPosition().y);
        } else
            p = new Point(this.getPosition().x + 1, this.getPosition().y);

        Ability f = new Ability("ability", p, imageStore.getImageList("ability"),  2, 5, this);
        world.addEntity(f);
        f.scheduleActions(scheduler, world, imageStore);
    }

    public void turn(int n){
        direction = n;
        setImages(Images(n));
    }
    public int getDirection(){
        return direction;
    }

    public List<PImage> Images(int n){
            List<PImage> move = new LinkedList<PImage>();
            move.add(fullList.get(n));
            return move;
    }

}
