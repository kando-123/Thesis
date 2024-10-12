package my.world;

import my.unit.AbstractField;
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

    public AbstractField getFieldAt(Hex hex)
    {
        return world.getFieldAt(hex);
    }
}
