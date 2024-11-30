package ge.main;

import ge.entity.*;
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
    private Invoker<ViewManager> invoker;

    public GameplayManager(WorldConfig config)
    {
        world = new World(config);
        players = new LinkedList<>();
    }

    void makePlayers(PlayerConfig[] playerConfigs, Invoker<ViewManager> viewInvoker)
    {
        this.invoker = viewInvoker;

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

        Hex[] capitals = world.locateCapitals(players.size(), new Invoker<>(this));
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
        world.fieldStream()
                .filter(f -> f.isOwned(ender) && f.isOccupied())
                .forEach(f -> f.getEntity().setMovable(true));

        players.addLast(ender);
    }

    void begin()
    {
        players.getFirst().play();
    }

    void build(BuildingField building)
    {
        building.getOwner().buy(building);
        world.substitute(building);
    }
    
    void hire(Spawner spawner, Entity entity)
    {
        spawner.spawn(entity);
        entity.getOwner().buy(entity);
    }

    void markForBuilding(boolean value, Player player, BuildingType building)
    {
        var accessor = world.accessor();
        world.fieldStream()
                .filter(f -> f.isOwned(player))
                .filter(f -> building.predicate(accessor).test(f))
                .forEach(f -> f.setMarked(value));
    }

    void markForHiring(boolean value, Player player, EntityType entity)
    {
        world.fieldStream()
                .filter(f -> f.isOwned(player))
                .filter(f ->
                {
                    if (f instanceof Spawner s)
                    {
                        return s.canSpawn(entity);
                    }
                    else
                    {
                        return false;
                    }
                })
                .forEach(f -> f.setMarked(value));
    }

    void markForMoving(boolean value, Collection<Hex> range)
    {
        world.fieldStream()
                .filter(f -> range.contains(f.getHex()))
                .forEach(f -> f.setMarked(value));
    }

    private final Invoker<GameplayManager> self = new Invoker<>(this);

    void move(Field origin, Field target)
    {
        var entity = origin.getEntity();
        var player = entity.getOwner();

        origin.takeEntity();

        try
        {
            List<Hex> path = entity.path(origin, target, world.accessor());
            Set<Field> way = new HashSet<>();
            for (var hex : path)
            {
                var place = world.getField(hex);
                if (!(place instanceof SeaField))
                {
                    way.add(place);
                }
                for (var neighbor : hex.neighbors())
                {
                    var field = world.getField(neighbor);
                    if (field != null && !field.isOccupied()
                        && field instanceof PlainsField && !(place instanceof SeaField))
                    {
                        way.add(field);
                    }
                }
            }
            way.remove(target);
            for (var field : way)
            {
                field.setOwner(player);
            }
        }
        catch (Entity.GoalNotReachedException | Entity.TooFarAwayException e)
        {
            System.out.println(e.toString());
        }

        var remainder = target.placeEntity(entity, self);
        if (remainder != null)
        {
            origin.setEntity(remainder);
        }
    }

    void drop(Player loser)
    {
        players.remove(loser);
        world.fieldStream()
                .filter(f -> f.isOwned(loser))
                .forEach(f ->
                {
                    f.takeEntity();
                    f.clearOwner();
                });
        if (players.size() == 1)
        {
            var name = players.getFirst().getName();
            invoker.invoke(new VictoryMessageCommand(name));
        }
    }

    void adjustMorale(Hex coords, Player winner, Player loser)
    {
        final int radius = 5;
        world.fieldStream()
                .filter(f -> f.getHex().distance(coords) <= radius)
                .forEach(f ->
                {
                    if (f.isOccupied())
                    {
                        var e = f.getEntity();
                        int d = f.getHex().distance(coords);
                        if (f.isOwned(winner))
                        {
                            e.addMorale(radius - d);
                        }
                        else if (f.isOwned(loser))
                        {
                            e.addMorale(-(radius - d));
                        }
                    }
                });
    }
}
