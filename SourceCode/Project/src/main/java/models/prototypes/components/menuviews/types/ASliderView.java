package models.prototypes.components.menuviews.types;

import models.prototypes.components.menuviews.AMenuComponent;
import models.prototypes.controls.AMouseController;
import models.prototypes.environments.menu.AMenuEnvironment;
import models.utils.config.Config;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * <p></p>
 * @param <E> -
 */
public abstract class ASliderView<E> extends AMenuComponent {

    private E e;

    protected final AButtonView button;

    private final BufferedImage trackImage;
    private final BufferedImage notchImage;

    protected ArrayList<E> values = new ArrayList<>();
    protected int itemCount = 2;
    protected int previous = 0;
    protected int current = previous;
    private float notchDistance = 0;

    private boolean showNotches = true;

    /**
     * <p></p>
     * @param parentMenuModel -
     * @param x -
     * @param y -
     * @param w -
     * @param h -
     */
    public ASliderView(AMenuEnvironment parentMenuModel, int x, int y, int w, int h) {
        super(parentMenuModel);

        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;

        trackImage = getParentEnvironment().getResources().getImage("slider_track");
        notchImage = getParentEnvironment().getResources().getImage("slider_notch");

        BufferedImage buttonImg = getParentEnvironment().getResources().getImage("button_slider");
        float scaleW = (float)buttonImg.getHeight() / h * .5f;

        button = new AButtonView(getParentEnvironment(), x, y, (int)(scaleW * h), h) {
            @Override
            public boolean onClick(float x, float y) {
                if(!isInBounds(x, y)) {
                    return false;
                }
                isPressed = true;
                return true;
            }

            @Override
            public void registerInput() {
                AMouseController mc = getParentEnvironment().getMouseController();
                if (mc.isLeftPressed()) {
                    ASliderView.this.isPressed = onClick(mc.getPos()[0], mc.getPos()[1]);
                    if(isPressed) {
                        playSound();
                        setPlaySound(false);
                    }
                } else {
                    ASliderView.this.isPressed = false;
                    isPressed = false;
                    setPlaySound(true);
                }
            }
        };
        button.setBackgroundImage(getParentEnvironment().getResources().getImage("button_slider"));
        button.setImageScaling(ImageScale.FIT_CENTERED);

        init();

        build();
    }

    /**
     * <p></p>
     */
    public abstract void init();

    /**
     * <p></p>
     * @param x -
     * @param y -
     */
    public void moveSliderTo(int x, int y) {

        x = (int)(x / Config.scaledW);
        if(x < this.x) {
            x = this.x;
        }

        int newX = (int)((x - this.x) / notchDistance);
        if(newX > itemCount -1) {
            newX = itemCount -1;
        }

        current = newX;

        x = (int)(this.x + (newX * notchDistance));
        button.setX(x);

    }

    /**
     * <p></p>
     */
    public abstract void doSetting();

    /**
     * <p></p>
     */
    public void build() {
        notchDistance = (w - button.getW()) / (float)(itemCount -1);
        button.setX(this.x + (int)(notchDistance*current));
    }

    /**
     * <p></p>
     * @param showNotches -
     */
    public void showNotches(boolean showNotches) {
        this.showNotches = showNotches;
    }

    @Override
    public boolean onClick(float x, float y) {
        return false;
    }

    @Override
    public void draw(Graphics2D g) {
        super.draw(g);

        float scaleH = .1f;
        g.drawImage(
                trackImage,
                (int) (x * Config.scaledW),
                (int)((y * Config.scaledH) + (.5f * h * Config.scaledH) - (.5f *  scaleH * h * Config.scaledH)),
                (int) (w * Config.scaledW),
                (int) (scaleH * h * Config.scaledH), null);

        if(showNotches) {
            if ((Config.scaledW * notchImage.getWidth() * (h / (float) notchImage.getHeight())) > (w / notchDistance)) {
                float scaleW = (float)notchImage.getHeight() / h * .5f;
                for (int i = 0; i < w; i++) {
                    g.drawImage(
                            notchImage,
                            (int) (((x + i) * Config.scaledW)),
                            (int) ((y * Config.scaledH) + (h * Config.scaledH * .75) - (h * Config.scaledH * .5f)),
                            (int) (Config.scaledW * notchImage.getWidth() * (h / notchImage.getHeight())),
                            (int) (h * Config.scaledH * .5f),
                            null);
                    i += notchDistance;
                }
            }
        }

        button.draw(g);
    }

    @Override
    public void registerInput() {
        if(button != null) {
            button.registerInput();
            if(button.isPressed()) {
                moveSliderTo(
                        getParentEnvironment().getMouseController().getPos()[0],
                        getParentEnvironment().getMouseController().getPos()[1]);
                doSetting();
            }
        }
    }

}
