package ge.field;

import ge.utilities.Hex;

/**
 *
 * @author Kay Jay O'Nail
 */
public class FortressField extends FortificationField
{
    public FortressField(Hex coords)
    {
        super(coords);
    }

    @Override
    public BuildingType getType()
    {
        return BuildingType.FORTRESS;
    }
}
