package my.field;

/**
 *
 * @author Kay Jay O'Nail
 */
public class MineField extends CommercialField
{
    protected MineField()
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
        return "You need a mountain field to build a mine.";
    }
}
