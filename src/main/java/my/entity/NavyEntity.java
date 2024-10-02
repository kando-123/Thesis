package my.entity;

import my.field.AbstractField;
import my.world.WorldAccessor;

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
    public AbstractEntity extract(int extractedNumber) throws OutOfRangeException
    {
        if (extractedNumber < 1 || extractedNumber >= number)
        {
            throw new OutOfRangeException();
        }

        int extractedMorale = (int) ((double) extractedNumber / (double) number * (double) morale);

        AbstractEntity extractedEntity = new InfantryEntity();
        extractedEntity.number = extractedNumber;
        extractedEntity.morale = extractedMorale;

        number -= extractedNumber;
        morale -= extractedMorale;

        return extractedEntity;
    
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
                accessibility = !field.isFellow(place); // The enemy is there. (MILITATION scenario)
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

    @Override
    public boolean canExtract(WorldAccessor accessor)
    {
        if (movable && number > MINIMAL_NUMBER)
        {
            for (var neighborHex : field.getHex().neighbors())
            {
                var neighborField = accessor.getFieldAt(neighborHex);
                if (neighborField != null && !neighborField.isMarine())
                {
                    return true;
                }
            }
            return false;
        }
        else
        {
            return false;
        }
    }
}
