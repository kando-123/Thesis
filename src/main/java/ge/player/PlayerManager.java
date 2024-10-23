package ge.player;

import java.util.*;

/**
 *
 * @author Kay Jay O'Nail
 */
public class PlayerManager
{
    private final List<Player> players;
    
    public PlayerManager(PlayerConfig[] configs)
    {
        players = new LinkedList<>();
        
        for (var config : configs)
        {
            players.add(Player.newInstance(config));
        }
        
        
    }
}
