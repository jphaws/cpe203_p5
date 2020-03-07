import processing.core.PImage;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class SGrass extends AbstractActiveEntity {

    private static final String FISH_KEY = "fish";
    private static final String FISH_ID_PREFIX = "fish -- ";
    private static final int FISH_CORRUPT_MIN = 20000;
    private static final int FISH_CORRUPT_MAX = 30000;

    public SGrass(String id, Point position,
                  List<PImage> images, int actionPeriod)
    {
        super(id, position, images, actionPeriod);
    }

    public static SGrass createSgrass(String id, Point position, int actionPeriod,
                                      List<PImage> images)
    {
        return new SGrass(id, position, images, actionPeriod);
    }

    public void executeActivity(WorldModel world,
                                      ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Point> openPt = world.findOpenAround(this.getPosition());

        if (openPt.isPresent())
        {
            Fish fish = Fish.createFish(FISH_ID_PREFIX + this.getId(),
                    openPt.get(), FISH_CORRUPT_MIN +
                            this.getRand().nextInt(FISH_CORRUPT_MAX - FISH_CORRUPT_MIN),
                    imageStore.getImageList(FISH_KEY));
            world.addEntity(fish);
            fish.scheduleActions(scheduler, world, imageStore);
        }

        scheduler.scheduleEvent(this,
                Activity.createActivityAction(this, world, imageStore),
                this.getActionPeriod());
    }


}
