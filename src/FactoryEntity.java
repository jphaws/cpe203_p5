import java.util.Random;

public class FactoryEntity {
    public static Random rand = new Random();
    public static AbstractEntity create(String id, ImageStore imageStore, Point position) {
        if(id.equals("ghost")){
            return new GhostNotFull(id, position, imageStore.getImageList(id), 990, 100, position );
        }
        else if(id.equals("skeleton")){
            return new Skeleton(id, position, imageStore.getImageList(id), 700, 100, position);
        }
        else if(id.equals("gold")) { // Done
            return new Gold(id, position, imageStore.getImageList(id), 20000+rand.nextInt(30000 - 20000));
        }
        else if(id.equals("player")){ // Done
            return new Player(id, position, 5, 5, imageStore.getImageList(id), 0);
        }

       return null;
    }
}

