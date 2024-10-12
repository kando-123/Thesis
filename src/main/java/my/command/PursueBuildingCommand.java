package my.command;

import my.flow.Manager;

/**
 *
 * @author Kay Jay O'Nail
 */
public class PursueBuildingCommand extends Command<Manager>
{
    @Override
    public void execute(Manager executor)
    {
        executor.pursueBuilding();
    }
}
