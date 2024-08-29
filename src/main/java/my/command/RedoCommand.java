package my.command;

import my.game.Manager;

/**
 *
 * @author Kay Jay O'Nail
 */
public class RedoCommand extends ManagerCommand
{
    @Override
    public void execute(Manager manager)
    {
        manager.redo();
    }

    @Override
    public void undo(Manager manager)
    {
        
    }
    
}
