package my.command;

import my.game.Manager;

/**
 *
 * @author Kay Jay O'Nail
 */
public class PursueBuildingCommand extends ManagerCommand
{
    @Override
    public void execute(Manager manager)
    {
        manager.pursueBuilding();
    }

    @Override
    public void undo(Manager manager)
    {
        
    }
}
