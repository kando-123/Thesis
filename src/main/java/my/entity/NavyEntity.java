package my.entity;

import my.field.AbstractField;

/**
 *
 * @author Kay Jay O'Nail
 */
public class NavyEntity extends AbstractEntity
{
    private static final int INITIAL_RADIUS = 4;
    
    public NavyEntity()
    {
        super(EntityType.NAVY);
        
        priceIntercept = 130;
        priceSlope = 20;
        
        radius = INITIAL_RADIUS;
    }

    @Override
    public String getDescription()
    {
        return "Navy is the marine entity. It can transport Infantry onboard.";
    }

    @Override
    public String getCondition()
    {
        return "To spawn Navy, you need Shipyard. (No entity must be there.)";
    }
    
    @Override
    public String getPricing()
    {
        return String.format("A troop of Navy costs %d Ħ for the ship + %d Ħ × number of soldiers.",
                priceIntercept, priceSlope);
    }

    @Override
    protected boolean isAccessible(AbstractField place)
    {
        boolean accessibility = false;
        
        if (place.isMarine())
        {
            if (!place.hasEntity()) // An unoccupied field is accessible. (MOVE scenario)
            {
                accessibility = true;
            }
            else // The field is occupied. By whose forces?
            {
                accessibility = field.isFellow(place); // The enemy is there. (MILITATION scenario)
            }
        }
        
        return accessibility;
    }

    @Override
    protected boolean isTransitable(AbstractField place)
    {
        return place.isMarine() && !place.hasEntity() && field.getHex().distance(place.getHex()) < radius;
    }
    
    @Override
    public boolean canMerge(AbstractEntity entity)
    {
        return entity.getType() == EntityType.INFANTRY && getNumber() < MAXIMAL_NUMBER;
    }
}
