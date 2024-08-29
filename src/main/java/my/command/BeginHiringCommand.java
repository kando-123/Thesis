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
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void undo(Manager manager)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
