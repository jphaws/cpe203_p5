import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class OctoFull extends AbstractOcto{

    public OctoFull(String id, Point position,
                  List<PImage> images, int actionPeriod, int animationPeriod, int resourceLimit, int resourceCount)
    {
        super(id, position, images, actionPeriod, animationPeriod, resourceLimit, resourceCount);
    }

    public static OctoFull createOctoFull(String id, int resourceLimit,
                                         Point position, int actionPeriod, int animationPeriod,
                                         List<PImage> images)
    {
        return new OctoFull(id, position, images, actionPeriod, animationPeriod,
                resourceLimit, resourceLimit);
    }

    public void executeActivity(WorldModel world,
                                        ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<AbstractEntity> fullTarget = world.findNearest(this.getPosition(),
                Atlantis.class);

        if (fullTarget.isPresent() &&
                moveTo(this, world, fullTarget.get(), scheduler))
        {
            //at atlantis trigger animation
            if(fullTarget.get() instanceof Atlantis)
                ((Atlantis)(fullTarget.get())).scheduleActions(scheduler, world, imageStore);

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
        AbstractMoveableEntity octo = OctoNotFull.createOctoNotFull(this.getId(), this.getResourceLimit(),
                this.getPosition(), this.getActionPeriod(), this.getAnimationPeriod(),
                this.getImages());

        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        world.addEntity(octo);
        octo.scheduleActions(scheduler, world, imageStore);
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

}
