package my.player;

/**
 *
 * @author Kay Jay O'Nail
 */
public abstract class AbstractPlayer
{
    public static final int PLAYERS_COUNT = PlayerColor.values().length - 1;
    
    private final PlayerType type;
    
    protected AbstractPlayer(PlayerType type)
    {
        this.type = type;
    }
    
    public PlayerType getType()
    {
        return type;
    }
}
