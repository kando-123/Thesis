package ge.field;

import ge.utilities.Hex;

/**
 *
 * @author Kay Jay O'Nail
 */
public class VillageField extends CommercialField
{
    public static final int INCOME = 25;
    
    public VillageField(Hex coords)
    {
        super(coords);
    }

    @Override
    public BuildingType type()
    {
        return BuildingType.VILLAGE;
    }

    @Override
    public int getIncome()
    {
        return INCOME;
    }
}
