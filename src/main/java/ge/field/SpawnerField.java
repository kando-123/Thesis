package ge.field;

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
}
