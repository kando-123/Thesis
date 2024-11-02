package ge.main;

import ge.field.*;
import ge.utilities.*;

/**
 *
 * @author Kay Jay O'Nail
 */
public class MarkCommand extends Command<GameplayManager>
{
    private final boolean value;
    private final UnaryPredicate predicate;

    public MarkCommand(boolean value, UnaryPredicate<Field> predicate)
    {
        this.value = value;
        this.predicate = predicate;
    }
    
    @Override
    public void execute(GameplayManager executor)
    {
        executor.mark(value, predicate);
    }
    
}
