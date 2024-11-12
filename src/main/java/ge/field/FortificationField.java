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
    
    protected abstract void subtractFortitude(int newFortitude);

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
            }
            else
            {
                /* Defend. */
                int attack = comer.strength();
                int fortitude = getFortitude();
                subtractFortitude(attack);
                
                if (attack > fortitude)
                {
                    // If the comer is stronger than this,
                    // the fortification is damaged and the comer conquers this field.
                    comer.defeat(fortitude);
                    entity = comer;
                    owner = entity.getOwner();
                }
                // else: Just subtract the attack (done); the comer perishes.
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
                final int defense = entity.strength();
                final int fortitude = getFortitude();
                final int attack = comer.strength();
                
                int fortitudeLoss = (int) ((double) fortitude / (fortitude + defense) * attack);
                subtractFortitude(fortitudeLoss);
                
                if (defense + fortitude > attack)
                {
                    /* Victory. */
                    entity.defeat(attack - fortitudeLoss);
                }
                else
                {
                    /* Loss. */
                    comer.defeat(defense + fortitude);
                    entity = comer;
                    owner = comer.getOwner();
                }
            }
        }
        
        entity.setMovable(false);
        
        return remainder;
    }
}
