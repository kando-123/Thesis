package my.player;

import java.awt.image.BufferedImage;
import my.entity.AbstractEntity;
import my.utils.Hex;
import my.field.AbstractField;
import my.field.BuildingField;
import my.world.WorldAccessor;
import my.world.WorldMarker;

/**
 *
 * @author Kay Jay O'Nail
 */
public class Player
{
    public static final int MAX_PLAYERS_COUNT = PlayerColor.values().length - 1;

    private final PlayerType type;
    private String name;
    private PlayerColor color;
    private BufferedImage contour;

    private final Country country;
    private final WorldMarker marker;
    private final WorldAccessor accessor;

    private int money;
    private static final int INITIAL_MONEY = 1000; // for testing

    public Player(PlayerType type, WorldAccessor accessor, WorldMarker marker)
    {
        this.type = type;
        this.marker = marker;
        this.accessor = accessor;

        country = new Country(accessor);
        money = INITIAL_MONEY;
    }

    public PlayerType getType()
    {
        return type;
    }

    public void setColor(PlayerColor newColor)
    {
        color = newColor;
        contour = ContourAssetManager.getInstance().getContour(color);
    }

    public PlayerColor getColor()
    {
        return color;
    }

    public BufferedImage getContour()
    {
        return contour;
    }

    public void setName(String newName)
    {
        name = newName;
    }

    public String getName()
    {
        return name;
    }

    public int getMoney()
    {
        return money;
    }

    public void capture(AbstractField field)
    {
        field.setOwner(this);
        country.addField(field);
    }

    public void release(AbstractField field)
    {
        field.setOwner(null);
        country.removeField(field);
    }

    public Hex setCapital(Hex newCapital)
    {
        return country.setCapital(newCapital);
    }

    public Hex getCapitalHex()
    {
        return country.getCapitalHex();
    }

    public boolean canBuild(BuildingField building)
    {
        var predicate = building.getPredicate(accessor);
        return country.isAny(predicate);
    }
    
    public boolean canAfford(BuildingField building)
    {
        int count = country.count(building.getType());
        return building.computePrice(count) <= money;
    }

    public void markFor(BuildingField building)
    {
        var predicate = building.getPredicate(accessor);
        country.markIf(predicate, marker);
    }
    
    public boolean canHire(AbstractEntity entity)
    {
        var predicate = entity.getPredicate(accessor);
        return country.isAny(predicate);
    }

    public void markFor(AbstractEntity entity)
    {
        var predicate = entity.getPredicate(accessor);
        country.markIf(predicate, marker);
    }
    
    public int computePriceFor(BuildingField building)
    {
        int count = country.count(building.getType());
        return building.computePrice(count);
    }
    
    public int buy(BuildingField building)
    {
        return (money -= computePriceFor(building));
    }
    
    public int buy(AbstractEntity entity)
    {
        return (money -= entity.computePrice());
    }
    
    public void resetEntities()
    {
        country.resetEntities();
    }

    public void play()
    {
        // Bot method.
    }

    public void endRound()
    {
        money += country.getIncome();
    }

    @Override
    public String toString()
    {
        return String.format("Player@[type=%s, color=%s, name=%s]", type, color, name);
    }
}
