package ge.world;

import ge.entity.*;
import ge.field.*;
import ge.player.*;
import ge.player.action.*;
import ge.utilities.*;
import java.util.*;

/**
 *
 * @author Kay Jay O'Nail
 */
public class WorldScanner
{
    private final World world;

    WorldScanner(World world)
    {
        this.world = world;
    }

    @Override
    public Object clone()
    {
        return new WorldScanner(world);
    }

    public long count(UnaryPredicate<Field> predicate)
    {
        return world.fieldStream().filter(f -> predicate.test(f)).count();
    }

    public boolean any(UnaryPredicate<Field> predicate)
    {
        return world.fieldStream().anyMatch(f -> predicate.test(f));
    }

    public int income(Player player)
    {
        return world.fieldStream()
                .filter(f -> f.isOwned(player))
                .mapToInt((Field f) ->
                {
                    if (f instanceof Commercial c)
                    {
                        return c.getIncome();
                    }
                    else
                    {
                        return 1;
                    }
                })
                .sum();
    }

    public Hex center(Player player)
    {
        long n = world.fieldStream()
                .filter(f -> f.isOwned(player))
                .count();
        double x = world.fieldStream()
                .filter(f -> f.isOwned(player))
                .map(f -> f.getHex())
                .mapToDouble(h -> h.getX())
                .sum() / n;
        double y = world.fieldStream()
                .filter(f -> f.isOwned(player))
                .map(f -> f.getHex())
                .mapToDouble(h -> h.getY())
                .sum() / n;
        return Hex.at(x, y);
    }

    public List<Action> actions(Player player)
    {
        var actions = new ArrayList<Action>();
        var accessor = new WorldAccessor(world);
        
        for (var type : BuildingType.values())
        {
            if (!player.hasMoney(type))
            {
                continue;
            }
            
            var predicate = type.predicate(accessor);
            var hexes = (Hex[]) world.fieldStream()
                    .filter(f -> f.isOwned(player) && predicate.test(f))
                    .map(f -> (Hex) f.getHex())
                    .toArray(Hex[]::new);
            if (hexes.length > 0)
            {
                actions.add(new BuildAction(type, hexes, player));
            }
        }
        
        for (var type : EntityType.values())
        {
            int maxNumber = type.maxNumber(player.getMoney());
            if (maxNumber == 0)
            {
                continue;
            }
            if (maxNumber > Entity.MAXIMAL_NUMBER)
            {
                maxNumber = Entity.MAXIMAL_NUMBER;
            }
            
            var spawners = world.fieldStream()
                    .filter(f -> f.isOwned(player) && f instanceof Spawner s && s.canSpawn(type))
                    .map(f -> (Spawner) f)
                    .toArray(Spawner[]::new);
            if (spawners.length > 0)
            {
                actions.add(new HireAction(type, maxNumber, spawners, player));
            }
        }
        
        var fields = world.fieldStream()
                .filter(f ->
                {
                    var hex = f.getHex();
                    return f.isOwned(player)
                           && f.isOccupied()
                           && f.getEntity().canMove(hex, accessor);
                })
                .toArray(Field[]::new);
        for (var field : fields)
        {
            actions.add(new MoveAction(field, accessor));
        }
        
        fields = world.fieldStream()
                .filter(f -> f.isOwned(player) && f.isOccupied() && f.getEntity().canExtract(f.getHex(), accessor))
                .toArray(Field[]::new);
        for (var field : fields)
        {
            actions.add(new ExtractAction(field, accessor));
        }

        // Select an action and send appropriate command through the invoker:
        //     "build a building of SUCH type on THESE coords",
        //     "spawn an entity of SUCH type and THIS number in THAT spawner",
        //     "move THIS entity to THAT location",
        //     "extract THIS number of entities from THAT troop and move them THERE".
        
        return actions;
    }
}
