package my.command;

import my.main.Manager;

/**
 *
 * @author Kay Jay O'Nail
 */
public abstract class Command
{
    public abstract void execute(Manager manager);
    
    public boolean isReversible()
    {
        return false;
    }
}
