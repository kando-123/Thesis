package my.world;

import my.field.Field;
import my.utils.Hex;

/**
 *
 * @author Kay Jay O'Nail
 */
public class WorldAccessor
{
    private final World world;
    
    public WorldAccessor(World world)
    {
        this.world = world;
    }
    
    public Field getFieldAt(Hex hex)
    {
        return world.getFieldAt(hex);
    }
}
