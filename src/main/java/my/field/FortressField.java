package my.field;

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
}
