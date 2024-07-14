package my.player;

/**
 *
 * @author Kay Jay O'Nail
 */
public class BotPlayer extends AbstractPlayer
{
    public BotPlayer()
    {
        super(PlayerType.BOT);
    }
    
    public void play()
    {
        System.out.println(String.format("Bot '%s' is playing.", getName()));
    }
}
