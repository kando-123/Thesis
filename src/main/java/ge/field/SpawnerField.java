package ge.field;

import ge.entity.Entity;
import ge.utilities.Hex;

/**
 *
 * @author Kay Jay O'Nail
 */
public abstract class SpawnerField extends BuildingField implements Spawner
{
    protected SpawnerField(Hex coords)
    {
        super(coords);
    }
    
    @Override
    public void spawn(Entity entity)
    {
        placeEntity(entity);
    }
}
