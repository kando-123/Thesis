package my.field;

import my.utils.IntegerLinearFunction;

/**
 *
 * @author Kay Jay O'Nail
 */
public abstract class BuildingField extends AbstractField
{
    protected BuildingField(FieldType type)
    {
        super(type);
    }
    
    @Override
    final public boolean isBuilding()
    {
        return true;
    }
    
    public abstract String getDescription();
    public abstract String getCondition();
    
    protected int priceIntercept;
    protected int priceSlope;
    
    public IntegerLinearFunction getPriceFunction()
    {
        return new IntegerLinearFunction(priceSlope, priceIntercept);
    }
}
