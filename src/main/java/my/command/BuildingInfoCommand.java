package my.command;

import my.unit.field.BuildingField;
import my.flow.Manager;

/**
 *
 * @author Kay Jay O'Nail
 */
public class BuildingInfoCommand extends Command<Manager>
{
    private final BuildingField building;
    
    public BuildingInfoCommand(BuildingField building)
    {
        this.building = building;
    }
    
    @Override
    public void execute(Manager manager)
    {
        manager.showBuildingInfo(building);
    }
}
