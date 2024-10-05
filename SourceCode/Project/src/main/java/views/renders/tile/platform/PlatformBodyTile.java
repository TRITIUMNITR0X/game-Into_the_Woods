package views.renders.tile.platform;

import models.utils.resources.Resources;

/**
 * <p>A Platform is a physical barrier object. It is the fundamental piece that allows
 * the other actors to behave as if in a physical world.</p>
 * @author Andrew Stephens
 */
public class PlatformBodyTile extends APlatformTile {

    public PlatformBodyTile(Resources resources, String name) {
        super(resources, name);
    }

    public PlatformBodyTile(Resources resources) {
        this(resources, "platform_level1_2");
    }
}