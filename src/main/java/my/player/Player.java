package my.player;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import my.entity.AbstractEntity;
import my.utils.Hex;
import my.field.AbstractField;
import my.field.BuildingField;
import my.world.WorldAccessor;
import my.world.WorldMarker;

class AssetManager
{
    private Map<PlayerColor, BufferedImage> contours;
    
    private AssetManager()
    {
        contours = new HashMap<>(PlayerColor.values().length);
        for (var color : PlayerColor.values())
        {
            if (color == PlayerColor.RANDOM)
            {
                continue;
            }

            String path = String.format("/Contours/%s.png", color.toString());
            InputStream stream = getClass().getResourceAsStream(path);
            try
            {
                BufferedImage image = ImageIO.read(stream);
                contours.put(color, image);
            }
            catch (IOException io)
            {
                System.err.println(io.getMessage());
            }
        }
    }
    
    private static final AssetManager instance = new AssetManager();

    public static AssetManager getInstance()
    {
        return instance;
    }
    
    public BufferedImage getContour(PlayerColor color)
    {
        return contours.get(color);
    }
    
}

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
    private static final int INITIAL_MONEY = 500;

    public Player(PlayerType type, WorldAccessor accessor, WorldMarker marker)
    {
        this.type = type;
        this.marker = marker;
        this.accessor = accessor;

        country = new Country(this, accessor);
        money = INITIAL_MONEY;
    }

    public PlayerType getType()
    {
        return type;
    }

    public void setColor(PlayerColor newColor)
    {
        color = newColor;
        contour = AssetManager.getInstance().getContour(color);
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
        country.addField(field);
    }

    public void release(AbstractField field)
    {
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

    public void play()
    {
        // Bot method.
    }

    public void endRound()
    {
        //money += territory.size() * INCOME_PER_FIELD;
    }

    @Override
    public String toString()
    {
        return String.format("Player@[type=%s, color=%s, name=%s]", type, color, name);
    }
}
