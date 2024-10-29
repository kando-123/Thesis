package ge.world;

import ge.field.*;
import ge.utilities.*;

/**
 *
 * @author Kay Jay O'Nail
 */
public class WorldAccessor
{
    private final World world;

    WorldAccessor(World world)
    {
        this.world = world;
    }

    @Override
    public Object clone()
    {
        return new WorldAccessor(world);
    }
    
    public Field getField(Hex coords)
    {
        return world.getField(coords);
    }
    
    public long countMatching(UnaryPredicate<Field> predicate)
    {
        return world.fieldStream().filter(f -> predicate.test(f)).count();
    }
    
    public boolean anyMatching(UnaryPredicate<Field> predicate)
    {
        return world.fieldStream().anyMatch(f -> predicate.test(f));
    }
}
