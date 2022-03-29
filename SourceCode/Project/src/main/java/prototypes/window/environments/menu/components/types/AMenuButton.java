package prototypes.window.environments.menu.components.types;

import prototypes.controls.AMouseController;
import prototypes.window.environments.menu.AMenuModel;
import prototypes.window.environments.menu.components.AMenuComponent;
import utils.config.ConfigData;
import utils.files.Resources;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.Locale;

/**
 * The abstract type AMenuButton. Implements IUpdatable and IDrawable.
 * <p>
 * This is to be used for the creation of anonymous buttons.
 * This can also be used as a parent class for specific AMenuButton types.
 */
public abstract class AMenuButton extends AMenuComponent {

    /**
     * Instantiates a new A menu button.
     *
     * @param parentMenuModel the menu Model
     * @param x               the x coordinate
     * @param y               the y coordinate
     * @param w               the width
     * @param h               the height
     */
    public AMenuButton(AMenuModel parentMenuModel, int x, int y, int w, int h) {
        super(parentMenuModel);

        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    /**
     * Instantiates a new A menu button. Defaults the width and height to 200 x 50, scaled to the window size.
     *
     * @param parentMenuModel the menu Model
     * @param x               the x coordinate
     * @param y               the y coordinate
     */
    public AMenuButton(AMenuModel parentMenuModel, int x, int y) {
        super(parentMenuModel);

        this.x = x;
        this.y = y;
        this.w = 200;
        this.h = 50;
    }

    @Override
    public void draw(Graphics g) {

        //g.setColor(Color.RED);
        float sW = ConfigData.scaledW, sH = ConfigData.scaledH;

        //g.drawRect((int)(x * sW), (int)(y * sH), (int)(w * sW), (int)(h * sH));

        if(backgroundImage != null) {
            switch (scaleType) {

                case FIT_CENTERED -> {
                    float sBW = w / (float)backgroundImage.getWidth();
                    float sBH = h / (float)backgroundImage.getHeight();
                    float s = Math.min(sBW, sBH);
                    g.drawImage(backgroundImage,
                            (int)(((x * sW) + (w * sW * .5f)) - (backgroundImage.getWidth() * sW * s * .5f)),
                            (int)(((y * sH) + (h * sH * .5f)) - (backgroundImage.getHeight() * sH * s * .5f)),
                            (int)(backgroundImage.getWidth() * sW * s),
                            (int)(backgroundImage.getHeight() * sH * s),
                            null);
                    /*
                    if(tint != null) {
                        g.drawImage(tint,
                                (int)(((x * sW) + (w * sW * .5f)) - (backgroundImage.getWidth() * sW * s * .5f)),
                                (int)(((y * sH) + (h * sH * .5f)) - (backgroundImage.getHeight() * sH * s * .5f)),
                                (int)(backgroundImage.getWidth() * sW * s),
                                (int)(backgroundImage.getHeight() * sH * s),
                                null);
                    }
                    */
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
                            (int)(w * sW),
                            (int)(h * sH),
                            null);
                }
                default -> {
                    g.drawImage(backgroundImage, (int)(x * sW), (int)(y * sH), (int)(w * sW), (int)(h * sH), null);
                    /*
                    if(tint != null) {
                        g.drawImage(tint, (int)(x * sW), (int)(y * sH), (int)(w * sW), (int)(h * sH), null);
                    }
                    */
                }

            }
        }

        g.setColor(Color.BLACK);
        g.setFont(
                Resources.getFont("bahnschrift")
                        .deriveFont(AffineTransform.getScaleInstance(.8, 1))
                        .deriveFont(Font.PLAIN, 36 * sW));
        int strWidth = g.getFontMetrics().stringWidth(text.toUpperCase());
        g.drawString(
                text.toUpperCase(),
                (int)((x * sW) + (w * sW * .5) - (strWidth * .5)),
                (int)((y * sH) + (h * sH * .5)));

        if(isFocused) {
            g.setColor(new Color(255, 255, 255, 50));
            g.fillRect((int) (x * sW), (int) (y * sH), (int) (w * sW), (int) (h * sH));
        }
    }

    @Override
    public void update(float delta) {
        super.update(delta);
    }

    /**
     * Checked every update.
     * Registers the input from the parent MainMenuModel class. That class is defined locally as parentMenuModel.
     */
    public void registerInput() {
        AMouseController mc = parentMenuModel.getMouseController();
        if (mc.isLeftPressed()) {
            if(onClick(mc.getPos()[0], mc.getPos()[1])) {
                mc.resetInput();
            }
        } else {
            isInBounds(mc.getPos()[0], mc.getPos()[1]);
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

}
