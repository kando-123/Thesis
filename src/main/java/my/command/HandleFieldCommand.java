package my.command;

import my.main.Manager;
import my.field.AbstractField;

/**
 *
 * @author Kay Jay O'Nail
 */
public class HandleFieldCommand extends Command<Manager>
{
    private final AbstractField field;
    
    public HandleFieldCommand(AbstractField field)
    {
        this.field = field;
    }
    
    @Override
    public void execute(Manager manager)
    {
        manager.handleField(field);
    }
}
