package my.field;

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
    
    @Override
    final public boolean isSpawner()
    {
        return true;
    }
}
