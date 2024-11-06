package ge.main;

import ge.field.*;
import ge.utilities.*;

/**
 *
 * @author Kay Jay O'Nail
 */
public class MoveCommand extends Command<GameplayManager>
{
    private final Field origin;
    private final Field target;

    public MoveCommand(Field origin, Field target)
    {
        this.origin = origin;
        this.target = target;
    }
    
    @Override
    public void execute(GameplayManager executor)
    {
        executor.move(origin, target);
    }
}
