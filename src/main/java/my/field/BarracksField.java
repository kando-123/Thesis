package my.field;

import my.entity.AbstractEntity;
import my.player.UnaryPredicate;
import my.utils.Hex;
import my.world.WorldAccessor;

/**
 *
 * @author Kay Jay O'Nail
 */
public class BarracksField extends SpawnerField
{
    public BarracksField()
    {
        super(FieldType.BARRACKS);
        
        priceIntercept = 250;
        priceSlope = 25;
    }

    @Override
    public String getDescription()
    {
        return "Barracks is where you can place the new Infantry and Cavalry you hire.";
    }

    @Override
    public String getCondition()
    {
        return "To build barracks, you need a plains field.";
    }
    
    @Override
    public boolean canSpawn(AbstractEntity entity)
    {
        return !hasEntity() && switch (entity.getType())
        {
            case INFANTRY, CAVALRY ->
            {
                yield true;
            }
            default ->
            {
                yield false;
            }
        };
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
