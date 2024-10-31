package ge.field;

import ge.utilities.Hex;

/**
 *
 * @author Kay Jay O'Nail
 */
public class MineField extends CommercialField
{
    public static final int INCOME = 100;
    
    public MineField(Hex coords)
    {
        super(coords);
    }

    @Override
    public BuildingType getType()
    {
        return BuildingType.MINE;
    }

    @Override
    public int getIncome()
    {
        return INCOME;
    }
}
