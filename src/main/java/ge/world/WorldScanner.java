package ge.world;

import ge.field.*;
import ge.player.*;
import ge.utilities.*;
import java.util.stream.Stream;

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
}
