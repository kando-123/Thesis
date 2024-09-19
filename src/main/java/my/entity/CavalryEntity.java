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
        // been settled (+ the central field, whose accessibility is not settled at all
        // but it is also 'visited').
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
                    // This field is being considered. No need to come back later.
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
