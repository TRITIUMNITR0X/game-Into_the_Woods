package models.actors.platforms;

import models.camera.Camera;
import models.prototypes.level.prop.AProp;
import models.utils.config.Config;
import models.utils.drawables.IDrawable;
import models.utils.resources.Resources;
import models.utils.updates.IUpdatable;
import views.Tile;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * <p>A Platform is a physical barrier object. It is the fundamental piece that allows
 * the other actors to behave as if in a physical world.</p>
 * @author Andrew Stephens
 */
public class Platform extends AProp implements IDrawable, IUpdatable {

    int cols = Math.max(1, (int)Math.ceil(w / (float) Tile.W));
    int rows = Math.max(1, (int)Math.ceil(h / (float)Tile.H));

    /**
     * The native image for this platform.
     */
    private final BufferedImage imageTop, imageBody;
    /**
     * The final drawn image
     */
    private BufferedImage image;

    /**
     * <p>Called from the subtypes, this method initializes the object.</p>
     * @param resources The resources of the parent Environment
     * @param imageNames The images for this object
     * @param x The horizontal position, relative to the default dimensions.
     * @param y The y position, relative to the default dimensions.
     * @param w The width, relative to the default dimensions.
     * @param h The height, relative to the default dimensions.
     * @param vx The horizontal velocity.
     * @param vy The vertical velocity.
     * @param hasGravity If the object should be effected by gravity.
     */
    public Platform(Resources resources, String[] imageNames,
                    float x, float y, float w, float h, float vx, float vy,
                    boolean hasGravity) {
        super(resources, x, y, w, h, vx, vy, hasGravity);

        this.imageTop = resources.getImage(imageNames[0]);
        this.imageBody = resources.getImage(imageNames[1]);

        //calcSubImages();
    }

    /**
     * <p>Called from the subtypes, this method initializes the object.</p>
     * @param resources The resources of the parent Environment
     * @param imageNames The images for this object
     * @param x The horizontal position, relative to the default dimensions.
     * @param y The y position, relative to the default dimensions.
     * @param w The width, relative to the default dimensions.
     * @param h The height, relative to the default dimensions.
     * @param hasGravity If the object should be effected by gravity.
     */
    public Platform(Resources resources, String[] imageNames,
                    float x, float y, float w, float h, boolean hasGravity) {
        super(resources, x, y, w, h, 0, 0, hasGravity);

        this.imageTop = resources.getImage(imageNames[0]);
        this.imageBody = resources.getImage(imageNames[1]);
    }

    /**
     * <p>Called from the subtypes, this method initializes the object.</p>
     * @param resources The resources of the parent Environment
     * @param imageName The image for this object
     * @param x The horizontal position, relative to the default dimensions.
     * @param y The y position, relative to the default dimensions.
     * @param w The width, relative to the default dimensions.
     * @param h The height, relative to the default dimensions.
     * @param vx The horizontal velocity.
     * @param vy The vertical velocity.
     * @param hasGravity If the object should be effected by gravity.
     */
    public Platform(Resources resources, String imageName,
                    float x, float y, float w, float h, float vx, float vy,
                    boolean hasGravity) {
        super(resources, x, y, w, h, vx, vy, hasGravity);

        this.imageTop = resources.getImage(imageName);
        this.imageBody = resources.getImage(imageName);
    }

    /**
     * <p>Called from the subtypes, this method initializes the object.</p>
     * @param resources The resources of the parent Environment
     * @param imageName The images for this object
     * @param x The horizontal position, relative to the default dimensions.
     * @param y The y position, relative to the default dimensions.
     * @param w The width, relative to the default dimensions.
     * @param h The height, relative to the default dimensions.
     * @param hasGravity If the object should be effected by gravity.
     */
    public Platform(Resources resources, String imageName,
                    float x, float y, float w, float h, boolean hasGravity) {
        super(resources, x, y, w, h, 0, 0, hasGravity);

        this.imageTop = resources.getImage(imageName);
        this.imageBody = resources.getImage(imageName);
    }

