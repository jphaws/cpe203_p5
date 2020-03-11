import processing.core.PImage;

import java.util.List;

public class Portal extends AbstractAnimatedEntity{

    private static final int PORTAL_ANIMATION_REPEAT_COUNT = 7;


    public Portal(String id, Point position,
                  List<PImage> images, int actionPeriod, int animationPeriod)
    {
        super(id, position, images, actionPeriod, animationPeriod);
    }

/*    public static Portal createAtlantis(String id, Point position,
                                        List<PImage> images)
    {
        return new Portal(id, position, images, 0, 0);
    }*/

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
                Animation.createAnimationAction(this, PORTAL_ANIMATION_REPEAT_COUNT),
                this.getAnimationPeriod());
    }


}
