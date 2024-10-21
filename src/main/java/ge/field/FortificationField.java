package ge.field;

import ge.utilities.Hex;


/**
 *
 * @author Kay Jay O'Nail
 */
public class FortificationField extends BuildingField implements Fortification
{
    protected FortificationField(Hex coords)
    {
        super(coords);
    }
    
}
