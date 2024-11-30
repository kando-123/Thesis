package ge.world;

import ge.entity.*;
import ge.field.*;
import ge.player.*;
import ge.player.action.*;
import ge.utilities.*;
import java.util.*;
import java.util.stream.*;

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
        return world
                .fieldStream()
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
        long n = world
                .fieldStream()
                .filter(f -> f.isOwned(player))
                .count();
        double x = world
                .fieldStream()
                .filter(f -> f.isOwned(player))
                .map(f -> f.getHex())
                .mapToDouble(h -> h.getX())
                .sum() / n;
        double y = world
                .fieldStream()
                .filter(f -> f.isOwned(player))
                .map(f -> f.getHex())
                .mapToDouble(h -> h.getY())
                .sum() / n;
        return Hex.at(x, y);
    }

    public List<Action> actions(Player player)
    {
        // Actions list.
        var actions = new ArrayList<Action>();

        // Own fields.
        var set = world.fieldStream()
                .filter(f -> f.isOwned(player))
                .collect(Collectors.toSet());
        
        // Accessor - to avoid creating a multitude of instances in the loop.
        var accessor = new WorldAccessor(world);

        // Find what can be built.
        for (var field : set)
        {
            /* Get building action. */
            var coords = field.getHex();
            for (var type : BuildingType.values())
            {
                if (type.predicate(accessor).test(field) && player.hasMoney(type))
                {
                    var building = BuildingField.newInstance(type, coords);
                    building.setOwner(player);
                    if (!actions.add(new BuildAction(building)))
                    {
                        return actions;
                    }
                }
            }
            
            /* Get hiring action */
            if (field instanceof Spawner spawner)
            {
                for (var type : EntityType.values())
                {
                    if (spawner.canSpawn(type))
                    {
                        for (int i = 1; i < Entity.MAXIMAL_NUMBER; ++i)
                        {
                            if (!player.hasMoney(type, i))
                            {
                                break;
                            }
                            var entity = Entity.newInstance(type, player, i);
                            if (!actions.add(new HireAction(spawner, entity)))
                            {
                                return actions;
                            }
                        }
                    }
                }
            }
            
            if (field.isOccupied())
            {
                Entity entity = field.getEntity();
                for (var target : entity.range(coords, accessor))
                {
                    var other = accessor.getField(target);
                    if (!actions.add(new MoveAction(field, other)))
                    {
                        return actions;
                    }
                }
            }
        }

        return actions;
    }
}
