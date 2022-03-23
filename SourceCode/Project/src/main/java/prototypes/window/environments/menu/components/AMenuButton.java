package prototypes.window.environments.menu.components;

import models.controls.menu.MenuMouseControls;
import models.data.PreferenceData;
import prototypes.window.environments.menu.AMenuModel;
import utils.drawables.IDrawable;
import utils.updates.IUpdatable;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * The abstract type AMenuButton. Implements IUpdatable and IDrawable.
 *
 * This is to be used for the creation of anonymous buttons.
 * This can also be used as a parent class for specific AMenuButton types.
 */
public abstract class AMenuButton implements IUpdatable, IDrawable {

    private ImageScale scaleType = ImageScale.FILL_XY;
    public enum ImageScale { FIT_CENTERED, CENTER_CROP, FILL_XY }

    private BufferedImage backgroundImage;

    // The Parent menu model which holds all pages and subpages.
    protected AMenuModel parentMenuModel;

    // Text to be displayed.
    // Will be removed if we add actual images in place of awt graphics.
    private String text = "";

    private final int x, y; // Coordinates on the 2D space, relative to the default screen size
    private final int w, h; // The width of the component

    private boolean isFocused = false;

    /**
     * Instantiates a new A menu button.
     * @param parentMenuModel the menu Model
     * @param x the x coordinate
     * @param y the y coordinate
     * @param w the width
     * @param h the height
     */
    public AMenuButton(AMenuModel parentMenuModel, int x, int y, int w, int h) {
        this.parentMenuModel = parentMenuModel;

        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    /**
     * Instantiates a new A menu button. Defaults the width and height to 200 x 50, scaled to the window size.
     * @param parentMenuModel the menu Model
     * @param x the x coordinate
     * @param y the y coordinate
     */
    public AMenuButton(AMenuModel parentMenuModel, int x, int y) {
        this.parentMenuModel = parentMenuModel;

        this.x = x;
        this.y = y;
        this.w = 200;
        this.h = 50;
    }

    /**
     * Checks if the passed coordinates are within the bounds of the component.
     * Takes screen scale into consideration.
     *
     * @param mx the mx
     * @param my the my
     * @return if the passed coordinates are within the bounds of the object.
     */
    protected boolean isInBounds(float mx, float my) {
        mx /= PreferenceData.scaledW;
        my /= PreferenceData.scaledH;

        boolean horizBound = (mx >= x && mx <= (x + w));
        boolean vertBound = (my >= y && my <= (y + h));

        setIsFocused(horizBound && vertBound);

        return horizBound && vertBound;
    }

    /**
     * If the object is allowed to have a focused state, this will cause the button to react when the user
     * mouses over it.
     * @param isFocused - if the component is focused on.
     */
    protected void setIsFocused(boolean isFocused) {
        this.isFocused = isFocused;
    }

    /**
     * The abstract method which is defined either upon instantiation or within a deriving class.
     * It is used to define actions that the button will execute.
     * This method is called automatically through the registerInput() method every time the Menu updates.
     *
     * @param x the x
     * @param y the y
     * @return Returns true if the button registers as clicked. Use IsInBounds() within the definition to test this.
     */
    public abstract boolean onClick(float x, float y);

    @Override
    public void draw(Graphics g) {

        g.setColor(Color.RED);
        float sW = PreferenceData.scaledW, sH = PreferenceData.scaledH;
        g.drawRect((int)(x * sW), (int)(y * sH), (int)(w * sW), (int)(h * sH));

        if(backgroundImage != null) {
            switch (scaleType) {
                case FIT_CENTERED -> {
                    float sBW = w / (float)backgroundImage.getWidth();
                    float sBH = h / (float)backgroundImage.getHeight();
                    float s = Math.min(sBW, sBH);
                    g.drawImage(backgroundImage,
                            (int)(((x * sW) + (w * .5f)) - (backgroundImage.getWidth() * s * .5f)),
                            (int)(((y * sH) + (h * .5f)) - (backgroundImage.getHeight() * s * .5f)),
                            (int)(backgroundImage.getWidth() * s),
                            (int)(backgroundImage.getHeight() * s),
                            null);
                }

                case CENTER_CROP -> {
                    float sBW = w / (float)backgroundImage.getWidth();
                    float sBH = h / (float)backgroundImage.getHeight();
                    float s = Math.min(sBW, sBH);
                    int     nx = (int)((backgroundImage.getWidth() - (backgroundImage.getWidth() * s)) * .5f),
                            nw = (int)(backgroundImage.getWidth() * s);
                    BufferedImage croppedImage =
                            backgroundImage.getSubimage(
                                    nx, 0,
                                    nw, backgroundImage.getHeight()
                            );
                    g.drawImage(croppedImage,
                            (int)(x * sW),
                            (int)(y * sH),
                            (int)(w * sH),
                            (int)(h * sH),
                            null);
                }
                default -> {
                    g.drawImage(backgroundImage, (int)(x * sW), (int)(y * sW), (int)(w * sH), (int)(h * sH), null);
                }
            }
        }

        g.drawString(text, (int)((x * sW) + (5 * sW)), (int)((y * sH) + (20 * sH)));

        if(isFocused) {
            g.setColor(new Color(255, 255, 255, 50));
            g.fillRect((int) (x * sW), (int) (y * sH), (int) (w * sW), (int) (h * sH));
        }
    }

    @Override
    public void update(float delta) {
        registerInput();
    }

    /**
     * Checked every update.
     * Registers the input from the parent MainMenuModel class. That class is defined locally as parentMenuModel.
     */
    public void registerInput() {
        if(parentMenuModel.getMouseController() instanceof MenuMouseControls mc) {
            if (mc.isLeftPressed()) {
                if(onClick(mc.getPos()[0], mc.getPos()[1])) {
                    mc.resetInput();
                }
            } else {
                isInBounds(mc.getPos()[0], mc.getPos()[1]);
            }
        }
    }

    /**
     * Sets text of the button.
     * Should be removed once we add image sources.
     *
     * @param s - the String of text
     */
    public void setText(String s) {
        text = s;
    }

    public void setBackgroundImage(BufferedImage backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    public void setImageScaling(ImageScale scaleType) {
        this.scaleType = scaleType;
    }

}
