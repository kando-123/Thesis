package my.field;

import my.player.UnaryPredicate;
import my.utils.Hex;
import my.world.WorldAccessor;

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

    @Override
    public UnaryPredicate<Hex> getPredicate(WorldAccessor accessor)
    {
        return (Hex item) ->
        {
            var field = accessor.getFieldAt(item);
            return field != null && field.isMountainous();
        };
    }
}
