package my.entity;

import my.field.AbstractField;
import my.field.FieldType;
import my.world.WorldAccessor;

/**
 *
 * @author Kay Jay O'Nail
 */
public class InfantryEntity extends AbstractEntity
{
    private static final int INITIAL_RADIUS = 2;

    public InfantryEntity()
    {
        super(EntityType.INFANTRY);

        priceIntercept = 0;
        priceSlope = 20;
        
        radius = INITIAL_RADIUS;
    }

    @Override
    public String getDescription()
    {
        return "Infantry is a slow land entity. It can, however, travel aboard ships. "
               + "It also passes mountains quicker than Cavalry.";
    }

    @Override
    public String getCondition()
    {
        return "To spawn Infantry, you need Barracks or Capital. (No entity must be there.)";
    }

    @Override
    public String getPricing()
    {
        return String.format("A troop of Infantry costs %d Ħ × number of soldiers.",
                priceSlope);
    }

    @Override
    protected boolean isAccessible(AbstractField place)
    {
        boolean accessibility = false;
        if (place.isMarine() || place.getType() == FieldType.SHIPYARD) // Sea or Shipyard.
        {
            AbstractEntity entity = place.getEntity();
            if (entity != null && entity.getType() == EntityType.NAVY) // Ship.
            {
                // An own ship that does have space onboard can be embarked. (EMBARK scenario)
                // A foreign ship is inaccessible.
                accessibility = entity.canMerge(this);
            }
        }
        else // Land.
        {
            if (!place.hasEntity()) // Unoccupied.
            {
                // An unoccupied land field is accessible. (MOVE scenario)
                accessibility = true;
            }
            else // Occupied.
            {
                if (field.isFellow(place)) // Fellow.
                {
                    // A troop of other type blocks movement.
                    // A field with a troop of the same type is accessible,
                    // on condition that merging is possible. (MERGE scenario)
                    AbstractEntity entity = place.getEntity();
                    accessibility = entity.canMerge(this);
                }
                else
                {
                    // A field with an enemy's troop is accessible. (MILITATION scenario)
                    accessibility = true;
                }
            }
        }
        return accessibility;
    }

    @Override
    protected boolean isTransitable(AbstractField place)
    {
        return !place.isMarine()
               && !place.isFortification()
               && !place.hasEntity()
               && field.getHex().distance(place.getHex()) < radius;
    }

    @Override
    public boolean canExtract() throws AccessorIsNeededException
    {
        return movable && number > MINIMAL_NUMBER;
    }

    @Override
    public boolean canExtract(WorldAccessor accessor)
    {
        return movable && number > MINIMAL_NUMBER;
    }
}