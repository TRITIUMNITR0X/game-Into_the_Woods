package models.utils.config;

import javax.swing.*;
import java.awt.*;

/**
 * <p></p>
 */
public class DisplayInfo {

    public static float SYS_SCALEX = 1f, SYS_SCALEY = 1f;

    /**
     * <p></p>
     */
    public DisplayInfo () {
        JFrame f = new JFrame();
        GraphicsConfiguration gc = f.getGraphicsConfiguration();
        SYS_SCALEX = (float) gc.getDefaultTransform().getScaleX();
        SYS_SCALEY = (float) gc.getDefaultTransform().getScaleY();
    }
}
