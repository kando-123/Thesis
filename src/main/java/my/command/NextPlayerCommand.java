package my.command;

import my.main.Manager;

/**
 *
 * @author Kay Jay O'Nail
 */
public class NextPlayerCommand extends ManagerCommand
{
    @Override
    public void execute(Manager manager)
    {
        manager.nextPlayer();
    }

    @Override
    public void undo(Manager manager)
    {
        
    }
    
}
