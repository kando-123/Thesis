package ge.entity;

import ge.field.*;
import ge.player.*;

/**
 *
 * @author Kay Jay O'Nail
 */
public class InfantryEntity extends Entity
{
    public InfantryEntity(Player owner, int number)
    {
        super(owner, number);
    }

    @Override
    public boolean canMerge(Entity entity)
    {
        return super.canMerge(entity) && entity instanceof InfantryEntity;
    }

    @Override
    protected boolean canAccess(Field field)
    {
        assert (field != null);

        if (field instanceof SeaField)
        {
            return field.isOccupied() && field.getEntity().canMerge(this);
        }
        else
        {
            if (field.isOccupied())
            {
                var entity = field.getEntity();
                if (isFellow(entity))
                {
                    return entity.canMerge(this);
                }
                else
                {
                    return true;
                }
            }
            else
            {
                return true;
            }
        }
    }

    @Override
    protected boolean canTransit(Field field)
    {
        assert (field != null);
        
        if (field instanceof SeaField)
        {
            return false;
        }
        else if (field instanceof Fortification)
        {
            return isFellow(field) && !field.isOccupied();
        }
        else
        {
            return !field.isOccupied();
        }
    }

    private static final int RADIUS = 2;
    
    @Override
    protected int radius()
    {
        return RADIUS;
    }

    @Override
    public EntityType getExtractedType()
    {
        return EntityType.INFANTRY;
    }
}
