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
    
    public Field getField(Hex coords)
    {
        return world.getField(coords);
    }
}
