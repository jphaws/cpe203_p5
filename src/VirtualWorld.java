import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import processing.core.*;

/*
VirtualWorld is our main wrapper
It keeps track of data necessary to use Processing for drawing but also keeps track of the necessary
components to make our world run (eventScheduler), the data in our world (WorldModel) and our
current view (think virtual camera) into that world (WorldView)
 */

public final class VirtualWorld
        extends PApplet {
    private static final int TIMER_ACTION_PERIOD = 100;

    private static final int VIEW_WIDTH = 1280;
    private static final int VIEW_HEIGHT = 960;
    //private static final int VIEW_WIDTH = 640;
    //private static final int VIEW_HEIGHT = 480;
    private static final int TILE_WIDTH = 32;
    private static final int TILE_HEIGHT = 32;
    private static final int WORLD_WIDTH_SCALE = 2;
    private static final int WORLD_HEIGHT_SCALE = 2;

    private static final int VIEW_COLS = VIEW_WIDTH / TILE_WIDTH;
    private static final int VIEW_ROWS = VIEW_HEIGHT / TILE_HEIGHT;
    private static final int WORLD_COLS = VIEW_COLS * WORLD_WIDTH_SCALE;
    private static final int WORLD_ROWS = VIEW_ROWS * WORLD_HEIGHT_SCALE;

    private static final String IMAGE_LIST_FILE_NAME = "imagelist";
    private static final String DEFAULT_IMAGE_NAME = "background_default";
    private static final int DEFAULT_IMAGE_COLOR = 0x808080;

    private String LOAD_FILE_NAME = "start.sav";

    private static final String FAST_FLAG = "-fast";
    private static final String FASTER_FLAG = "-faster";
    private static final String FASTEST_FLAG = "-fastest";
    private static final double FAST_SCALE = 0.5;
    private static final double FASTER_SCALE = 0.25;
    private static final double FASTEST_SCALE = 0.10;

    private static double timeScale = 1.0;
    private String devMode = "";
    private boolean devModeLock = true;
    private int levelNumber = -1;
    private boolean levelCompleted = false;

    PFont f;
    private int level = 0;
    private ImageStore imageStore;
    private WorldModel world;
    private WorldView view;
    private EventScheduler scheduler;
    private Player player;
    private long next_time;

    public void settings() {
        size(VIEW_WIDTH, VIEW_HEIGHT);
    }

    /*
       Processing entry point for "sketch" setup.
    */
    public void setup() {
        f = createFont("Optima-BoldItalic", 16, true);

        this.imageStore = new ImageStore(
                createImageColored(TILE_WIDTH, TILE_HEIGHT, DEFAULT_IMAGE_COLOR));
        this.world = new WorldModel(WORLD_ROWS, WORLD_COLS,
                createDefaultBackground(imageStore));
        this.view = new WorldView(VIEW_ROWS, VIEW_COLS, this, world,
                TILE_WIDTH, TILE_HEIGHT);
        this.scheduler = new EventScheduler(timeScale);

        loadImages(IMAGE_LIST_FILE_NAME, imageStore, this);
        loadWorld(world, LOAD_FILE_NAME, imageStore);

        this.player = (Player)FactoryEntity.create("player", imageStore, new Point(5, 5));
        world.addEntity(player);

        scheduleActions(world, scheduler, imageStore);

        next_time = System.currentTimeMillis() + TIMER_ACTION_PERIOD;
    }

    public void draw() {
        if (levelCompleted) {
            switch (levelNumber) {
                case -1:
                    LOAD_FILE_NAME = "start.sav";
                    setup();
                    break;
                case 0:
                    LOAD_FILE_NAME = "level1.sav";
                    setup();
                    level = 1;
                    break;
                case 1:
                    LOAD_FILE_NAME = "level2.sav";
                    setup();
                    level = 2;
                    break;
                case 2:
                    LOAD_FILE_NAME = "level3.sav";
                    setup();
                    level = 3;
                    break;
                case 3:
                    LOAD_FILE_NAME = "Winner.sav";
                    setup();
                    break;
            }
            levelCompleted = false;
        }
        if(player.getGoldCount() >= 10){
            world.removeTempObstacles(scheduler);
        }
        long time = System.currentTimeMillis();
        if (time >= next_time) {
            this.scheduler.updateOnTime(time);
            next_time = time + TIMER_ACTION_PERIOD;
        }
        view.drawViewport();

        //Supposed to display health and gold count, and also enemies gold count
        textFont(f,16);
        fill(9);
        text("Health: " + player.getHealth(),10,20);
        text("GOLD Count: " + player.getGoldCount(),110,20);
        text("LEVEL: " + level,600,20);

        if(player.getHealth() == 0) {
            textFont(f, 60);
            text("U LOSE!",500,300);
            text("GAME OVER!",450,500);
        }

    }

    public void keyPressed() {
        if (key == CODED) {
            int dx = 0;
            int dy = 0;

            switch (keyCode) {
                case UP:
                    dy = -1;
                    player.turn(1);
                    break;
                case DOWN:
                    dy = 1;
                    player.turn(0);
                    break;
                case LEFT:
                    dx = -1;
                    player.turn(2);
                    break;
                case RIGHT:
                    dx = 1;
                    player.turn(3);
                    break;
            }
            Point pt = new Point(player.getPosition().x + dx, player.getPosition().y + dy);
            player.setImageIndex(0);
            player.scheduleActions(scheduler, world, imageStore);
            if (!world.isOccupied(pt)) {
                world.moveEntity(player, pt);
            }
            else if (world.getOccupancyCell(pt) instanceof Gold) {
                world.removeEntity(world.getOccupancyCell(pt));
                world.moveEntity(player, pt);
                player.increaseGoldCount();
            }
            else if (world.getOccupancyCell(pt) instanceof Portal) {
                levelNumber++;
                levelCompleted = true;
            }
        }
        if (key == ' ') {
           if(!(world.getPlayer() == null)) {
               player.useWeapon(world, imageStore, scheduler);
           }
        }

        if (key == ENTER || key == RETURN) {
            if (levelNumber == -1) {
                levelCompleted = true;
                levelNumber++;
            }
        }
        int vx = 0;
        int vy = 0;
//        if(key == 'w') {
//            vy -= 1;
//            WorldView v = view;
//            v.shiftView(vx, vy);
//        } else if(key == 's') {
//            vy += 1;
//            WorldView v = view;
//            v.shiftView(vx, vy);
//        } else if(key == 'a'){
//            vx -= 1;
//            WorldView v = view;
//            v.shiftView(vx, vy);
//        } else if(key == 'd') {
//            vx += 1;
//            WorldView v = view;
//            v.shiftView(vx, vy);
//        }

        if (key == '~') {
            devModeLock = false;
        }
        if (!devModeLock) {
            switch (key) {
                case 'o':
                    devMode = "o";       //activate obstacle edit mode
                    break;
                case 'p':
                    devMode = "p";       //activate portal edit mode
                    break;
                case 's':
                    devMode = "s";      //activate skeleton edit mode
                    break;
                case 'g':
                    devMode = "g";       //activate gold edit mode
                    break;
                case 'd':
                    devMode = "d";       //activate calpoly edit mode
                    break;
                case 'h':
                    devMode = "h";      //ghost edit mode
                    break;
                case 't':
                    devMode = "t";      //TempObstacle edit mode
                    break;
                case '+':
                    player.increaseGoldCount();
                    break;
                case 'f':
                    devMode = "f";      //Furnace edit mode
                    break;
                case 'l':
                    for (AbstractEntity e : world.getEntities()) {     //print list of all currently generated entities
                        if ((e instanceof GhostNotFull) || (e instanceof DerpPoly) ) {
                            System.out.println(e);
                        }
                    }
                    break;
            }
        }
    }

    public void mousePressed() {
        Point pressed = mouseToPoint(mouseX, mouseY);
        System.out.println("(" + pressed.x + ", " + pressed.y + ") " + devMode) ;

        switch (devMode) {
            case "o":
                if (world.getOccupancyCell(new Point(pressed.x, pressed.y)) instanceof Obstacle)
                    world.removeEntity(world.getOccupancyCell(new Point(pressed.x, pressed.y)));
                else {
                    world.addEntity(new Obstacle("obstacle", new Point(pressed.x, pressed.y), imageStore.getImageList("obstacle")));
                }
                break;
            case "t":
                if (world.getOccupancyCell(new Point(pressed.x, pressed.y)) instanceof TempObstacle)
                    world.removeEntity(world.getOccupancyCell(new Point(pressed.x, pressed.y)));
                else {
                    world.addEntity(new TempObstacle("temp", new Point(pressed.x, pressed.y), imageStore.getImageList("temp")));
                }
                break;
            case "p":
                if (world.getOccupancyCell(new Point(pressed.x, pressed.y)) instanceof Portal)
                    world.removeEntity(world.getOccupancyCell(new Point(pressed.x, pressed.y)));
                else {
                    world.addEntity(new Portal("portal", new Point(pressed.x, pressed.y), imageStore.getImageList("portal"), 0, 0));
                }
                break;
            case "d":
                if (world.getOccupancyCell(new Point(pressed.x, pressed.y)) instanceof DerpPoly)
                    world.removeEntity(world.getOccupancyCell(new Point(pressed.x, pressed.y)));
                else {
                    world.addEntity(new DerpPoly("derp", new Point(pressed.x, pressed.y), imageStore.getImageList("derp"), 0, 0));
                }
                break;
            case "s":
                if (world.getOccupancyCell(new Point(pressed.x, pressed.y)) instanceof Skeleton)
                    world.removeEntity(world.getOccupancyCell(new Point(pressed.x, pressed.y)));
                else {
                    world.addEntity(new Skeleton("skeleton", new Point(pressed.x, pressed.y), imageStore.getImageList("skeleton"), 900, 100, new Point(pressed.x, pressed.y)));
                }
                break;
            case "h":
                if (world.getOccupancyCell(new Point(pressed.x, pressed.y)) instanceof GhostNotFull
                || world.getOccupancyCell(new Point(pressed.x, pressed.y)) instanceof GhostFull)
                    world.removeEntity(world.getOccupancyCell(new Point(pressed.x, pressed.y)));
                else {
                    world.addEntity(new GhostNotFull("ghost", new Point(pressed.x, pressed.y), imageStore.getImageList("ghost"), 900, 100, new Point(pressed.x, pressed.y)));
                }
                break;
            case "g":
                if (world.getOccupancyCell(new Point(pressed.x, pressed.y)) instanceof Gold)
                    world.removeEntity(world.getOccupancyCell(new Point(pressed.x, pressed.y)));
                else {
                    world.addEntity(new Gold("gold", new Point(pressed.x, pressed.y), imageStore.getImageList("gold"), 100));
                }
                break;
            case "f":
                if (world.getOccupancyCell(new Point(pressed.x, pressed.y)) instanceof Furnace)
                    world.removeEntity(world.getOccupancyCell(new Point(pressed.x, pressed.y)));
                else {
                    world.addEntity(new Furnace("furnace", new Point(pressed.x, pressed.y), imageStore.getImageList("furnace"), 100));
                }
                break;
        }

        redraw();

    }

    private Point mouseToPoint(int x, int y) {
        return new Point(mouseX / 32, mouseY / 32);
    }


    public static Background createDefaultBackground(ImageStore imageStore) {
        return new Background(DEFAULT_IMAGE_NAME,
                imageStore.getImageList(DEFAULT_IMAGE_NAME));
    }

    public static PImage createImageColored(int width, int height, int color) {
        PImage img = new PImage(width, height, RGB);
        img.loadPixels();
        for (int i = 0; i < img.pixels.length; i++) {
            img.pixels[i] = color;
        }
        img.updatePixels();
        return img;
    }

    private static void loadImages(String filename, ImageStore imageStore,
                                   PApplet screen) {
        try {
            Scanner in = new Scanner(new File(filename));
            imageStore.loadImages(in, screen);
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }

    public static void loadWorld(WorldModel world, String filename,
                                 ImageStore imageStore) {
        try {
            Scanner in = new Scanner(new File(filename));
            world.load(in, imageStore);
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }

    public static void scheduleActions(WorldModel world,
                                       EventScheduler scheduler, ImageStore imageStore) {
        for (AbstractEntity entity : world.getEntities()) {
            //Only start actions for entities that include action (not those with just animations)
            if (entity instanceof AbstractActiveEntity)  //entity instanceof Execuable
                ((AbstractActiveEntity) entity).scheduleActions(scheduler, world, imageStore);
        }
    }

    public static void parseCommandLine(String[] args) {
        for (String arg : args) {
            switch (arg) {
                case FAST_FLAG:
                    timeScale = Math.min(FAST_SCALE, timeScale);
                    break;
                case FASTER_FLAG:
                    timeScale = Math.min(FASTER_SCALE, timeScale);
                    break;
                case FASTEST_FLAG:
                    timeScale = Math.min(FASTEST_SCALE, timeScale);
                    break;
            }
        }
    }

    public static void main(String[] args) {
        parseCommandLine(args);
        PApplet.main(VirtualWorld.class);
    }
}
