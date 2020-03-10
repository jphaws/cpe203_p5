import processing.core.PImage;
import java.util.*;

/*
WorldModel ideally keeps track of the actual size of our grid world and what is in that world
in terms of entities and background elements
 */

final class WorldModel {
   private int numRows;
   private int numCols;
   private Background background[][];
   private AbstractEntity occupancy[][];
   private Set<AbstractEntity> entities;

   private static final int FISH_REACH = 1;

   private static final String PLAYER_KEY = "player";
   private static final int PLAYER_NUM_PROPERTIES = 7;
   private static final int PLAYER_ID = 1;
   private static final int PLAYER_COL = 2;
   private static final int PLAYER_ROW = 3;
   private static final int PLAYER_LIMIT = 1;
   private static final int PLAYER_ACTION_PERIOD = 5;
   private static final int PLAYER_ANIMATION_PERIOD = 6;

   private static final String GHOST_KEY = "octo";
   private static final int GHOST_NUM_PROPERTIES = 7;
   private static final int GHOST_ID = 1;
   private static final int GHOST_COL = 2;
   private static final int GHOST_ROW = 3;
   private static final int GHOST_LIMIT = 4;
   private static final int GHOST_ACTION_PERIOD = 5;
   private static final int GHOST_ANIMATION_PERIOD = 6;

   private static final String SKELETON_KEY = "skeleton";
   private static final int SKELETON_NUM_PROPERTIES = 7;
   private static final int SKELETON_ID = 1;
   private static final int SKELETON_COL = 2;
   private static final int SKELETON_ROW = 3;
   private static final int SKELETON_ACTION_PERIOD = 4;
   private static final int SKELETON_ANIMATION_PERIOD = 4;
   private static final int SKELETON_PERIOD_SCALE = 4;
   private static final int SKELETON_ANIMATION_MIN = 50;
   private static final int SKELETON_ANIMATION_MAX = 150;

   private static final String OBSTACLE_KEY = "obstacle";
   private static final int OBSTACLE_NUM_PROPERTIES = 4;
   private static final int OBSTACLE_ID = 1;
   private static final int OBSTACLE_COL = 2;
   private static final int OBSTACLE_ROW = 3;

   private static final String FISH_KEY = "gold";
   private static final int FISH_NUM_PROPERTIES = 5;
   private static final int FISH_ID = 1;
   private static final int FISH_COL = 2;
   private static final int FISH_ROW = 3;
   private static final int FISH_ACTION_PERIOD = 4;

   private static final String ATLANTIS_KEY = "atlantis";
   private static final int ATLANTIS_NUM_PROPERTIES = 4;
   private static final int ATLANTIS_ID = 1;
   private static final int ATLANTIS_COL = 2;
   private static final int ATLANTIS_ROW = 3;

   private static final String SGRASS_KEY = "seaGrass";
   private static final int SGRASS_NUM_PROPERTIES = 5;
   private static final int SGRASS_ID = 1;
   private static final int SGRASS_COL = 2;
   private static final int SGRASS_ROW = 3;
   private static final int SGRASS_ACTION_PERIOD = 4;

   private static final String BGND_KEY = "background";
   private static final int BGND_NUM_PROPERTIES = 4;
   private static final int BGND_ID = 1;
   private static final int BGND_COL = 2;
   private static final int BGND_ROW = 3;

   private static final int PROPERTY_KEY = 0;
   Random rand = new Random();



   public WorldModel(int numRows, int numCols, Background defaultBackground) {
      this.numRows = numRows;
      this.numCols = numCols;
      this.background = new Background[numRows][numCols];
      this.occupancy = new AbstractEntity[numRows][numCols];
      this.entities = new HashSet<>();

      for (int row = 0; row < numRows; row++) {
         Arrays.fill(this.background[row], defaultBackground);
      }
   }

   public Optional<PImage> getBackgroundImage(Point pos)
   {
      if (withinBounds(pos))
      {
         return Optional.of((getBackgroundCell(pos)).getCurrentImage());
      }
      else
      {
         return Optional.empty();
      }
   }


   private Background getBackgroundCell(Point pos)
   {
      return background[pos.y][pos.x];
   }

   public Optional<Point> findOpenAround(Point pos)
   {
      for (int dy = -FISH_REACH; dy <= FISH_REACH; dy++)
      {
         for (int dx = -FISH_REACH; dx <= FISH_REACH; dx++)
         {
            Point newPt = new Point(pos.x + dx, pos.y + dy);
            if (withinBounds(newPt) &&
               !isOccupied(newPt))
            {
               return Optional.of(newPt);
            }
         }
      }

      return Optional.empty();
   }

   public boolean isOccupied(Point pos)
   {
      return withinBounds(pos) &&
              getOccupancyCell(pos) != null;
   }

   public boolean withinBounds(Point pos)
   {
      return pos.y >= 0 && pos.y < getNumRows() &&
              pos.x >= 0 && pos.x < getNumCols();
   }

   public AbstractEntity getOccupancyCell(Point pos)
   {
      return occupancy[pos.y][pos.x];
   }

