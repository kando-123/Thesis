package my.unit.field;

import my.unit.FieldType;
import my.player.UnaryPredicate;
import my.utils.Hex;
import my.world.WorldAccessor;

/**
 *
 * @author Kay Jay O'Nail
 */
public class VillageField extends CommercialField
{
    private static final int INCOME = 25;
    
    public VillageField()
    {
        super(FieldType.VILLAGE);
        
        priceIntercept = 150;
        priceSlope = 15;
    }

    @Override
    public String getDescription()
    {
        return String.format("Village brings extra %d Ħ after every round.", INCOME);
    }

    @Override
    public String getCondition()
    {
        return "To build a village, you need a plains field.";
    }

    @Override
    public UnaryPredicate<Hex> getPredicate(WorldAccessor accessor)
    {
        return (Hex item) ->
        {
            var field = accessor.getFieldAt(item);
            return field != null && field.isPlains();
        };
    }

    @Override
    public int getIncome()
    {
        return INCOME;
    }
}
