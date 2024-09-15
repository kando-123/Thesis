package my.command;

import my.flow.Manager;

/**
 *
 * @author Kay Jay O'Nail
 */
public abstract class ReversibleCommand extends Command
{
    public abstract void undo(Manager manager);
    
    @Override
    final public boolean isReversible()
    {
        return true;
    }
}
