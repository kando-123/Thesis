package my.field;

import my.entity.AbstractEntity;

/**
 *
 * @author Kay Jay O'Nail
 */
public class CapitalField extends AbstractField implements Defense, Spawner
{
    public CapitalField()
    {
        super(FieldType.CAPITAL);
    }

    @Override
    public boolean canSpawn(AbstractEntity entity)
    {
        return isFree();
    }
}