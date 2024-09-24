package my.entity;

import my.field.AbstractField;

/**
 *
 * @author Kay Jay O'Nail
 */
public class CavalryEntity extends AbstractEntity
{
    public CavalryEntity()
    {
        super(EntityType.CAVALRY);
        
        priceIntercept = 0;
        priceSlope = 25;
        
        RADIUS = 4;
    }

    @Override
    public String getDescription()
    {
        return "Cavalry is a quick land entity. It passes plains faster than Infantry. "
                + "It is, however, slower in mountains. It cannot embark ships.";
    }

    @Override
    public String getCondition()
    {
        return "To spawn Cavalry, you need Barracks or Capital. (No entity must be there.)";
    }
    
    @Override
    public String getPricing()
    {
        return String.format("A troop of Cavalry costs %d Ħ × number of soldiers.",
                priceSlope);
    }

    @Override
    protected boolean isAccessible(AbstractField place)
    {
        boolean accessibility = false;
        
        if (!place.isMarine())
        {
            if (!place.hasEntity()) // An unoccupied field is accessible. (MOVE scenario)
            {
                accessibility = true;
            }
            else // The field is occupied. By whose forces? 
            {
                if (place.getOwner() != field.getOwner()) // The enemy is there. (MILITATION scenario)
                {
                    accessibility = true;
                }
                else // Fellow troop is there.
                {
                    AbstractEntity entity = place.getEntity();
                    
                    // If the troop is of the same type and can merge, the field is accessible. (MERGE scenario)
                    accessibility = entity.getType() == EntityType.CAVALRY && entity.canMerge();
                }
            }
        }
        
        return accessibility;
    }

    @Override
    protected boolean isTransitable(AbstractField place)
    {
        return !place.isMarine() && !place.isMountainous() && !place.hasEntity()
               && field.getHex().distance(place.getHex()) < RADIUS;
    }

    
}
