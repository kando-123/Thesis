package ge.field;

import ge.utilities.Hex;

/**
 *
 * @author Kay Jay O'Nail
 */
public class TownField extends CommercialField
{
    public static final int INCOME = 150;
    
    public TownField(Hex coords)
    {
        super(coords);
    }

    @Override
    public BuildingType type()
    {
        return BuildingType.TOWN;
    }

    @Override
    public int getIncome()
    {
        return INCOME;
    }
}
