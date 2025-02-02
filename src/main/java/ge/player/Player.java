package ge.player;

import ge.entity.*;
import ge.field.*;
import ge.world.*;
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

    protected final WorldScanner scanner;
    protected final WorldAccessor accessor;

    protected Player(WorldScanner scanner, WorldAccessor accessor, ContourColor color)
    {
        this.scanner = scanner;
        this.accessor = accessor;
        this.color = color;
        contour = ASSET_MANAGER.getImage(color.resource);
        money = INITIAL_MONEY;
    }

    public BufferedImage getContour()
    {
        return contour;
    }

    public int getMoney()
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
        return scanner.any(f -> f.isOwned(this) && building.predicate(accessor).test(f));
    }
    
    public boolean hasPlace(EntityType entity)
    {
        return scanner.any((Field f) ->
        {
            if (f.isOwned(this) && f instanceof Spawner s)
            {
                return s.canSpawn(entity);
            }
            else
            {
                return false;
            }
        });
    }

    public boolean hasMoney(BuildingType building)
    {
        long count = scanner.count((Field f) -> 
        {
            if (f.isOwned(this) && f instanceof BuildingField b)
            {
                return b.type() == building;
            }
            else
            {
                return false;
            }
        });
        return building.price((int) count) <= money;
    }
    
    public boolean hasMoney(EntityType entity)
    {
        return hasMoney(entity, 1);
    }
    
    public boolean hasMoney(EntityType entity, int number)
    {
        return money >= entity.price(number);
    }

    public int priceForNext(BuildingType building)
    {
        long current = scanner.count((Field f) ->
        {
            if (f instanceof BuildingField b && f.isOwned(this))
            {
                return b.type() == building;
            }
            else
            {
                return false;
            }
        });
        return building.price((int) current);
    }
    
    public void buy(BuildingField building) throws TooLittleMoneyException
    {
        int cost = priceForNext(building.type());
        if (cost > money)
        {
            throw new TooLittleMoneyException();
        }
        money -= cost;
    }
    
    public void buy(Entity entity) throws TooLittleMoneyException
    {
        int number = entity.getNumber();
        int cost = entity.type().price(number);
        if (cost > money)
        {
            throw new TooLittleMoneyException();
        }
        money -= cost;
    }
    
    public static class TooLittleMoneyException extends RuntimeException
    {
    }
    
    public void earn()
    {
        money += scanner.income(this);
    }
    
    public abstract String getName();
}
