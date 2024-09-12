package my.command;

import my.main.Manager;

/**
 *
 * @author Kay Jay O'Nail
 */
public class UndoCommand extends ManagerCommand
{
    @Override
    public void execute(Manager manager)
    {
        manager.undo();
    }

    @Override
    public void undo(Manager manager)
    {
        
    }
    
}
