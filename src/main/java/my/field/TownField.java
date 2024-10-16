package my.field;

import my.player.UnaryPredicate;
import my.utils.Hex;
import my.world.WorldAccessor;

/**
 *
 * @author Kay Jay O'Nail
 */
public class TownField extends CommercialField
{
    private static final int INCOME = 150;
    
    public TownField()
    {
        super(FieldType.TOWN);
        
        priceIntercept = 500;
        priceSlope = 50;
    }

    @Override
    public String getDescription()
    {
        return String.format("Town brings extra %d Ħ after every round.", INCOME);
    }

    @Override
    public String getCondition()
    {
        return "To build a town, you need a continental field.";
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
