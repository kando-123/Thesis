package ge.field;

import ge.utilities.Hex;

/**
 *
 * @author Kay Jay O'Nail
 */
public abstract class CommercialField extends BuildingField implements Commercial
{
    protected CommercialField(Hex coords)
    {
        super(coords);
    }
}
