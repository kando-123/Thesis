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
                    var builder = new UserPlayer.Builder();
                    builder.setConfig(userConfig);
                    builder.setScanner(world.scanner());
                    builder.setAccessor(world.accessor());
                    builder.setMarker(world.marker());
                    builder.setInvoker(viewInvoker);
                    var user = builder.get();
                    
                    assert (user != null);
                    
                    players.add(user);
                }
                case BotConfig botConfig ->
                {
                    var bot = new BotPlayer(world.scanner(), world.accessor(), botConfig, selfInvoker);
                    players.add(bot);
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
        return world.renderer();
    }
    
    public WorldScanner getWorldScanner()
    {
        return world.scanner();
    }
    
    public WorldAccessor getWorldAccessor()
    {
        return world.accessor();
    }
    
    public PlayersAccessor getPlayersAccessor()
    {
        return new PlayersAccessor(players);
    }
    
    void end()
    {
        var ender = players.removeFirst();
        ender.earn();
        
        players.addLast(ender);
    }
    
    void begin()
    {
        players.getFirst().play();
    }
    
    void build(BuildingField building)
    {
        world.substitute(building);
    }
    
    void mark(boolean value, UnaryPredicate<Field> predicate)
    {
        world.fieldStream().filter(f -> predicate.test(f)).forEach(f -> f.setMarked(value));
    }
}
