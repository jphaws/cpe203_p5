import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class Furnace extends AbstractActiveEntity {

    private static final String GOLD_ID_PREFIX = "gold";

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
            Gold gold = (Gold)FactoryEntity.create(GOLD_ID_PREFIX, imageStore, openPt.get()); //20000+this.getRand().nextInt(30000 - 20000);
            world.addEntity(gold);
            gold.scheduleActions(scheduler, world, imageStore);
        }

        scheduler.scheduleEvent(this,
                Activity.createActivityAction(this, world, imageStore),
                this.getActionPeriod());
    }


}
