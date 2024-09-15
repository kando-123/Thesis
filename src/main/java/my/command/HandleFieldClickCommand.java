package my.command;

import my.flow.Manager;
import my.field.AbstractField;

/**
 *
 * @author Kay Jay O'Nail
 */
public class HandleFieldClickCommand extends Command<Manager>
{
    private final AbstractField field;
    
    public HandleFieldClickCommand(AbstractField field)
    {
        this.field = field;
    }
    
    @Override
    public void execute(Manager manager)
    {
        manager.handleFieldClick(field);
    }
}
