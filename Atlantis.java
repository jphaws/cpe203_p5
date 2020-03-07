import processing.core.PImage;

import java.util.List;

public class Atlantis extends AbstractAnimatedEntity{

    private static final int ATLANTIS_ANIMATION_REPEAT_COUNT = 7;


    public Atlantis(String id, Point position,
                  List<PImage> images, int actionPeriod, int animationPeriod)
    {
        super(id, position, images, actionPeriod, animationPeriod);
    }

    public static Atlantis createAtlantis(String id, Point position,
                                        List<PImage> images)
    {
        return new Atlantis(id, position, images, 0, 0);
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
                Animation.createAnimationAction(this, ATLANTIS_ANIMATION_REPEAT_COUNT),
                this.getAnimationPeriod());
    }


}
