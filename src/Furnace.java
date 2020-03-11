import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class Furnace extends AbstractActiveEntity {

    private static final String GOLD_KEY = "gold";
    private static final String GOLD_ID_PREFIX = "fish -- ";
    private static final int GOLD_CORRUPT_MIN = 20000;
    private static final int GOLD_CORRUPT_MAX = 30000;

    public Furnace(String id, Point position,
                   List<PImage> images, int actionPeriod)
    {
        super(id, position, images, actionPeriod);
    }

    public static Furnace createFurnace(String id, Point position, int actionPeriod,
                                        List<PImage> images)
    {
        return new Furnace(id, position, images, actionPeriod);
    }

    public void executeActivity(WorldModel world,
                                      ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Point> openPt = world.findOpenAround(this.getPosition());

        if (openPt.isPresent())
        {
            Gold gold = new Gold(GOLD_ID_PREFIX + this.getId(),
                    openPt.get(), imageStore.getImageList(GOLD_KEY),GOLD_CORRUPT_MIN +
                            this.getRand().nextInt(GOLD_CORRUPT_MAX - GOLD_CORRUPT_MIN));
            world.addEntity(gold);
            gold.scheduleActions(scheduler, world, imageStore);
        }

        scheduler.scheduleEvent(this,
                Activity.createActivityAction(this, world, imageStore),
                this.getActionPeriod());
    }


}
