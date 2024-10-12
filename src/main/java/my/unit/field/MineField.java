package my.unit.field;

import my.unit.FieldType;
import my.player.UnaryPredicate;
import my.utils.Hex;
import my.world.WorldAccessor;

/**
 *
 * @author Kay Jay O'Nail
 */
public class MineField extends CommercialField
{
    private static final int INCOME = 100;
    
    public MineField()
    {
        super(FieldType.MINE);
        
        priceIntercept = 250;
        priceSlope = 25;
    }

    @Override
    public String getDescription()
    {
        return String.format("Mine brings extra %d Ħ after every round.", INCOME);
    }

    @Override
    public String getCondition()
    {
        return "To build a mine, you need a mountain field.";
    }

    @Override
    public UnaryPredicate<Hex> getPredicate(WorldAccessor accessor)
    {
        return (Hex item) ->
        {
            var field = accessor.getFieldAt(item);
            return field != null && field.isMountainous();
        };
    }

    @Override
    public int getIncome()
    {
        return INCOME;
    }
}
