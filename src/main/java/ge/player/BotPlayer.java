package ge.player;

/**
 *
 * @author Kay Jay O'Nail
 */
public class BotPlayer extends Player
{
    public BotPlayer(BotConfig config)
    {
        super(config.color);
    }

    @Override
    void play()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
