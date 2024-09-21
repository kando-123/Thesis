package my.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import my.field.AbstractField;
import my.utils.Hex;
import my.world.WorldAccessor;

/**
 *
 * @author Kay Jay O'Nail
 */
public class NavyEntity extends AbstractEntity
{
    private static final int RADIUS = 4;
    
    public NavyEntity()
    {
        super(EntityType.NAVY);
        
        priceIntercept = 130;
        priceSlope = 20;
    }

    @Override
    public String getDescription()
    {
        return "Navy is the marine entity. It can transport Infantry onboard.";
    }

    @Override
    public String getCondition()
    {
        return "To spawn Navy, you need Shipyard or Capital. (No entity must be there.)";
    }
    
    @Override
    public String getPricing()
    {
        return String.format("A troop of Navy costs %d Ħ for the ship + %d Ħ × number of soldiers.",
                priceIntercept, priceSlope);
    }

    private boolean isAccessible(AbstractField place)
    {
        boolean accessibility = false;
        
        if (place.isMarine())
        {
            if (!place.hasEntity()) // An unoccupied field is accessible. (MOVE scenario)
            {
                accessibility = true;
            }
            else // The field is occupied. By whose forces?
            {
                accessibility = place.getOwner() != field.getOwner(); // The enemy is there. (MILITATION scenario)
            }
        }
        
        return accessibility;
    }

    private boolean isTransitable(AbstractField place)
    {
        return place.isMarine() && !place.hasEntity() && field.getHex().distance(place.getHex()) < RADIUS;
    }

    @Override
    public Map<Hex, List<Hex>> getMovementRange(WorldAccessor accessor)
    {
        Map<Hex, List<Hex>> range = new HashMap<>();
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
                List<Hex> oldPath = range.get(current), newPath = new ArrayList<>();
                if (oldPath != null)
                {
                    newPath.addAll(oldPath);
                    newPath.add(current);
                }
                
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
                            range.put(neighborHex, newPath);
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
