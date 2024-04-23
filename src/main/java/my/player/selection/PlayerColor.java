package my.player.selection;

/**
 *
 * @author Kay Jay O'Nail
 */
public enum PlayerColor
{
    RED,
    ORANGE,
    YELLOW,
    GREEN,
    BLUE,
    VIOLET,
    MAGENTA;
    
    public String toTitleCase()
    {
        String name = name();
        return name.substring(0, 1).concat(name.substring(1).toLowerCase());
    }
}
