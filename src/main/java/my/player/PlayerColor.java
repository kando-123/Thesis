package my.player;

/**
 *
 * @author Kay Jay O'Nail
 */
public enum PlayerColor
{
    RED("/Contours/Red.png"),
    ORANGE("/Contours/Orange.png"),
    YELLOW("/Contours/Yellow.png"),
    GREEN("/Contours/Green.png"),
    BLUE("/Contours/Blue.png"),
    VIOLET("/Contours/Violet.png"),
    MAGENTA("/Contours/Magenta.png");
    
    public final String path;
    
    private PlayerColor(String path)
    {
        this.path = path;
    }
}
