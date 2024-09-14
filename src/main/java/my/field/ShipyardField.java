package my.field;

import my.entity.AbstractEntity;
import my.player.UnaryPredicate;
import my.utils.Hex;
import my.world.WorldAccessor;

/**
 *
 * @author Kay Jay O'Nail
 */
public class ShipyardField extends SpawnerField
{
    public ShipyardField()
    {
        super(FieldType.SHIPYARD);

        priceIntercept = 600;
        priceSlope = 75;
    }

    @Override
    public String getDescription()
    {
        return "Shipyard is where you can place the new Navy ships.";
    }

    @Override
    public String getCondition()
    {
        return "To build a shipyard, you need a plains field adjacent to a see field.";
    }

    @Override
    public boolean canSpawn(AbstractEntity entity)
    {
        return isFree() && switch (entity.getType())
        {
            case NAVY ->
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
            if (field.isPlains())
            {
                for (var neighbor : item.neighbors())
                {
                    field = accessor.getFieldAt(neighbor);
                    if (field != null && field.isMarine())
                    {
                        return true;
                    }
                }
            }
            return false;
        };
    }
}
