package my.field;

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
        priceSlope = 50;
    }

    @Override
    public String getDescription()
    {
        return "Barracks are where you can place the new entities you hire.";
    }

    @Override
    public String getCondition()
    {
        return "You need a land field to build barracks.";
    }
}
