package my.field;

/**
 *
 * @author Kay Jay O'Nail
 */
public class VillageField extends CommercialField
{
    public VillageField()
    {
        super(FieldType.VILLAGE);
        
        priceIntercept = 150;
        priceSlope = 25;
    }

    @Override
    public String getDescription()
    {
        return "Village brings extra HexCoin after every round.";
    }

    @Override
    public String getCondition()
    {
        return "You need a plains field to build a village.";
    }
}
