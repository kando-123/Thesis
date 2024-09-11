package my.player;

import java.awt.image.BufferedImage;
import my.field.BuildingPriceCalculator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import my.utils.Hex;
import my.field.AbstractField;
import java.util.Set;
import my.field.BuildingField;
import my.field.ContoursManager;
import my.field.FieldType;
import my.world.World;

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

    private final BuildingPriceCalculator priceCalculator;

    public Player(PlayerType type, World.Accessor accessor, World.Marker marker)
    {
        this.type = type;
        this.marker = marker;
        this.accessor = accessor;

        country = new Country(this, accessor);
        money = INITIAL_MONEY;

        priceCalculator = BuildingPriceCalculator.getInstance();
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

    public void takeMoney(int outcome)
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

    public Set<Hex> getOwnedHexes()
    {
        return country.getTerritory();
    }
    
    private int getPriceFor(FieldType buildingType)
    {
        int count = country.getCount(buildingType);
        return priceCalculator.calculatePrice(buildingType, count);
    }

    public int getPriceFor(BuildingField building)
    {
        return getPriceFor(building.getType());
    }
    
    public int getCount(FieldType type)
    {
        return country.getCount(type);
    }

    public Hex setCapital(Hex newCapital)
    {
        return country.setCapital(newCapital);
    }

    public Hex getCapitalHex()
    {
        return country.getCapitalHex();
    }

    public Map<FieldType, Integer> getPrices()
    {
        Map<FieldType, Integer> prices = new HashMap<>();
        for (var value : FieldType.values())
        {
            prices.put(value, getPriceFor(value));
        }
        return prices;
    }

    private interface UnaryPredicate<T>
    {
        public boolean test(T item);
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
        if (predicate != null)
        {
            for (var hex : country.getTerritory())
            {
                if (predicate.test(hex))
                {
                    marker.mark(hex);
                }
            }
        }
    }

    public Set<FieldType> getErectableBuildings()
    {
        Set<FieldType> buildings = new HashSet<>();
        for (var value : FieldType.values())
        {
            if (value.isBuilding())
            {
                var predicate = predicates.get(value);
                for (var hex : country.getTerritory())
                {
                    if (predicate.test(hex))
                    {
                        buildings.add(value);
                        break;
                    }
                }
            }
        }
        return buildings;
    }

    public void play()
    {

    }

    //private static final int INCOME_PER_FIELD = 1;
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
