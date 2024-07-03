package my.player;

/**
 *
 * @author Kay Jay O'Nail
 */
public enum PlayerColor
{
    RANDOM,
    
    RED,
    ORANGE,
    YELLOW,
    GREEN,
    BLUE,
    VIOLET,
    MAGENTA;
    
    @Override
    public String toString()
    {
        String name = name();
        return name.substring(0, 1).concat(name.substring(1).toLowerCase());
    }
}