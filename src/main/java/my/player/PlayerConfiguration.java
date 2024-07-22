package my.player;


/**
 *
 * @author Kay Jay O'Nail
 */
public class PlayerConfiguration
{
    public PlayerType type;
    public PlayerColor color;
    public String name;
    
    @Override
    public String toString()
    {
        return String.format("PlayerParameters@[type=%s, color=%s, name=%s]",
                type, color, name);
    }
}
