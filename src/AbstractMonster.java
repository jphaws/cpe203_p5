import processing.core.PImage;

import java.util.List;

public abstract class AbstractMonster extends AbstractMoveableEntity {

    private int resourceLimit;
    private int resourceCount;

    public AbstractMonster(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod)
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
