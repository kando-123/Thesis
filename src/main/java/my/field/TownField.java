package my.field;

/**
 *
 * @author Kay Jay O'Nail
 */
public class TownField extends CommercialField
{
    public TownField()
    {
        super(FieldType.TOWN);
        
        priceIntercept = 400;
        priceSlope = 50;
    }

    @Override
    public String getDescription()
    {
        return "Town brings extra HexCoins after every round.";
    }

    @Override
    public String getCondition()
    {
        return "You need a continental field to build a town.";
    }
}
