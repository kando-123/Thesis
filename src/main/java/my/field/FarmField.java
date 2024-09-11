package my.field;

/**
 *
 * @author Kay Jay O'Nail
 */
public class FarmField extends CommercialField
{
    public FarmField()
    {
        super(FieldType.FARMFIELD);
        
        priceIntercept = 100;
        priceSlope = 10;
    }

    @Override
    public String getDescription()
    {
        return "Farmfield brings extra HexCoins after every round.";
    }

    @Override
    public String getCondition()
    {
        return "You need a plains field adjacent to a village to build a farmfield.";
    }
}
