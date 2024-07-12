package my.player;

import java.awt.Color;

/**
 *
 * @author Kay Jay O'Nail
 */
public enum PlayerColor
{
    RANDOM(null),
    
    RED(new Color(214, 56, 56)),
    ORANGE(new Color(216, 217, 54)),
    YELLOW(new Color(214, 214, 56)),
    GREEN(new Color(56, 214, 56)),
    CYAN(new Color(56, 214, 214)),
    BLUE(new Color(56, 139, 214)),
    VIOLET(new Color(133, 56, 214)),
    MAGENTA(new Color(214, 56, 214));
    
    public final Color colorValue;
    
    private PlayerColor(Color rgbColor)
    {
        this.colorValue = rgbColor;
    }
    
    @Override
    public String toString()
    {
        String name = name();
        return name.substring(0, 1).concat(name.substring(1).toLowerCase());
    }
}
