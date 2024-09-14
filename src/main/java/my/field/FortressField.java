package my.field;

import my.player.UnaryPredicate;
import my.utils.Hex;
import my.world.WorldAccessor;

/**
 *
 * @author Kay Jay O'Nail
 */
public class FortressField extends DefenseField
{
    public FortressField()
    {
        super(FieldType.FORTRESS);
        
        priceIntercept = 400;
        priceSlope = 100;
    }

    @Override
    public String getDescription()
    {
        return "Fortress gives additional defence points.";
    }

    @Override
    public String getCondition()
    {
        return "To build a fortress, you need a continental field.";
    }

    @Override
    public UnaryPredicate<Hex> getPredicate(WorldAccessor accessor)
    {
        return (Hex item) ->
        {
            var field = accessor.getFieldAt(item);
            return field != null && field.isContinental();
        };
    }
}
