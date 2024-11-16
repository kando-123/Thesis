package ge.field;

import ge.entity.EntityType;
import ge.utilities.Hex;

/**
 *
 * @author Kay Jay O'Nail
 */
public class BarracksField extends SpawnerField
{
    public BarracksField(Hex coords)
    {
        super(coords);
    }

    @Override
    public BuildingType type()
    {
        return BuildingType.BARRACKS;
    }

    @Override
    public boolean canSpawn(EntityType type)
    {
        return !isOccupied() && (type == EntityType.CAVALRY || type == EntityType.INFANTRY);
    }
}
