package my.command;

import my.game.Manager;
import my.units.FieldType;

/**
 *
 * @author Kay Jay O'Nail
 */
public class PursueBuildingCommand extends ManagerCommand
{
    private FieldType building;

    public PursueBuildingCommand(FieldType building)
    {
        this.building = building;
    }
    
    @Override
    public void execute(Manager manager)
    {
        manager.pursueBuilding(building);
    }

    @Override
    public void undo(Manager manager)
    {
        
    }
}
