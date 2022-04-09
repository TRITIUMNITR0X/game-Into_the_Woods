package models.prototypes.window.environments.menu.components.types;

import models.prototypes.controls.AMouseController;
import models.prototypes.window.environments.menu.AMenuModel;
import models.prototypes.window.environments.menu.components.AMenuComponent;
import models.utils.config.ConfigData;
import models.utils.drawables.IDrawable;
import models.utils.resources.Resources;
import models.utils.updates.IUpdatable;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public abstract class AMenuButton extends AMenuComponent implements IDrawable, IUpdatable {

    private boolean playSound = true;

    public AMenuButton(AMenuModel parentMenuModel, int x, int y, int w, int h) {
        super(parentMenuModel);

        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    public AMenuButton(AMenuModel parentMenuModel, int x, int y) {
        super(parentMenuModel);

        this.x = x;
        this.y = y;
        this.w = 200;
        this.h = 50;
    }

    public void playSound() {
        if(playSound) {
            Resources.playAudio("buttonclick");
        }
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

    public void registerInput() {
        AMouseController mc = parentMenuModel.getMouseController();
        if (mc.isLeftPressed()) {
            if(onClick(mc.getPos()[0], mc.getPos()[1])) {
                playSound();
                mc.resetInput();
            }
        } else {
            isInBounds(mc.getPos()[0], mc.getPos()[1]);
        }
    }

    public void setText(String s) {
        text = s;
    }

}
