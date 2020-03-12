import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class Ability extends AbstractMoveableEntity {

    Player p;
    final int DIRECTION;
    Point target;
    boolean GHOST_FULL = false;
    Skeleton skeleton;
    GhostNotFull ghost;
    boolean isSKEL = false;
    boolean isGHOST = false;

    public Ability(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod, Player p) {
        super(id, position, images, actionPeriod, animationPeriod);
        this.p = p;
        this.DIRECTION = p.getDirection();
        if (DIRECTION == 0) {
            target = new Point(this.getPosition().x, this.getPosition().y + 6);
        } else if (DIRECTION == 2) {
            target = new Point(this.getPosition().x - 6, this.getPosition().y);
        } else if (DIRECTION == 3) {
            target = new Point(this.getPosition().x + 6, this.getPosition().y);
        } else {
            target = new Point(this.getPosition().x, this.getPosition().y - 6);
        }

    }

    @Override
    protected boolean moveTo(AbstractMoveableEntity Entity, WorldModel world, AbstractEntity target, EventScheduler scheduler) {
        PathingStrategy straight = new AxeThrowingPath();
        List<Point> ps = straight.computePath(this.getPosition(), new Point(DIRECTION,0),null,null,null);
        Point nextPos = ps.get(0);

        if (world.getOccupant(nextPos).isPresent()) {
            Optional<AbstractEntity> occupant = world.getOccupant(nextPos);
            AbstractEntity e = occupant.get();
            if (e instanceof GhostFull) {
                scheduler.unscheduleAllEvents(e);
                world.removeEntity(e);
                scheduler.unscheduleAllEvents(this);
                world.removeEntity(this);
                GhostNotFull g = new GhostNotFull(e.getId(), ((GhostFull) e).getRespawn(), e.getImages()
                ,700,100,1,0,((GhostFull) e).getRespawn());
                ghost = g;
                isGHOST = true;
                GHOST_FULL = true;

            } else if (e instanceof Skeleton) {
                scheduler.unscheduleAllEvents(e);
                world.removeEntity(e);
                scheduler.unscheduleAllEvents(this);
                world.removeEntity(this);
                Skeleton s = new Skeleton(e.getId(), ((Skeleton) e).getSpawn(), e.getImages(),
                        700, 100, ((Skeleton) e).getSpawn());
                skeleton = s;
                isSKEL = true;
                return true;
            }
            else if(e instanceof GhostNotFull) {
                scheduler.unscheduleAllEvents(e);
                world.removeEntity(e);
                scheduler.unscheduleAllEvents(this);
                world.removeEntity(this);
                GhostNotFull g = new GhostNotFull(e.getId(), ((GhostNotFull) e).getRespawn(), e.getImages()
                        ,700,100,1,0,((GhostNotFull) e).getRespawn());
                ghost = g;
                isGHOST = true;
                return true;
            } else if (e instanceof Obstacle || e instanceof Portal || e instanceof Furnace
                    || e instanceof Gold || e instanceof Quake || e instanceof Ability) {
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
        Optional<AbstractEntity> AxeTarget = world.findNearest(this.getPosition(), AbstractEntity.class);
        long nextPeriod = this.getActionPeriod();


        Point tgtPos = AxeTarget.get().getPosition();

        Quake quake = Quake.createQuake(tgtPos, imageStore.getImageList("quake"));

        if (moveTo(this, world, AxeTarget.get(), scheduler)) {

            world.addEntity(quake);
            nextPeriod += this.getActionPeriod();
            quake.scheduleActions(scheduler, world, imageStore);
        }

        if(isSKEL) {
            world.addEntity(skeleton);
            skeleton.scheduleActions(scheduler, world, imageStore);
            isSKEL = false;
        }

        if(isGHOST) {
            world.addEntity(ghost);
            ghost.scheduleActions(scheduler, world, imageStore);
            isGHOST = false;
        }
        if (GHOST_FULL) {
            Gold g = new Gold("gold", tgtPos, imageStore.getImageList("gold"), 10000000);
            world.addEntity(g);
            g.scheduleActions(scheduler, world, imageStore);
            GHOST_FULL = false;
        }

        if (this.getPosition() == tgtPos) {
            world.removeEntity(this);
        }

        scheduler.scheduleEvent(this,
                Activity.createActivityAction(this, world, imageStore),
                nextPeriod);

    }
}
