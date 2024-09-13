package my.command;

import my.field.BuildingField;
import my.main.Manager;

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
