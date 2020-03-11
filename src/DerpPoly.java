import processing.core.PImage;

import java.util.List;

public class DerpPoly extends AbstractAnimatedEntity {
    private static final int DERP_ANIMATION_REPEAT_COUNT = 0;


    public DerpPoly(String id, Point position,
                  List<PImage> images, int actionPeriod, int animationPeriod)
    {
        super(id, position, images, actionPeriod, animationPeriod);
    }

    public void executeActivity(WorldModel world,
                                ImageStore imageStore, EventScheduler scheduler)
    {
        scheduler.unscheduleAllEvents(this);
        world.removeEntity(this);
    }

    public void scheduleActions(EventScheduler scheduler,
                                WorldModel world, ImageStore imageStore)
    {

        scheduler.scheduleEvent(this,
                Animation.createAnimationAction(this, DERP_ANIMATION_REPEAT_COUNT),
                this.getAnimationPeriod());
    }
}
