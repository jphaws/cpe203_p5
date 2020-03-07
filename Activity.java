public class Activity implements Action {

    private AbstractActiveEntity entity;
    private WorldModel world;
    private ImageStore imageStore;
    private int repeatCount;

    public Activity(AbstractActiveEntity entity, WorldModel world,
                    ImageStore imageStore, int repeatCount)
    {
        this.entity = entity;
        this.world = world;
        this.imageStore = imageStore;
        this.repeatCount = repeatCount;
    }

    public static Action createActivityAction(AbstractActiveEntity entity, WorldModel world,
                                       ImageStore imageStore)
    {
        return new Activity(entity, world, imageStore, 0);
    }


    public void executeAction(EventScheduler scheduler)
    {
        entity.executeActivity(world, imageStore, scheduler);
    }
}
