package my.entity;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import my.field.AbstractField;
import my.field.FieldType;
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
        if (place.isMarine() || place.getType() == FieldType.SHIPYARD) // Sea or Shipyard.
        {
            AbstractEntity entity = place.getEntity();
            if (entity != null && entity.getType() == EntityType.NAVY) // Ship.
            {
                // An own ship that does have space onboard can be embarked. (EMBARK scenario)
                // A foreign ship is inaccessible.
                accessibility = place.getOwner() == field.getOwner() && entity.canMerge();
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
                    // A field with a troop of the same type is accessible,
                    // on condition that merging is possible. (MERGE scenario)
                    AbstractEntity entity = place.getEntity();
                    accessibility = entity.getType() == EntityType.INFANTRY && entity.canMerge();
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
        Set<Hex> range = new HashSet<>();
        Set<Hex> visited = new HashSet<>();
        Queue<Hex> queue = new LinkedList<>();

        Hex center = field.getHex();
        queue.add(center);
        visited.add(center);

        for (int i = 0; i < RADIUS; ++i)
        {
            for (int j = queue.size(); j > 0; --j)
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
                        visited.add(neighborHex);
                        if (isAccessible(neighborField))
                        {
                            range.add(neighborHex);
                            if (isTransitable(neighborField))
                            {
                                queue.add(neighborHex);
                            }
                        }
                    }
                }
            }
        }

        return range;
    }
}
