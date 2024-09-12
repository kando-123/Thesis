package my.command;

import my.main.Manager;

/**
 *
 * @author Kay Jay O'Nail
 */
public abstract class ManagerCommand
{
    public abstract void execute(Manager manager);
    public abstract void undo(Manager manager);
}
