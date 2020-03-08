import processing.core.PImage;

import java.util.List;
import java.util.Random;
public class Fish extends AbstractActiveEntity {


    private static final String CRAB_KEY = "crab";
    private static final String CRAB_ID_SUFFIX = " -- crab";
    private static final int CRAB_PERIOD_SCALE = 4;
    private static final int CRAB_ANIMATION_MIN = 50;
    private static final int CRAB_ANIMATION_MAX = 150;


    public Fish(String id, Point position, List<PImage> images, int actionPeriod)
    {
        super(id, position, images, actionPeriod);

    }

    public static Fish createFish(String id, Point position, int actionPeriod,
                                    List<PImage> images)
    {
        return new Fish(id, position, images, actionPeriod);
    }

    public void executeActivity(WorldModel world,
                                    ImageStore imageStore, EventScheduler scheduler)
    {
        Point pos = this.getPosition();  // store current position before removing

        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        Crab crab = Crab.createCrab(this.getId() + CRAB_ID_SUFFIX,
                pos, this.getActionPeriod() / CRAB_PERIOD_SCALE,
                CRAB_ANIMATION_MIN +
                        this.getRand().nextInt(CRAB_ANIMATION_MAX - CRAB_ANIMATION_MIN),
                imageStore.getImageList(CRAB_KEY));

        world.addEntity(crab);
        crab.scheduleActions(scheduler, world, imageStore);
    }

}
