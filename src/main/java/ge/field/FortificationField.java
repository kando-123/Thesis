package ge.field;

import ge.utilities.Hex;


/**
 *
 * @author Kay Jay O'Nail
 */
public abstract class FortificationField extends BuildingField implements Fortification
{
    protected FortificationField(Hex coords)
    {
        super(coords);
    }   
}
