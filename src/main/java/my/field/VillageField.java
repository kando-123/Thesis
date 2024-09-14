package my.field;

import my.player.UnaryPredicate;
import my.utils.Hex;
import my.world.WorldAccessor;

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
}