   public Optional<AbstractEntity> getOccupant(Point pos)
   {
      if (isOccupied(pos))
      {
         return Optional.of(getOccupancyCell(pos));
      }
      else
      {
         return Optional.empty();
      }
   }

   private void setBackground(Point pos,
                             Background background)
   {
      if (this.withinBounds(pos))
      {
         setBackgroundCell(pos, background);
      }
   }

   private void setBackgroundCell(Point pos,
                                 Background background)
   {
      this.background[pos.y][pos.x] = background;
   }

   private void setOccupancyCell(Point pos,
                                       AbstractEntity entity)
   {
      occupancy[pos.y][pos.x] = entity;
   }

   public void addEntity(AbstractEntity entity)
   {
      if (withinBounds(entity.getPosition()))
      {
         setOccupancyCell(entity.getPosition(), entity);
         getEntities().add(entity);
      }
   }

   public void moveEntity(AbstractEntity entity, Point pos)
   {
      Point oldPos = entity.getPosition();
      if (withinBounds(pos) && !pos.equals(oldPos))
      {
         setOccupancyCell(oldPos, null);
         removeEntityAt(pos);
         setOccupancyCell(pos, entity);
         entity.setPosition(pos);
      }
   }

   public void removeEntity(AbstractEntity entity)
   {
      removeEntityAt(entity.getPosition());
   }

   private void removeEntityAt(Point pos)
   {
      if (withinBounds(pos)
              && getOccupancyCell(pos) != null)
      {
         AbstractEntity entity = getOccupancyCell(pos);

         /* this moves the entity just outside of the grid for
            debugging purposes */
         entity.setPosition(new Point(-1, -1));
         getEntities().remove(entity);
         setOccupancyCell(pos, null);
      }
   }

   private void tryAddEntity(AbstractEntity entity)
   {
      if (isOccupied(entity.getPosition()))
      {
         // arguably the wrong type of exception, but we are not
         // defining our own exceptions yet
         throw new IllegalArgumentException("position occupied");
      }

      addEntity(entity);
   }

   private Optional<AbstractEntity> nearestEntity(List<AbstractEntity> entities,
                                          Point pos)
   {
      if (entities.isEmpty())
      {
         return Optional.empty();
      }
      else
      {
         AbstractEntity nearest = entities.get(0);
         int nearestDistance = distanceSquared(nearest.getPosition(), pos);

         for (AbstractEntity other : entities)
         {
            int otherDistance = distanceSquared(other.getPosition(), pos);

            if (otherDistance < nearestDistance)
            {
               nearest = other;
               nearestDistance = otherDistance;
            }
         }

         return Optional.of(nearest);
      }
   }

   private int distanceSquared(Point p1, Point p2)
   {
      int deltaX = p1.x - p2.x;
      int deltaY = p1.y - p2.y;

      return deltaX * deltaX + deltaY * deltaY;
   }

   public Optional<AbstractEntity> findNearest(Point pos,
                                       Class kind)
   {
      List<AbstractEntity> ofType = new LinkedList<>();
      for (AbstractEntity  entity : getEntities())
      {
         if (kind.isInstance(entity))
         {
            ofType.add(entity);
         }
      }

      return nearestEntity(ofType, pos);
   }

   private boolean parsePlayer(String [] properties, ImageStore imageStore) {
      if (properties.length == PLAYER_NUM_PROPERTIES) {
         Point pt = new Point(Integer.parseInt(properties[PLAYER_COL]),
                 Integer.parseInt(properties[PLAYER_ROW]));

         Player player = new Player(properties[PLAYER_ID],
                 pt,
                 Integer.parseInt(properties[PLAYER_ACTION_PERIOD]),
                 Integer.parseInt(properties[PLAYER_ANIMATION_PERIOD]),
                 imageStore.getImageList(PLAYER_KEY),0);
      }
      return properties.length == PLAYER_NUM_PROPERTIES;
   }

   private boolean parseBackground(String [] properties, ImageStore imageStore)
   {
      if (properties.length == BGND_NUM_PROPERTIES)
      {
         Point pt = new Point(Integer.parseInt(properties[BGND_COL]),
                 Integer.parseInt(properties[BGND_ROW]));
         String id = properties[BGND_ID];
         setBackground(pt,
                 new Background(id, imageStore.getImageList(id)));
      }

      return properties.length == BGND_NUM_PROPERTIES;
   }

   private boolean parseGhost(String [] properties, ImageStore imageStore)
   {
      if (properties.length == GHOST_NUM_PROPERTIES)
      {
         Point pt = new Point(Integer.parseInt(properties[GHOST_COL]),
                 Integer.parseInt(properties[GHOST_ROW]));
         AbstractEntity entity = new GhostNotFull(properties[GHOST_ID],
                 pt,
                 imageStore.getImageList(GHOST_KEY),
                 Integer.parseInt(properties[GHOST_ACTION_PERIOD]),
                 Integer.parseInt(properties[GHOST_ANIMATION_PERIOD]),
                 Integer.parseInt(properties[GHOST_LIMIT]),0);
         tryAddEntity(entity);
      }

      return properties.length == GHOST_NUM_PROPERTIES;
   }

