package ge.main;

import ge.field.Field;
import ge.utilities.Command;

/**
 *
 * @author Kay Jay O'Nail
 */
public class ExtractAndMoveCommand extends Command<GameplayManager>
{
    private final Field origin;
    private final Field target;
    private final int count;

    public ExtractAndMoveCommand(Field origin, Field target, int count)
    {
        this.origin = origin;
        this.target = target;
        this.count = count;
    }
    
    @Override
    public void execute(GameplayManager executor)
    {
        executor.extractAndMove(origin, target, count);
    }
}
