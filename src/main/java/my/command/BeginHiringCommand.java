package my.command;

import my.game.Manager;

/**
 *
 * @author Kay Jay O'Nail
 */
public class BeginHiringCommand extends ManagerCommand
{
    @Override
    public void execute(Manager manager)
    {
        manager.beginHiring();
    }

    @Override
    public void undo(Manager manager)
    {
        
    }
    
}
