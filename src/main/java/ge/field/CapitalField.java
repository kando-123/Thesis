package ge.field;

import ge.entity.EntityType;
import ge.utilities.Hex;

/**
 *
 * @author Kay Jay O'Nail
 */
public class CapitalField extends PropertyField implements Fortification, Spawner
{
    public CapitalField(Hex coords)
    {
        super(coords);
    }

    @Override
    public boolean canSpawn(EntityType type)
    {
        return !isOccupied() && (type == EntityType.CAVALRY || type == EntityType.INFANTRY);
    }
}
