package ge.main;

import ge.utilities.*;
import java.util.*;

/**
 *
 * @author Kay Jay O'Nail
 */
public class MarkForMovingCommand extends Command<GameplayManager>
{
    private final boolean value;
    private final Collection<Hex> range;

    public MarkForMovingCommand(boolean value, Collection<Hex> range)
    {
        this.value = value;
        this.range = range;
    }
    
    @Override
    public void execute(GameplayManager executor)
    {
        executor.markForMoving(value, range);
    }
}
