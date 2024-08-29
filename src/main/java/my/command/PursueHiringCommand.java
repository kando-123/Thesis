package my.command;

import my.game.Manager;

/**
 *
 * @author Kay Jay O'Nail
 */
public class PursueHiringCommand extends ManagerCommand
{
    @Override
    public void execute(Manager manager)
    {
        manager.pursueHiring();
    }

    @Override
    public void undo(Manager manager)
    {
        
    }    
}
