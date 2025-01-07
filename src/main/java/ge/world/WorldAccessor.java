package ge.world;

import ge.field.Field;
import ge.utilities.Hex;

/**
 *
 * @author Kay Jay O'Nail
 */
public class WorldAccessor implements FieldAccessor
{
    private final World world;
    
    WorldAccessor(World world)
    {
        this.world = world;
    }
    
    @Override
    public Field getField(Hex coords)
    {
        return world.getField(coords);
    }
}
