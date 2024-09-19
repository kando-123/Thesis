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
public class CavalryEntity extends AbstractEntity
{
    private static final int RADIUS = 4;
    
    public CavalryEntity()
    {
        super(EntityType.CAVALRY);
        
        priceIntercept = 0;
        priceSlope = 25;
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

    private boolean isAccessible(AbstractField place)
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

    private boolean isTransitable(AbstractField place)
    {
        return !place.isMarine() && !place.isMountainous() && !place.hasEntity()
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
