package ge.entity;

import ge.field.*;
import ge.player.*;

/**
 *
 * @author Kay Jay O'Nail
 */
public class NavyEntity extends Entity
{
    public NavyEntity(Player owner, int number)
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
        if (field instanceof SeaField)
        {
            return !(field.isOccupied() && field.getEntity().isFellow(this));
        }
        else
        {
            return false;
        }
    }

    @Override
    protected boolean canTransit(Field field)
    {
        return field instanceof SeaField && !field.isOccupied();
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
        return EntityType.INFANTRY;
    }

    @Override
    public EntityType type()
    {
        return EntityType.NAVY;
    }
}
