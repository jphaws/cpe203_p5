import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class Ability extends AbstractMoveableEntity{

    public Ability(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod) {
        super(id, position, images, actionPeriod, animationPeriod);
    }

    @Override
    protected boolean moveTo(AbstractMoveableEntity Entity, WorldModel world, AbstractEntity target, EventScheduler scheduler) {
        Point nextPos = new Point(this.getPosition().x+1, this.getPosition().y);

        if (world.getOccupant(nextPos).isPresent()){
            Optional<AbstractEntity> occupant = world.getOccupant(nextPos);
            AbstractEntity e = occupant.get();
            if(e instanceof AbstractOcto){
                scheduler.unscheduleAllEvents(e);
                world.removeEntity(e);
                scheduler.unscheduleAllEvents(this);
                world.removeEntity(this);
                return true;
            }
            else if (e instanceof Obstacle){
                scheduler.unscheduleAllEvents(this);
                world.removeEntity(this);
            }
        } else {

            if (!Entity.getPosition().equals(nextPos)) {
                Optional<AbstractEntity> occupant = world.getOccupant(nextPos);
                if (occupant.isPresent()) {
                    scheduler.unscheduleAllEvents(this);
                }

                world.moveEntity(Entity, nextPos);
            }
            return false;
        }

        return false;
    }

    @Override
    protected void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Optional<AbstractEntity> AxeTarget = world.findNearest(this.getPosition(), AbstractOcto.class);
        long nextPeriod = this.getActionPeriod();

        if (AxeTarget.isPresent())
        {
            Point tgtPos = AxeTarget.get().getPosition();

            if (moveTo(this, world, AxeTarget.get(), scheduler))
            {
                Quake quake = Quake.createQuake(tgtPos,
                        imageStore.getImageList("quake"));

                world.addEntity(quake);
                nextPeriod += this.getActionPeriod();
                quake.scheduleActions(scheduler, world, imageStore);
            }
        }
        scheduler.scheduleEvent(this,
                Activity.createActivityAction(this, world, imageStore),
                nextPeriod);
    }
}
