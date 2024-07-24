package my.player;

import my.utils.Hex;
import my.units.Field;
import java.util.HashMap;
import java.util.Map;

import my.world.*;

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
    
    private final Map<Hex, Field> territory;
    
    private int money;
    private static final int INITIAL_MONEY = 500;
    
    public Player(PlayerType type)
    {
        this.type = type;
        
        territory = new HashMap<>();
        money = INITIAL_MONEY;
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
    
    public void capture(Hex hex, Field field)
    {
        territory.put(hex, field);
        field.setOwner(this);
    }
    
    public void release(Hex hex)
    {
        territory.get(hex).setOwner(null);
        territory.remove(hex);
    }
    
    public void play()
    {
        
    }
    
    private static final int INCOME_PER_FIELD = 1;
    
    public void endRound()
    {
        money += territory.size() * INCOME_PER_FIELD;
    }
    
    @Override
    public String toString()
    {
        return String.format("Player@[type=%s, color=%s, name=%s]", type, color, name);
    }
}
