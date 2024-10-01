package my.command;

import my.flow.Manager;

/**
 *
 * @author Kay Jay O'Nail
 */
public abstract class ReversibleCommand extends Command<Manager>
{
    public abstract void undo(Manager manager);
}
