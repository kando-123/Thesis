package my.command;

import my.game.Manager;
import my.units.Field;

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
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
