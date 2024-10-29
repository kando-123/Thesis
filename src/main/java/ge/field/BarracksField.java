package ge.field;

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
    public BuildingType getType()
    {
        return BuildingType.BARRACKS;
    }
}
