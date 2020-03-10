import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class Skeleton extends AbstractMonsterFactory{


    private static final String QUAKE_KEY = "quake";

    public Skeleton(String id, Point position,
                    List<PImage> images, int actionPeriod, int animationPeriod)
    {
        super(id, position, images, actionPeriod, animationPeriod);
    }

    public void executeActivity(WorldModel world,
                                    ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<AbstractEntity> skeletonTarget = world.findNearest(this.getPosition(), Player.class);
        long nextPeriod = this.getActionPeriod();

        if (skeletonTarget.isPresent())
        {
            Point tgtPos = skeletonTarget.get().getPosition();

            if (moveTo(this, world, skeletonTarget.get(), scheduler))
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

    public boolean moveTo(AbstractMoveableEntity Entity, WorldModel world,
                          AbstractEntity target, EventScheduler scheduler) {
        if (adjacent(Entity.getPosition(), target.getPosition())) {
            world.removeEntity(target);
            scheduler.unscheduleAllEvents(target);
            return true;
        } else {
            Point nextPos = Entity.nextPosition(world, target.getPosition());

            if (!Entity.getPosition().equals(nextPos)) {
                Optional<AbstractEntity> occupant = world.getOccupant(nextPos);
                if (occupant.isPresent()) {
                    scheduler.unscheduleAllEvents(occupant.get());
                }

                world.moveEntity(Entity, nextPos);
            }
            return false;
        }
    }
}