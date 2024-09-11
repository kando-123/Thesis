package my.field;

/**
 *
 * @author Kay Jay O'Nail
 */
public class FortressField extends BuildingField
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
        return "You need a continental field to build a fortress.";
    }
}
