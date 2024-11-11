package ge.field;

import ge.entity.*;
import ge.utilities.*;

/**
 *
 * @author Kay Jay O'Nail
 */
public abstract class FortificationField extends BuildingField implements Fortification
{
    protected FortificationField(Hex coords)
    {
        super(coords);
    }

    @Override
    public Entity placeEntity(Entity comer)
    {
        Entity remainder = null;
        if (!isOwned())
        {
            /* Move. */
            entity = comer;
            owner = comer.getOwner();
        }
        else if (!isOccupied())
        {
            if (isFellow(comer))
            {
                /* Move. */
                entity = comer;
                owner = comer.getOwner();
            }
            else
            {
                /* Defend. */
                
                // If the comer is stronger than this
                
                throw new UnsupportedOperationException();
            }
        }
        else
        {
            if (isFellow(comer))
            {
                /* Merge. */
                remainder = entity.merge(comer);
            }
            else
            {
                /* Militate. */
                if (entity.strength() + getFortitude() < comer.strength())
                {
                    /* Lose. */
                    throw new UnsupportedOperationException();
                }
                else
                {
                    /* Win. */
                    throw new UnsupportedOperationException();
                }
            }
        }
        return remainder;
    }
}
