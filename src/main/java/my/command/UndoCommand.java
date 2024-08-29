package my.command;

import my.game.Manager;

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
