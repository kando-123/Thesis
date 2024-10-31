package ge.world;

import ge.field.Field;
import ge.utilities.Hex;

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
    
    public Field getField(Hex coords)
    {
        return world.getField(coords);
    }
}
