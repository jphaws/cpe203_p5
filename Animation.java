public class Animation implements Action {

    private AbstractAnimatedEntity entity;
    private WorldModel world;
    private ImageStore imageStore;
    private int repeatCount;

    public Animation(AbstractAnimatedEntity entity, WorldModel world,
                  ImageStore imageStore, int repeatCount)
    {
        this.entity = entity;
        this.world = world;
        this.imageStore = imageStore;
        this.repeatCount = repeatCount;
    }


    public void executeAction(EventScheduler scheduler)
    {
        entity.nextImage();

        if (repeatCount != 1)
        {
            scheduler.scheduleEvent(entity,
                    createAnimationAction(entity,
                            Math.max(repeatCount - 1, 0)),
                    entity.getAnimationPeriod());
        }
    }

    public static Action createAnimationAction(AbstractAnimatedEntity entity, int repeatCount)
    {
        return new Animation(entity, null, null, repeatCount);
    }
}
