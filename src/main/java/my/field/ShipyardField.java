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
        return "Shipyard is where you can place the new ships.";
    }

    @Override
    public String getCondition()
    {
        return "You need a land field that is adjacent to a see field to build a shipyard.";
    }
}
