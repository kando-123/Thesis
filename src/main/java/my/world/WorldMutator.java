package my.world;

import my.field.AbstractField;

/**
 *
 * @author Kay Jay O'Nail
 */
public class WorldMutator
{
    private final World world;

    public WorldMutator(World world)
    {
        this.world = world;
    }
    
    public void substitute(AbstractField oldField, AbstractField newField)
    {
        world.substitute(oldField, newField);
    }
}
