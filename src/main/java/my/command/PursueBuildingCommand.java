package my.command;

import my.field.BuildingField;
import my.game.Manager;
import my.field.FieldType;

/**
 *
 * @author Kay Jay O'Nail
 */
public class PursueBuildingCommand extends ManagerCommand
{
    private final BuildingField building;

    public PursueBuildingCommand(BuildingField building)
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
