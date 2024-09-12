package my.command;

import my.main.Manager;

/**
 *
 * @author Kay Jay O'Nail
 */
public class NextPlayerCommand extends Command
{
    @Override
    public void execute(Manager manager)
    {
        manager.nextPlayer();
    }
}
