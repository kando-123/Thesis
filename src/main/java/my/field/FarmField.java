package my.field;

import my.player.UnaryPredicate;
import my.utils.Hex;
import my.world.WorldAccessor;

/**
 *
 * @author Kay Jay O'Nail
 */
public class FarmField extends CommercialField
{
    private static final int INCOME = 50;
    
    public FarmField()
    {
        super(FieldType.FARMFIELD);
        
        priceIntercept = 100;
        priceSlope = 10;
    }

    @Override
    public String getDescription()
    {
        return String.format("Farmfield brings extra %d Ħ after every round.", INCOME);
    }

    @Override
    public String getCondition()
    {
        return "To build a farmfield, you need a plains field adjacent to a village.";
    }

    @Override
    public UnaryPredicate<Hex> getPredicate(WorldAccessor accessor)
    {
        return (Hex item) ->
        {
            var field = accessor.getFieldAt(item);
            if (field.isPlains())
            {
                for (var neighbor : item.neighbors())
                {
                    field = accessor.getFieldAt(neighbor);
                    if (field != null && field.getType() == FieldType.VILLAGE)
                    {
                        return true;
                    }
                }
            }
            return false;
        };
    }

    @Override
    public int getIncome()
    {
        return INCOME;
    }
}
