import processing.core.PImage;

import java.util.List;

public abstract class AbstractAnimatedEntity extends AbstractActiveEntity{

    private int animationPeriod;

    public AbstractAnimatedEntity(String id, Point position,
                                  List<PImage> images, int actionPeriod, int animationPeriod){
        super(id, position, images, actionPeriod);
        this.animationPeriod = animationPeriod;
    }


    protected int getAnimationPeriod()
    {
        return animationPeriod;
    }

    protected void nextImage()
    {
        setImageIndex((getImageIndex() + 1) % getImages().size());
    }

}
