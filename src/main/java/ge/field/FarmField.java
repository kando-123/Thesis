package ge.field;

import ge.utilities.Hex;

/**
 *
 * @author Kay Jay O'Nail
 */
public class FarmField extends CommercialField
{
    public static final int INCOME = 50;
    
    public FarmField(Hex coords)
    {
        super(coords);
    }

    @Override
    public BuildingType type()
    {
        return BuildingType.FARM;
    }

    @Override
    public int getIncome()
    {
        return INCOME;
    }
}
