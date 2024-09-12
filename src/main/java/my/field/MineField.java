package my.field;

/**
 *
 * @author Kay Jay O'Nail
 */
public class MineField extends CommercialField
{
    public MineField()
    {
        super(FieldType.MINE);
        
        priceIntercept = 200;
        priceSlope = 50;
    }

    @Override
    public String getDescription()
    {
        return "Mine brings extra HexCoins after every round.";
    }

    @Override
    public String getCondition()
    {
        return "To build a mine, you need a mountain field.";
    }
}
