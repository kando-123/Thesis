package my.player;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import my.utils.Hex;
import my.field.AbstractField;
import my.field.BuildingField;
import my.field.FieldType;
import my.world.World;

class ContoursManager
{
    private Map<PlayerColor, BufferedImage> contours;
    
    private ContoursManager()
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
    
    private static final ContoursManager instance = new ContoursManager();

    public static ContoursManager getInstance()
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
    private final World.Marker marker;
    private final World.Accessor accessor;

    private int money;
    private static final int INITIAL_MONEY = 500;

    public Player(PlayerType type, World.Accessor accessor, World.Marker marker)
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
        contour = ContoursManager.getInstance().getContour(color);
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

    public void spendMoney(int outcome)
    {
        money -= outcome;
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

    private final Map<FieldType, UnaryPredicate<Hex>> predicates = createPredicates();

    private Map<FieldType, UnaryPredicate<Hex>> createPredicates()
    {
        var map = new HashMap<FieldType, UnaryPredicate<Hex>>();

        map.put(FieldType.BARRACKS, (Hex hex) ->
        {
            var field = accessor.getFieldAt(hex);
            return field != null && field.isPlains();
        });
        map.put(FieldType.TOWN, (Hex hex) ->
        {
            var field = accessor.getFieldAt(hex);
            return field != null && field.isPlains();
        });
        map.put(FieldType.VILLAGE, (Hex hex) ->
        {
            var field = accessor.getFieldAt(hex);
            return field != null && field.isPlains();
        });
        map.put(FieldType.FORTRESS, (Hex hex) ->
        {
            var field = accessor.getFieldAt(hex);
            return field != null && field.isContinental();
        });
        map.put(FieldType.MINE, (Hex hex) ->
        {
            var field = accessor.getFieldAt(hex);
            return field != null && field.isMountainous();
        });
        map.put(FieldType.FARMFIELD, (Hex hex) ->
        {
            var field = accessor.getFieldAt(hex);
            if (field.isPlains())
            {
                for (var neighbor : hex.neighbors())
                {
                    field = accessor.getFieldAt(neighbor);
                    if (field != null && field.getType() == FieldType.VILLAGE)
                    {
                        return true;
                    }
                }
            }
            return false;
        });
        map.put(FieldType.SHIPYARD, (Hex hex) ->
        {
            var field = accessor.getFieldAt(hex);
            if (field.isPlains())
            {
                for (var neighbor : hex.neighbors())
                {
                    field = accessor.getFieldAt(neighbor);
                    if (field != null && field.isMarine())
                    {
                        return true;
                    }
                }
            }
            return false;
        });

        return map;
    }

    public void markFor(FieldType building)
    {
        UnaryPredicate<Hex> predicate = predicates.get(building);
        country.markIf(predicate, marker);
    }

    public boolean canBuild(BuildingField building)
    {
        var predicate = predicates.get(building.getType());
        return country.isAny(predicate);
    }
    
    public boolean canAfford(BuildingField building)
    {
        int count = country.count(building.getType());
        return building.computePrice(count) <= money;
    }
    
    public int computePriceFor(BuildingField building)
    {
        int count = country.count(building.getType());
        return building.computePrice(count);
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
