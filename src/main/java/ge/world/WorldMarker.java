package ge.world;

import ge.field.Field;
import ge.utilities.UnaryPredicate;

/**
 *
 * @author Kay Jay O'Nail
 */
public class WorldMarker
{
    private final World world;

    public WorldMarker(World world)
    {
        this.world = world;
    }
    
    public void mark(boolean value, UnaryPredicate<Field> predicate)
    {
        world.fieldStream().filter(f -> predicate.test(f)).forEach(f -> f.setMarked(value));
    }
}
