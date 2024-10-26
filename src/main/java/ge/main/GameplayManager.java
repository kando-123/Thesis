package ge.main;

import ge.player.*;
import ge.utilities.*;
import ge.view.*;
import ge.world.*;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Kay Jay O'Nail
 */
public class GameplayManager
{
    private World world;
    private List<Player> players;
    
    void init(PlayerConfig[] playerConfigs, WorldConfig worldConfig, Invoker<ViewManager> invoker)
    {
        world = new World(worldConfig);
        players = new LinkedList<>();
        var selfInvoker = new Invoker<>(this);
        
        for (PlayerConfig config : playerConfigs)
        {
            switch (config)
            {
                case UserConfig userConfig ->
                {
                    players.add(new UserPlayer(userConfig, invoker));
                }
                case BotConfig botConfig ->
                {
                    players.add(new BotPlayer(botConfig, selfInvoker));
                }
                default ->
                {
                }
            }
        }
        
        Hex[] capitals = world.locateCapitals(players.size());
        for (int i = 0; i < capitals.length; ++i)
        {
            var field = world.getField(capitals[i]);
            field.setOwner(players.get(i));
        }
    }
    
    public WorldRenderer getWorldRenderer()
    {
        return world.getRenderer();
    }
}
