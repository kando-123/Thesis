package my.player;

import java.util.HashSet;
import java.util.Set;
import my.world.field.*;

/**
 *
 * @author Kay Jay O'Nail
 */
public abstract class AbstractPlayer
{
    public static final int MAX_PLAYERS_COUNT = PlayerColor.values().length - 1;
    
    private final PlayerType type;
    private PlayerColor color;
    private String name;
    
    private final Set<Field> territory;
    
    protected AbstractPlayer(PlayerType type)
    {
        this.type = type;
        
        territory = new HashSet<>();
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
    
    public void capture(Field field)
    {
        territory.add(field);
        field.setOwnership(this);
    }
    
    public void release(Field field)
    {
        territory.remove(field);
        field.setOwnership(null);
    }
    
    @Override
    public String toString()
    {
        return String.format("AbstractPlayer@[type=%s, color=%s, name=%s]", type, color, name);
    }
}
