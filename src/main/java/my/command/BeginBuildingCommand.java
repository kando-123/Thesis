package my.command;

import my.main.Manager;

/**
 *
 * @author Kay Jay O'Nail
 */
public class BeginBuildingCommand extends ManagerCommand
{
    @Override
    public void execute(Manager manager)
    {
        manager.beginBuilding();
    }

    @Override
    public void undo(Manager manager)
    {
        
    }
}
