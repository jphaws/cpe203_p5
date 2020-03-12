import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class GhostNotFull extends AbstractMonster {

    private Point respawn;
    public GhostNotFull(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod,
                     Point respawn)
    {
        super(id, position, images, actionPeriod, animationPeriod);
        this.respawn = respawn;

    }

    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<AbstractEntity> notFullTarget = world.findNearest(this.getPosition(),
                Gold.class);

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
            AbstractMonster g = GhostFull.createGhostFull(this.getId(), this.getResourceLimit(),
                    this.getPosition(), this.getActionPeriod(), this.getAnimationPeriod(),
                    this.getImages(), this.respawn);

            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(g);
            g.scheduleActions(scheduler, world, imageStore);

            return true;
        }

        return false;
    }

    protected boolean moveTo(AbstractMoveableEntity Entity, WorldModel world,
                             AbstractEntity target, EventScheduler scheduler)
    {
        if (adjacent(Entity.getPosition(), target.getPosition()))
        {
            ((AbstractMonster) Entity).setResourceCount(((AbstractMonster) Entity).getResourceCount() + 1);
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

    public Point getRespawn() {
        return respawn;
    }
}
