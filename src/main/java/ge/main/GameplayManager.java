package ge.main;

import ge.field.*;
import ge.player.*;
import ge.utilities.*;
import ge.view.*;
import ge.world.*;
import java.util.*;

/**
 *
 * @author Kay Jay O'Nail
 */
public class GameplayManager
{
    private final World world;
    private final List<Player> players;
    
    public GameplayManager(WorldConfig config)
    {
        world = new World(config);
        players = new LinkedList<>();
    }

    void makePlayers(PlayerConfig[] playerConfigs, Invoker<ViewManager> viewInvoker)
    {
        var selfInvoker = new Invoker<>(this);

        for (PlayerConfig config : playerConfigs)
        {
            switch (config)
            {
                case UserConfig userConfig ->
                {
                    players.add(new UserPlayer(userConfig, viewInvoker));
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
            var capital = capitals[i];
            var player = players.get(i);
            world.getField(capital).setOwner(player);

            for (var neighbor : capital.neighbors())
            {
                var field = world.getField(neighbor);
                if (field != null && field instanceof ContinentalField)
                {
                    field.setOwner(player);
                }
            }
        }
    }

    public WorldRenderer getWorldRenderer()
    {
        return world.getRenderer();
    }
    
    void end()
    {
        // end procedure
        
        players.addLast(players.removeFirst());
    }
    
    void begin()
    {
        players.getFirst().play();
    }
}
