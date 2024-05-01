package my.player.selection;

/**
 *
 * @author Kay Jay O'Nail
 */
public class PlayersNumber
{
    public int local;
    public int remote;
    public int bot;
    
    public PlayersNumber(int local, int remote, int bot)
    {
        this.local = local;
        this.remote = remote;
        this.bot = bot;
    }
    
    public PlayersNumber()
    {
        local = 0;
        remote = 0;
        bot = 0;
    }
}
