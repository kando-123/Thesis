package ge.player;

import ge.field.*;
import ge.world.WorldAccessor;
import java.awt.*;
import java.awt.image.*;

/**
 *
 * @author Kay Jay O'Nail
 */
public abstract class Player
{
    public enum ContourColor
    {
        RED(new Color(214, 56, 56)),
        ORANGE(new Color(216, 127, 54)),
        YELLOW(new Color(214, 214, 56)),
        GREEN(new Color(56, 214, 56)),
        CYAN(new Color(56, 214, 214)),
        BLUE(new Color(56, 139, 214)),
        VIOLET(new Color(133, 56, 214)),
        MAGENTA(new Color(214, 56, 214));

        public final Color rgb;
        public final String resource;

        private ContourColor(Color rgb)
        {
            this.rgb = rgb;
            resource = name().substring(0, 1).concat(name().substring(1).toLowerCase());
        }
    }

    public static final int MAX_NUMBER = ContourColor.values().length;

    private final ContourColor color;
    private final BufferedImage contour;

    private static final ContourAssetManager ASSET_MANAGER = ContourAssetManager.getInstance();

    private static final int INITIAL_MONEY = 1_000;
    private int money;

    private final WorldAccessor accessor;

    protected Player(WorldAccessor accessor, ContourColor color)
    {
        this.accessor = accessor;
        this.color = color;
        contour = ASSET_MANAGER.getImage(color.resource);
        money = INITIAL_MONEY;
    }

    public BufferedImage getContour()
    {
        return contour;
    }

    int getMoney()
    {
        return money;
    }

    ContourColor getColor()
    {
        return color;
    }

    public abstract void play();

    public boolean hasPlace(BuildingType building)
    {
        return accessor.anyMatching(f -> f.isOwned(this) && building.predicate(accessor).test(f));
    }

    public boolean hasMoney(BuildingType building)
    {
        long count = accessor.countMatching(f -> f.isOwned(this) && building.predicate(accessor).test(f));
        return building.price((int) count) <= money;
    }
}
