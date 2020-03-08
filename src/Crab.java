import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class Crab extends AbstractMoveableEntity{


    private static final String QUAKE_KEY = "quake";

    public Crab(String id, Point position,
                  List<PImage> images, int actionPeriod, int animationPeriod)
    {
        super(id, position, images, actionPeriod, animationPeriod);
    }

    public static Crab createCrab(String id, Point position,
                                     int actionPeriod, int animationPeriod, List<PImage> images)
    {
        return new Crab(id, position, images, actionPeriod, animationPeriod);
    }

    public void executeActivity(WorldModel world,
                                    ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<AbstractEntity> crabTarget = world.findNearest(this.getPosition(), SGrass.class);
        long nextPeriod = this.getActionPeriod();

        if (crabTarget.isPresent())
        {
            Point tgtPos = crabTarget.get().getPosition();

            if (moveTo(this, world, crabTarget.get(), scheduler))
            {
                Quake quake = Quake.createQuake(tgtPos,
                        imageStore.getImageList(QUAKE_KEY));

                world.addEntity(quake);
                nextPeriod += this.getActionPeriod();
                quake.scheduleActions(scheduler, world, imageStore);
            }
        }

        scheduler.scheduleEvent(this,
                Activity.createActivityAction(this, world, imageStore),
                nextPeriod);
    }

    public boolean moveTo(AbstractMoveableEntity crab, WorldModel world,
                          AbstractEntity target, EventScheduler scheduler) {
        if (adjacent(crab.getPosition(), target.getPosition())) {
            world.removeEntity(target);
            scheduler.unscheduleAllEvents(target);
            return true;
        } else {
            Point nextPos = crab.nextPosition(world, target.getPosition());

            if (!crab.getPosition().equals(nextPos)) {
                Optional<AbstractEntity> occupant = world.getOccupant(nextPos);
                if (occupant.isPresent()) {
                    scheduler.unscheduleAllEvents(occupant.get());
                }

                world.moveEntity(crab, nextPos);
            }
            return false;
        }
    }
}
