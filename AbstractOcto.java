import processing.core.PImage;

import java.util.List;

public abstract class AbstractOcto extends AbstractMoveableEntity {

    private int resourceLimit;
    private int resourceCount;  //Only used by the octos

    public AbstractOcto(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod,
                        int resourceLimit, int resourceCount)
    {
        super(id, position, images, actionPeriod, animationPeriod);
        this.resourceLimit = resourceLimit;
        this.resourceCount = resourceCount;
    }

    protected int getResourceLimit(){
        return resourceLimit;
    }

    protected int getResourceCount(){
        return resourceCount;
    }

    protected void setResourceCount(int resourceCount){
        this.resourceCount = resourceCount;
    }

}
