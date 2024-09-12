package my.field;

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
}
