package my.command;

import my.field.BuildingField;
import my.main.Manager;

/**
 *
 * @author Kay Jay O'Nail
 */
public class PursueBuildingCommand extends Command<Manager>
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
}
