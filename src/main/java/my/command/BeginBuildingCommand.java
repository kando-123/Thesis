package my.command;

import my.field.BuildingField;
import my.flow.Manager;

/**
 *
 * @author Kay Jay O'Nail
 */
public class BeginBuildingCommand extends Command<Manager>
{
    private final BuildingField building;
    
    public BeginBuildingCommand(BuildingField building)
    {
        this.building = building;
    }
    
    @Override
    public void execute(Manager manager)
    {
        manager.beginBuilding(building);
    }
}
