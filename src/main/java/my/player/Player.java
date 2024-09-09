package my.player;

import my.field.BuildingPriceCalculator;
import java.util.HashMap;
import java.util.Map;
import my.utils.Hex;
import my.field.Field;
import java.util.Set;
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
    private PlayerColor color;
    private String name;
    
    private final Country country;
    
    private int money;
    private static final int INITIAL_MONEY = 500;
    
    private final BuildingPriceCalculator priceCalculator;
    
    public Player(PlayerType type, World.Accessor worldAccessor)
    {
        this.type = type;
        
        country = new Country(this, worldAccessor);
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
    }
    
    public PlayerColor getColor()
    {
        return color;
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
    
    public void capture(Field field)
    {
        country.addField(field);
    }
    
    public void release(Field field)
    {
        country.removeField(field);
    }
    
    public Set<Hex> getOwnedHexes()
    {
        return country.getTerritory();
    }
    
    public int getPriceFor(FieldType type)
    {
        int count = country.getCount(type);
        return priceCalculator.calculatePrice(type, count);
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
