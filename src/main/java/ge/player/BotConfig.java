package ge.player;

/**
 *
 * @author Kay Jay O'Nail
 */
public class BotConfig extends PlayerConfig
{
    private BotConfig(Player.ContourColor color)
    {
        super(color);
    }
    
    public static class Builder
    {
        private Player.ContourColor color;
        
        public void setColor(Player.ContourColor newColor)
        {
            color = newColor;
        }
        
        public boolean hasColor()
        {
            return color != null;
        }
        
        public Player.ContourColor getColor()
        {
            return color;
        }
        
        public BotConfig get()
        {
            if (color != null)
            {
                return new BotConfig(color);
            }
            else
            {
                return null;
            }
        }
    }
}
