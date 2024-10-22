package ge.player;

/**
 *
 * @author Kay Jay O'Nail
 */
public class UserPlayer extends Player
{
    private final String name;
    
    public UserPlayer(UserConfig config)
    {
        super(config.color);
        
        this.name = config.name;
    }

    @Override
    void play()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
