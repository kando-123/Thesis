package ge.player;

/**
 *
 * @author Kay Jay O'Nail
 */
public class UserConfig extends PlayerConfig
{
    final String name;

    private UserConfig(Player.ContourColor color, String name)
    {
        super(color);
        this.name = name;
    }
    
    public static class Builder
    {
        private Player.ContourColor color;
        private String name;
        
        public void setColor(Player.ContourColor newColor)
        {
            color = newColor;
        }
        
        public boolean hasColor()
        {
            return color != null;
        }
        
        public void setName(String newName)
        {
            name = newName;
        }
        
        public UserConfig get()
        {
            if (name != null && !name.isBlank() && color != null)
            {
                return new UserConfig(color, name);
            }
            else
            {
                return null;
            }
        }
    }
}
