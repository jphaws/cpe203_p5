import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class GhostFull extends AbstractMonster {

    private Point respawn;
    public GhostFull(String id, Point position,
                     List<PImage> images, int actionPeriod, int animationPeriod,
                     int resourceLimit, int resourceCount, Point respawn)
    {
        super(id, position, images, actionPeriod, animationPeriod);
        this.respawn = respawn;
    }

    public static GhostFull createGhostFull(String id, int resourceLimit,
                                            Point position, int actionPeriod, int animationPeriod,
                                            List<PImage> images, Point respawn)
    {
        return new GhostFull(id, position, images, actionPeriod, animationPeriod,
                resourceLimit, resourceLimit, respawn);
    }

    public void executeActivity(WorldModel world,
                                        ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<AbstractEntity> fullTarget = world.findNearest(this.getPosition(),
                DerpPoly.class);

        if (fullTarget.isPresent() &&
                moveTo(this, world, fullTarget.get(), scheduler))
        {
            //at atlantis trigger animation
            if(fullTarget.get() instanceof DerpPoly)
                ((DerpPoly)(fullTarget.get())).scheduleActions(scheduler, world, imageStore);

            //transform to unfull
            transformFull(world, scheduler, imageStore);
        }
        else
        {
            scheduler.scheduleEvent(this,
                    Activity.createActivityAction(this, world, imageStore),
                    this.getActionPeriod());
        }
    }

    private void transformFull(WorldModel world,
                               EventScheduler scheduler, ImageStore imageStore)
    {
        AbstractMoveableEntity Ghost = (GhostNotFull)FactoryEntity.create("ghost", imageStore, this.getPosition());

        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        world.addEntity(Ghost);
        Ghost.scheduleActions(scheduler, world, imageStore);
    }

    public boolean moveTo(AbstractMoveableEntity Entity, WorldModel world,
                          AbstractEntity target, EventScheduler scheduler)
    {
        if (adjacent(Entity.getPosition(), target.getPosition()))
        {
            return true;
        }
        else
        {
            Point nextPos = Entity.nextPosition(world, target.getPosition());

            if (!Entity.getPosition().equals(nextPos))
            {
                Optional<AbstractEntity> occupant = world.getOccupant(nextPos);
                if (occupant.isPresent())
                {
                    scheduler.unscheduleAllEvents(occupant.get());
                }

                world.moveEntity(Entity, nextPos);
            }
            return false;
        }
    }

    public Point getRespawn() {
        return respawn;
    }
}
