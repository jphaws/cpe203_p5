import processing.core.PApplet;
import processing.core.PImage;

import java.util.Optional;

/*
WorldView ideally mostly controls drawing the current part of the whole world
that we can see based on the viewport
*/

final class WorldView {
   private PApplet screen;
   private WorldModel world;
   private int tileWidth;
   private int tileHeight;
   private Viewport viewport;

   public WorldView(int numRows, int numCols, PApplet screen, WorldModel world,
                    int tileWidth, int tileHeight) {
      this.screen = screen;
      this.world = world;
      this.tileWidth = tileWidth;
      this.tileHeight = tileHeight;
      this.viewport = new Viewport(numRows, numCols);
   }

   //Copied from Functions

   public void shiftView(int colDelta, int rowDelta) {
      int newCol = clamp(viewport.getCol() + colDelta, 0,
              world.getNumCols() - viewport.getNumCols());
      int newRow = clamp(viewport.getRow() + rowDelta, 0,
              world.getNumRows() - viewport.getNumCols());

      viewport.shift(newCol, newRow);
   }

   private int clamp(int value, int low, int high) {
      return Math.min(high, Math.max(value, low));
   }

   private void drawBackground() {
      for (int row = 0; row < this.viewport.getNumCols(); row++) {
         for (int col = 0; col < this.viewport.getNumCols(); col++) {
            Point worldPoint = this.viewport.viewportToWorld(col, row);
            Optional<PImage> image = this.world.getBackgroundImage(worldPoint);
            if (image.isPresent()) {
               this.screen.image(image.get(), col * this.tileWidth,
                       row * this.tileHeight);
            }
         }
      }
   }

   private void drawEntities() {
      for (AbstractEntity entity : this.world.getEntities()) {
         Point pos = entity.getPosition();

         if (this.viewport.contains(pos)) {
            Point viewPoint = this.viewport.worldToViewport(pos.x, pos.y);
            this.screen.image(entity.getCurrentImage(),
                    viewPoint.x * this.tileWidth, viewPoint.y * this.tileHeight);
         }
      }
   }

   //Check this after fixing the last one

   public void drawViewport() {
      drawBackground();
      drawEntities();
   }
}