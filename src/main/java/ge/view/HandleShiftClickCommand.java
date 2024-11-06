package ge.view;

import ge.field.Field;
import ge.utilities.*;

/**
 *
 * @author Kay Jay O'Nail
 */
public class HandleShiftClickCommand extends Command<ViewManager>
{
    private final Field field;

    public HandleShiftClickCommand(Field field)
    {
        this.field = field;
    }
    
    @Override
    public void execute(ViewManager executor)
    {
        executor.handleShiftClick(field);
    }
}
