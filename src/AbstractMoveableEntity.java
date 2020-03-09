import processing.core.PImage;

import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public abstract class AbstractMoveableEntity extends AbstractAnimatedEntity{



    public AbstractMoveableEntity(String id, Point position, List<PImage> images,
                       int actionPeriod, int animationPeriod)
    {
        super(id, position, images, actionPeriod, animationPeriod);
    }
    
    protected abstract boolean moveTo(AbstractMoveableEntity Entity, WorldModel world,
                                AbstractEntity target, EventScheduler scheduler);
    
    

    protected void scheduleActions(EventScheduler scheduler,
                                WorldModel world, ImageStore imageStore)
    {
        scheduler.scheduleEvent(this,
                Activity.createActivityAction(this, world, imageStore),
                getActionPeriod());
        scheduler.scheduleEvent(this, Animation.createAnimationAction(this, 0),
                getActionPeriod());
    }

    protected static boolean adjacent(Point p1, Point p2)
    {
        return (p1.x == p2.x && Math.abs(p1.y - p2.y) == 1) ||
                (p1.y == p2.y && Math.abs(p1.x - p2.x) == 1);
    }

    public Point nextPosition(WorldModel world, Point target) {
        PathingStrategy aStar = new AStarPathingStrategy();
        List<Point> pointsList = aStar.computePath(getPosition(), target, canPassThrough(world), withinReach(), PathingStrategy.CARDINAL_NEIGHBORS);
        if (pointsList.size() == 0) {
            return getPosition();
        }
        return pointsList.get(0);
    }

    private static Predicate<Point> canPassThrough(WorldModel world) {
        return p -> (world.withinBounds(p) && !world.isOccupied(p));
    }

    private static BiPredicate<Point, Point> withinReach() {
        return AbstractMoveableEntity::adjacent;
    }


}
