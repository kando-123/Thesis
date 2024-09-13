package my.field;

import my.entity.AbstractEntity;

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
}
