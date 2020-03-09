import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class OctoNotFull extends AbstractOcto{


    public OctoNotFull(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod,
                       int resourceLimit, int resourceCount)
    {
        super(id, position, images, actionPeriod, animationPeriod, resourceLimit, resourceCount);

    }

    public static OctoNotFull createOctoNotFull(String id, int resourceLimit,
                                           Point position, int actionPeriod, int animationPeriod,
                                           List<PImage> images)
    {
        return new OctoNotFull(id, position, images, actionPeriod, animationPeriod,
                                resourceLimit, 0);
    }

    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<AbstractEntity> notFullTarget = world.findNearest(this.getPosition(),
                Fish.class);

        if (!notFullTarget.isPresent() ||
                !moveTo(this, world, notFullTarget.get(), scheduler) ||
                !transformNotFull(world, scheduler, imageStore))
        {
            scheduler.scheduleEvent(this,
                    Activity.createActivityAction(this, world, imageStore),
                    this.getActionPeriod());
        }
    }

    private boolean transformNotFull(WorldModel world,
                                     EventScheduler scheduler, ImageStore imageStore)
    {
        if (this.getResourceCount() >= this.getResourceLimit())
        {
            AbstractOcto octo = OctoFull.createOctoFull(this.getId(), this.getResourceLimit(),
                    this.getPosition(), this.getActionPeriod(), this.getAnimationPeriod(),
                    this.getImages());

            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(octo);
            octo.scheduleActions(scheduler, world, imageStore);

            return true;
        }

        return false;
    }

    protected boolean moveTo(AbstractMoveableEntity Entity, WorldModel world,
                             AbstractEntity target, EventScheduler scheduler)
    {
        if (adjacent(Entity.getPosition(), target.getPosition()))
        {
            ((AbstractOcto) Entity).setResourceCount(((AbstractOcto) Entity).getResourceCount() + 1);
            world.removeEntity(target);
            scheduler.unscheduleAllEvents(target);

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
