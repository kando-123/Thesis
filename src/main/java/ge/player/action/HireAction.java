package ge.player.action;

import ge.entity.*;
import ge.field.*;
import ge.main.*;
import ge.player.*;
import ge.utilities.*;
import java.util.*;

/**
 *
 * @author Kay Jay O'Nail
 */
public class HireAction extends Action<GameplayManager>
{
    private final EntityType type;
    private final Spawner[] spawners;
    private final int maxNumber;
    private final Player player;

    public HireAction(EntityType type, int maxNumber, Spawner[] spawners, Player player)
    {
        assert (spawners.length > 0);
        
        this.type = type;
        this.maxNumber = maxNumber;
        this.spawners = spawners;
        this.player = player;
    }

    private static final Random RANDOM = new Random();

    @Override
    public void perform(Invoker<GameplayManager> invoker)
    {
        var spawner = spawners[RANDOM.nextInt(spawners.length)];
        int number = (maxNumber == Entity.MINIMAL_NUMBER)
                ? maxNumber
                : (int) RANDOM.nextInt(Entity.MINIMAL_NUMBER, maxNumber);
        var entity = Entity.newInstance(type, player, number);
        invoker.invoke(new HireCommand(spawner, entity));
    }

    @Override
    public int weight()
    {
        final int landEntityWeight = 6;
        final int navyWeight = 1;
        return type == EntityType.NAVY ? navyWeight : landEntityWeight;
    }
}
