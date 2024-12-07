package ge.player.action;

import ge.field.*;
import ge.main.*;
import ge.utilities.*;
import ge.world.WorldAccessor;
import java.util.Random;

/**
 *
 * @author Kay Jay O'Nail
 */
public class MoveAction extends Action<GameplayManager>
{
    private final Field origin;
    private final WorldAccessor accessor;

    public MoveAction(Field origin, WorldAccessor accessor)
    {
        assert (origin.isOccupied());
        
        this.origin = origin;
        this.accessor = accessor;
    }

    private static final Random RANDOM = new Random();
    
    @Override
    public void perform(Invoker<GameplayManager> invoker)
    {
        var entity = origin.getEntity();
        var range = entity.range(origin.getHex(), accessor).toArray(Hex[]::new);var hex = range[RANDOM.nextInt(range.length)];
        var field = accessor.getField(hex);
        invoker.invoke(new MoveCommand(origin, field));
    }

    @Override
    public int weight()
    {
        return 5;
    }
}
