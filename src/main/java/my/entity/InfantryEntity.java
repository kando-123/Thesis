package my.entity;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import my.field.AbstractField;
import my.utils.Hex;
import my.world.WorldAccessor;

/**
 *
 * @author Kay Jay O'Nail
 */
public class InfantryEntity extends AbstractEntity
{
    private static final int RADIUS = 2;

    public InfantryEntity()
    {
        super(EntityType.INFANTRY);

        priceIntercept = 0;
        priceSlope = 20;
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

    private boolean isAccessible(AbstractField place)
    {
        boolean accessibility = false;
        if (place.isMarine()) // Sea.
        {
            AbstractEntity entity = place.getEntity();
            if (entity != null && entity.getType() == EntityType.NAVY) // Ship.
            {
                // An own ship that does have space onboard can be embarked. (EMBARK scenario)
                // A foreign ship is inaccessible.
                accessibility = place.getOwner() == field.getOwner()
                                && entity.getNumber() < AbstractEntity.MAXIMAL_NUMBER;
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
                if (place.getOwner() == field.getOwner()) // Own.
                {
                    // A troop of other type blocks movement.
                    // A field with a troop of the same type is accessible. (MERGE scenario)
                    accessibility = place.getEntity().getType() == EntityType.INFANTRY;
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

    private boolean isTransitable(AbstractField place)
    {
        return !place.isMarine() && !place.hasEntity()
               && field.getHex().distance(place.getHex()) < RADIUS;
    }

    @Override
    public Set<Hex> getMovementRange(WorldAccessor accessor)
    {
        // The set of hexagonal coordinates of the fields this entity can go to.
        Set<Hex> range = new HashSet<>();

        // The set of hexagonal coordinates of the fields whose accessibility has already
        // been settled.
        Set<Hex> visited = new HashSet<>();

        // The BFS queue.
        Queue<Hex> queue = new LinkedList<>();

        // The starting point of the algorithm.
        Hex center = field.getHex();
        queue.add(center);
        visited.add(center);

        // Do BFS.
        while (!queue.isEmpty())
        {
            Hex current = queue.remove();
            for (var neighborHex : current.neighbors())
            {
                if (visited.contains(neighborHex))
                {
                    continue;
                }
                
                AbstractField neighborField = accessor.getFieldAt(neighborHex);
                if (neighborField != null)
                {
                    // This field has already been considered. No need to come back later.
                    visited.add(neighborHex);
                    if (isAccessible(neighborField))
                    {
                        // This field is accessible.
                        range.add(neighborHex);
                        if (isTransitable(neighborField))
                        {
                            // This field can be passed through, so its neighbors can,
                            // potentially, be reached. Push to the queue for later
                            // examination.
                            queue.add(neighborHex);
                        }
                    }
                }
            }
        }

        return range;
    }
}
