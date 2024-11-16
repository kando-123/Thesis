package ge.world;

import ge.field.*;
import ge.player.*;
import ge.player.action.Action;
import ge.player.action.BuildAction;
import ge.utilities.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    
    public List<Action> actions(Player player, WorldAccessor accessor)
    {
        // Actions list.
        var actions = new ArrayList<Action>();
        
        // Own fields.
        var set = world.fieldStream()
                .filter(f -> f.isOwned(player))
                .collect(Collectors.toSet());
        
        // Building types.
        var types = BuildingType.values();
        
        // Find what can be built.
        for (var field : set)
        {
            var coords = field.getHex();
            for (var type : types)
            {
                if (type.predicate(accessor).test(field) && player.hasMoney(type))
                {
                    var building = BuildingField.newInstance(type, coords);
                    building.setOwner(player);
                    actions.add(new BuildAction(building));
                }
            }
        }
        
        return actions;
    }
}
