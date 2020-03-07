import processing.core.PImage;

import java.util.List;
import java.util.Random;

public abstract class AbstractActiveEntity extends AbstractEntity{

    private int actionPeriod;
    private static final Random rand = new Random();

    public AbstractActiveEntity(String id, Point position,
                                List<PImage> images, int actionPeriod) {
        super(id, position, images);
        this.actionPeriod = actionPeriod;
    }

    protected abstract void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler);

    protected void scheduleActions(EventScheduler scheduler,
                                WorldModel world, ImageStore imageStore)
    {
        scheduler.scheduleEvent( this,
                Activity.createActivityAction(this, world, imageStore),
                actionPeriod);
    }

    protected int getActionPeriod()
    {
        return actionPeriod;
    }
    protected Random getRand(){
        return rand;
    }


}
