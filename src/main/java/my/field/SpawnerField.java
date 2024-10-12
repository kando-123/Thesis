package my.field;

import my.field.FieldType;

/**
 *
 * @author Kay Jay O'Nail
 */
public abstract class SpawnerField extends BuildingField implements Spawner
{
    protected SpawnerField(FieldType type)
    {
        super(type);
    }
}
