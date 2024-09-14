package my.field;

import my.player.UnaryPredicate;
import my.utils.Hex;
import my.world.WorldAccessor;

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
    
    public abstract String getDescription();
    public abstract String getCondition();
    
    protected int priceIntercept;
    protected int priceSlope;
    
    public int computePrice(int ordinal)
    {
        return priceSlope * ordinal + priceIntercept;
    }
    
    public int getPriceIntercept()
    {
        return priceIntercept;
    }
    
    public int getPriceSlope()
    {
        return priceSlope;
    }
    
    public abstract UnaryPredicate<Hex> getPredicate(WorldAccessor accessor);
}
