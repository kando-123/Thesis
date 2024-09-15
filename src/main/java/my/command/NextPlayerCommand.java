package my.command;

import my.flow.Manager;

/**
 *
 * @author Kay Jay O'Nail
 */
public class NextPlayerCommand extends Command<Manager>
{
    @Override
    public void execute(Manager manager)
    {
        manager.nextPlayer();
    }
}
