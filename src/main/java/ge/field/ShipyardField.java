package ge.field;

import ge.entity.EntityType;
import ge.utilities.Hex;

/**
 *
 * @author Kay Jay O'Nail
 */
public class ShipyardField extends SpawnerField
{
    public ShipyardField(Hex coords)
    {
        super(coords);
    }

    @Override
    public BuildingType getType()
    {
        return BuildingType.SHIPYARD;
    }

    @Override
    public boolean canSpawn(EntityType type)
    {
        return !isOccupied() && type == EntityType.NAVY;
    }
}
