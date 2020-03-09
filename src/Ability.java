import processing.core.PImage;

import java.util.List;
import java.util.Optional;
import java.util.Queue;

public class Ability extends AbstractMoveableEntity{

    Player p;
    final int DIRECTION;
    Point target;

    public Ability(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod, Player p) {
        super(id, position, images, actionPeriod, animationPeriod);
        this.p = p;
        this.DIRECTION = p.getDirection();
        if(DIRECTION == 0) {
            target = new Point(this.getPosition().x , this.getPosition().y + 6);
        } else if(DIRECTION == 2) {
            target = new Point(this.getPosition().x - 6, this.getPosition().y);
        }
        else if(DIRECTION  == 3) {
            target = new Point(this.getPosition().x + 6, this.getPosition().y);
        }
        else {
            target = new Point(this.getPosition().x, this.getPosition().y - 6);
        }

    }

    @Override
    protected boolean moveTo(AbstractMoveableEntity Entity, WorldModel world, AbstractEntity target, EventScheduler scheduler) {
        Point nextPos;
        if(DIRECTION == 0) {
            nextPos = new Point(this.getPosition().x , this.getPosition().y + 1);
        } else if(DIRECTION == 2) {
            nextPos = new Point(this.getPosition().x - 1, this.getPosition().y);
        }
        else if(DIRECTION  == 3) {
            nextPos = new Point(this.getPosition().x + 1, this.getPosition().y);
        }
        else {
            nextPos = new Point(this.getPosition().x, this.getPosition().y - 1);
        }

        if (world.getOccupant(nextPos).isPresent()){
            Optional<AbstractEntity> occupant = world.getOccupant(nextPos);
            AbstractEntity e = occupant.get();
            if(e instanceof AbstractOcto || e instanceof Crab){
                scheduler.unscheduleAllEvents(e);
                world.removeEntity(e);
                scheduler.unscheduleAllEvents(this);
                world.removeEntity(this);
                return true;
            }
            else if (e instanceof Obstacle ||e instanceof Atlantis || e instanceof SGrass
            || e instanceof Fish || e instanceof Quake || e instanceof Ability){
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
        Optional<AbstractEntity> AxeTarget = world.findNearest(this.getPosition(), AbstractMoveableEntity.class);
        long nextPeriod = this.getActionPeriod();


            Point tgtPos = AxeTarget.get().getPosition();


            if (moveTo(this, world, AxeTarget.get(), scheduler))
            {
                Quake quake = Quake.createQuake(tgtPos,
                        imageStore.getImageList("quake"));

                world.addEntity(quake);
                nextPeriod += this.getActionPeriod();
                quake.scheduleActions(scheduler, world, imageStore);
            }

            if(this.getPosition() == tgtPos) {
                world.removeEntity(this);
            }

        scheduler.scheduleEvent(this,
                Activity.createActivityAction(this, world, imageStore),
                nextPeriod);
    }
}
