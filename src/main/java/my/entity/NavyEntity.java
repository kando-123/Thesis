package my.entity;

import java.util.Set;
import my.utils.Hex;
import my.world.WorldAccessor;

/**
 *
 * @author Kay Jay O'Nail
 */
public class NavyEntity extends AbstractEntity
{
    public NavyEntity()
    {
        super(EntityType.NAVY);
        
        priceIntercept = 130;
        priceSlope = 20;
    }

    @Override
    public String getDescription()
    {
        return "Navy is the marine entity. It can transport Infantry onboard.";
    }

    @Override
    public String getCondition()
    {
        return "To spawn Navy, you need Shipyard or Capital. (No entity must be there.)";
    }
    
    @Override
    public String getPricing()
    {
        return String.format("A troop of Navy costs %d Ħ for the ship + %d Ħ × number of soldiers.",
                priceIntercept, priceSlope);
    }

    @Override
    public Set<Hex> getMovementRange(WorldAccessor accessor)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
