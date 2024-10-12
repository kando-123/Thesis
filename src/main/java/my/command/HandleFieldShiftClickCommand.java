package my.command;

import my.unit.AbstractField;
import my.flow.Manager;

/**
 *
 * @author Kay Jay O'Nail
 */
public class HandleFieldShiftClickCommand extends Command<Manager>
{
    private final AbstractField field;
    
    public HandleFieldShiftClickCommand(AbstractField field)
    {
        this.field = field;
    }
    
    @Override
    public void execute(Manager manager)
    {
        manager.handleFieldShiftClick(field);
    }
}