    public Platform(Resources resources, BufferedImage[] images,
                    float x, float y, float w, float h, float vX, float vY, boolean hasGravity) {
        super(resources, x, y, w, h, vX, vY, hasGravity);

        this.imageTop = images[0];
        this.imageBody = images[1];
    }

    @Override
    public void calcSubImages() {

        cols = Math.max(1, (int)Math.ceil(w / (float)Tile.W)) + 1;
        rows = Math.max(1, (int)Math.ceil(h / (float)Tile.H)) + 1;

        if(imageTop != null && imageBody != null) {
            Image tempTopImage = imageTop.getScaledInstance(
                    Tile.W,
                    Tile.H,
                    BufferedImage.SCALE_SMOOTH
            );

            Image tempBodyImage = imageBody.getScaledInstance(
                    Tile.W,
                    Tile.H,
                    BufferedImage.SCALE_SMOOTH
            );

            BufferedImage tempPlatformImage = new BufferedImage(
                    Tile.W * cols,
                    Tile.H * rows,
                    BufferedImage.TYPE_INT_ARGB);
            Graphics2D bGr = tempPlatformImage.createGraphics();

            for(int col = 0; col < cols; col++) {
                float x = (int) (col * Tile.W * Config.scaledW_zoom);
                float y = 0;

                bGr.drawImage(
                        tempTopImage,
                        (int) Math.floor(x),
                        (int) Math.floor(y),
                        (int) Math.floor((Tile.W) * Config.scaledW_zoom) + 1,
                        (int) Math.floor((Tile.H) * Config.scaledH_zoom) + 1,
                        null
                );

                for(int row = 1; row < rows; row++) {

                    y = (int) (row * Tile.H * Config.scaledW_zoom);

                    bGr.drawImage(
                            tempBodyImage,
                            (int) Math.floor(x),
                            (int) Math.floor(y),
                            (int) Math.floor((Tile.W) * Config.scaledW_zoom) + 1,
                            (int) Math.floor((Tile.H) * Config.scaledH_zoom) + 1,
                            null
                    );
                }
            }
            bGr.dispose();

            image = tempPlatformImage.getSubimage(0, 0, (int)Math.max(w, 1),  (int)Math.max(h, 1));
        }
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(Color.DARK_GRAY);

        float offsetX = ((x * Config.scaledW_zoom) + (Camera.camX));
        float offsetY = ((y * Config.scaledH_zoom) + (Camera.camY));

        g.drawImage(image,
                (int) Math.floor(offsetX),
                (int) Math.floor(offsetY),
                (int) Math.floor(w * Config.scaledW_zoom) + 1,
                (int) Math.floor(h * Config.scaledH_zoom) + 1,
                null);

        if(Config.DEBUG && isHighlighted) {
            Color c = Color.RED;
            g.setColor(c);
            g.drawRect((int) (offsetX), (int) (offsetY),
                    (int) (w * Config.scaledW_zoom),
                    (int) (h * Config.scaledH_zoom));
            g.drawString(x + " " + y, (int) (offsetX), (int) (offsetY));
        }
    }

    @Override
    public void drawAsHUD(Graphics2D g) {

        g.setColor(Color.WHITE);

        float[] offset = Camera.getRelativeOffsetBy(x, y, Camera.SCALE_MINIMAP);
        float[] scale = Camera.getRelativeScaleBy(w, h, Camera.SCALE_MINIMAP);

        g.fillRect((int) offset[0], (int) offset[1], (int) scale[0], (int) scale[1]);

    }

    @Override
    public AProp[] createTiles() {
        AProp[] tiles = super.createTiles();
        for(int i = 0; i < tiles.length; i++) {
            float x = getX() * i / Tile.W, y = getY() * i / Tile.H;
            Platform platform = new Platform(
                    resources, new BufferedImage[]{imageTop, imageBody},
                    x, y, w, h, vX, vY, hasGravity
            );
            tiles[i] = platform;
        }
        System.out.println(tiles.length);
        return tiles;
    }
}
