package ge.main;

import ge.player.Player;
import java.util.List;

/**
 *
 * @author Kay Jay O'Nail
 */
public class PlayersAccessor
{
    List<Player> list;

    public PlayersAccessor(List<Player> list)
    {
        this.list = list;
    }
    
    public Player current()
    {
        return list.getFirst();
    }
}
