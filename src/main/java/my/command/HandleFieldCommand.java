package my.command;

import my.game.Manager;
import my.field.Field;

/**
 *
 * @author Kay Jay O'Nail
 */
public class HandleFieldCommand extends ManagerCommand
{
    private final Field field;
    
    public HandleFieldCommand(Field field)
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
