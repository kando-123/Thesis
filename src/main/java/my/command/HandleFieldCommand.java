package my.command;

import my.game.Manager;
import my.field.AbstractField;

/**
 *
 * @author Kay Jay O'Nail
 */
public class HandleFieldCommand extends ManagerCommand
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

    @Override
    public void undo(Manager manager)
    {
        
    }
}
