import processing.core.PImage;

import java.util.List;

public class Gold extends AbstractActiveEntity {



    public Gold(String id, Point position, List<PImage> images, int actionPeriod)
    {
        super(id, position, images, actionPeriod);

    }

    public void executeActivity(WorldModel world,
                                    ImageStore imageStore, EventScheduler scheduler)
    {
        Point pos = this.getPosition();  // store current position before removing

        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

/*        Skeleton skeleton = new Skeleton(this.getId() + SKELETON_ID_SUFFIX,
                pos, imageStore.getImageList(SKELETON_KEY), this.getActionPeriod() / SKELETON_PERIOD_SCALE,
                SKELETON_ANIMATION_MIN +
                        this.getRand().nextInt(SKELETON_ANIMATION_MAX - SKELETON_ANIMATION_MIN));

        world.addEntity(skeleton);
        skeleton.scheduleActions(scheduler, world, imageStore);*/
    }

}
