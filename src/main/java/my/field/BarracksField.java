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
        return "Barracks is where you can place the new Infantry and Cavalry you hire.";
    }

    @Override
    public String getCondition()
    {
        return "To build barracks, you need a plains field.";
    }
}
