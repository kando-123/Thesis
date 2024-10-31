package ge.world;

import ge.field.*;
import ge.player.*;
import ge.utilities.*;

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
    
    public long countMatching(UnaryPredicate<Field> predicate)
    {
        return world.fieldStream().filter(f -> predicate.test(f)).count();
    }
    
    public boolean anyMatching(UnaryPredicate<Field> predicate)
    {
        return world.fieldStream().anyMatch(f -> predicate.test(f));
    }
    
    public int getIncome(Player player)
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
}
