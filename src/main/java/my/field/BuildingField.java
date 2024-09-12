package my.field;

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
    
    public int computePrice(int ordinal)
    {
        return priceSlope * ordinal + priceIntercept;
    }
}
