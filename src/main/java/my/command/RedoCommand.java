package my.command;

import my.main.Manager;

/**
 *
 * @author Kay Jay O'Nail
 */
public class RedoCommand extends Command<Manager>
{
    @Override
    public void execute(Manager manager)
    {
        manager.redo();
    }
}