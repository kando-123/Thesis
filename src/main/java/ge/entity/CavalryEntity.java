package ge.entity;

import ge.field.*;
import ge.player.*;

/**
 *
 * @author Kay Jay O'Nail
 */
public class CavalryEntity extends Entity
{
    public CavalryEntity(Player owner, int number)
    {
        super(owner, number);
    }

    @Override
    public boolean canMerge(Entity entity)
    {
        return super.canMerge(entity) && entity instanceof CavalryEntity;
    }

    @Override
    protected boolean canAccess(Field field)
    {
        assert (field != null);
        
        if (field instanceof SeaField)
        {
            return false;
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
        
        if (field instanceof SeaField || field instanceof MountainsField)
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

    private static final int RADIUS = 4;
    
    @Override
    protected int radius()
    {
        return RADIUS;
    }

    @Override
    public EntityType getExtractedType()
    {
        return EntityType.CAVALRY;
    }

    @Override
    public EntityType type()
    {
        return EntityType.CAVALRY;
    }
}
