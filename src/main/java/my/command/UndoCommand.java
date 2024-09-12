package my.command;

import my.main.Manager;

/**
 *
 * @author Kay Jay O'Nail
 */
public class UndoCommand extends Command
{
    @Override
    public void execute(Manager manager)
    {
        manager.undo();
    }
}
