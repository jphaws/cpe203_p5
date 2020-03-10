import processing.core.PImage;

import java.util.List;

public abstract class AbstractEntity{

    private String id;
    private Point position;
    private List<PImage> images;
    private int imageIndex;

    public AbstractEntity(String id, Point position, List<PImage> images) {
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
    }


    protected Point getPosition() {
        return position;
    }

    protected List<PImage> getImages() {
        return images;
    }

    protected void setImages(List<PImage> images){
        this.images=images;
    }

    protected void setPosition(Point position) {
        this.position = position;
    }

    protected void setImageIndex(int imageIndex){
        this.imageIndex = imageIndex;
    }

    protected int getImageIndex() {
        return imageIndex;
    }

    protected PImage getCurrentImage() {
        return (images.get(imageIndex));
    }

    protected String getId(){
        return id;
    }

    public String toString(){
        return id + " " + id + " " + position.x + " " + position.y;
    }
}
