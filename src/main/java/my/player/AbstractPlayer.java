package my.player;

import javax.swing.JPanel;

/**
 *
 * @author Kay Jay O'Nail
 */
public abstract class AbstractPlayer
{
    public static final int PLAYERS_COUNT = PlayerColor.values().length - 1;
    
    private final PlayerType type;
    private PlayerColor color;
    private String name;
    
    protected AbstractPlayer(PlayerType type)
    {
        this.type = type;
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
}