   private boolean parseSkeleton(String [] properties, ImageStore imageStore)
   {
      if (properties.length == SKELETON_NUM_PROPERTIES)
      {
         Point pt = new Point(Integer.parseInt(properties[SKELETON_COL]),
                 Integer.parseInt(properties[SKELETON_ROW]));
         AbstractEntity entity = new Skeleton(properties[SKELETON_ID],
                 pt,
                 imageStore.getImageList(SKELETON_KEY),
                 (Integer.parseInt(properties[SKELETON_ACTION_PERIOD]) / (SKELETON_PERIOD_SCALE)),
                 (Integer.parseInt(properties[SKELETON_ANIMATION_PERIOD])) + rand.nextInt(SKELETON_ANIMATION_MAX - SKELETON_ANIMATION_MIN));
         tryAddEntity(entity);
      }

      return properties.length == SKELETON_NUM_PROPERTIES;
   }


   private boolean parseObstacle(String [] properties, ImageStore imageStore)
   {
      if (properties.length == OBSTACLE_NUM_PROPERTIES)
      {
         Point pt = new Point(
                 Integer.parseInt(properties[OBSTACLE_COL]),
                 Integer.parseInt(properties[OBSTACLE_ROW]));
         Obstacle entity = Obstacle.createObstacle(properties[OBSTACLE_ID],
                 pt, imageStore.getImageList(OBSTACLE_KEY));
         tryAddEntity(entity);
      }

      return properties.length == OBSTACLE_NUM_PROPERTIES;
   }

   private boolean parseFish(String [] properties, ImageStore imageStore)
   {
      if (properties.length == FISH_NUM_PROPERTIES)
      {
         Point pt = new Point(Integer.parseInt(properties[FISH_COL]),
                 Integer.parseInt(properties[FISH_ROW]));
         AbstractEntity entity = new Gold(properties[FISH_ID],
                 pt, imageStore.getImageList(FISH_KEY), Integer.parseInt(properties[FISH_ACTION_PERIOD]));
         tryAddEntity(entity);
      }

      return properties.length == FISH_NUM_PROPERTIES;
   }

   private boolean parseAtlantis(String [] properties, ImageStore imageStore)
   {
      if (properties.length == ATLANTIS_NUM_PROPERTIES)
      {
         Point pt = new Point(Integer.parseInt(properties[ATLANTIS_COL]),
                 Integer.parseInt(properties[ATLANTIS_ROW]));
         AbstractEntity entity = Atlantis.createAtlantis(properties[ATLANTIS_ID],
                 pt, imageStore.getImageList(ATLANTIS_KEY));
         tryAddEntity(entity);
      }

      return properties.length == ATLANTIS_NUM_PROPERTIES;
   }

   private boolean parseSgrass(String [] properties, ImageStore imageStore)
   {
      if (properties.length == SGRASS_NUM_PROPERTIES)
      {
         Point pt = new Point(Integer.parseInt(properties[SGRASS_COL]),
                 Integer.parseInt(properties[SGRASS_ROW]));
         AbstractEntity entity = SGrass.createSgrass(properties[SGRASS_ID],
                 pt,
                 Integer.parseInt(properties[SGRASS_ACTION_PERIOD]),
                 imageStore.getImageList(SGRASS_KEY));
         tryAddEntity(entity);
      }

      return properties.length == SGRASS_NUM_PROPERTIES;
   }

   private boolean processLine(String line, ImageStore imageStore)
   {
      String[] properties = line.split("\\s");
      if (properties.length > 0)
      {
         switch (properties[PROPERTY_KEY])
         {
            case BGND_KEY:
               return parseBackground(properties, imageStore);
            case GHOST_KEY:
               return parseGhost(properties, imageStore);
            case OBSTACLE_KEY:
               return parseObstacle(properties, imageStore);
            case FISH_KEY:
               return parseFish(properties, imageStore);
            case ATLANTIS_KEY:
               return parseAtlantis(properties, imageStore);
            case SGRASS_KEY:
               return parseSgrass(properties, imageStore);
            case PLAYER_KEY:
               return parsePlayer(properties, imageStore);
            case SKELETON_KEY:
               return parseSkeleton(properties,imageStore);
         }
      }

      return false;
   }

   public void load(Scanner in, ImageStore imageStore)
   {
      int lineNumber = 0;
      while (in.hasNextLine())
      {
         try
         {
            if (!processLine(in.nextLine(), imageStore))
            {
               System.err.println(String.format("invalid entry on line %d",
                       lineNumber));
            }
         }
         catch (NumberFormatException e)
         {
            System.err.println(String.format("invalid entry on line %d",
                    lineNumber));
         }
         catch (IllegalArgumentException e)
         {
            System.err.println(String.format("issue on line %d: %s",
                    lineNumber, e.getMessage()));
         }
         lineNumber++;
      }
   }

   public int getNumRows() {
      return numRows;
   }

   public int getNumCols() {
      return numCols;
   }

   public Set<AbstractEntity> getEntities() {
      return entities;
   }
}