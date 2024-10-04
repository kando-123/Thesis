package my.flow;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import my.entity.AbstractEntity;
import my.entity.EntityType;
import my.field.AbstractField;
import my.player.Player;
import my.utils.Hex;
import my.world.WorldAccessor;
import my.world.WorldMarker;

/**
 *
 * @author Kay Jay O'Nail
 */
public class MovementManager
{
    private final WorldMarker marker;
    private final WorldAccessor accessor;

    private AbstractEntity entity;
    private Map<Hex, List<Hex>> range;

    public MovementManager(WorldMarker marker, WorldAccessor accessor)
    {
        this.marker = marker;
        this.accessor = accessor;
    }

    public void begin(AbstractEntity entity)
    {
        this.entity = entity;
        entity.mark();

        range = entity.getMovementRange(accessor);
        for (var hex : range.keySet())
        {
            marker.mark(hex);
        }
    }

    public void finish(AbstractField field, Player player)
    {
        entity.unmark();
        if (field != null && marker.isMarked(field.getHex()))
        {
            AbstractField origin = entity.getField();
            AbstractEntity resultant = field.interact(entity);

            if (resultant != null)
            {
                if (resultant.getType() != EntityType.NAVY)
                {
                    List<Hex> path = range.get(field.getHex());

                    if (field.isOwned(player))
                    {
                        path.add(field.getHex());
                    }

                    Set<Hex> way = makeWay(path, player);
                    
                    for (var hex : way)
                    {
                        AbstractField passedField = accessor.getFieldAt(hex);
                        player.capture(passedField);
                    }
                }
                else
                {
                    if (origin.isMarine())
                    {
                        player.release(origin);
                    }
                    player.capture(field);
                }
            }
        }
        entity = null;
        marker.unmarkAll();
    }

    private Set<Hex> makeWay(List<Hex> path, Player player)
    {
        Set<Hex> way = new HashSet<>();
        
        for (int i = path.size() - 1; i >= 0; --i)
        {
            Hex hex = path.get(i);
            way.add(hex);

            if (((path.size() - 1 - i) & 1) == 0)
            {
                for (var neighborHex : hex.neighbors())
                {
                    var neighborField = accessor.getFieldAt(neighborHex);
                    if (neighborField != null
                            && neighborField.isPlains()
                            && !neighborField.hasEntity()
                            && neighborField.getOwner() != player)
                    {
                        way.add(neighborHex);
                    }
                }
            }
        }
        
        return way;
    }
}
