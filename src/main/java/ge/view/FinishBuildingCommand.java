package ge.view;

import ge.field.BuildingField;
import ge.utilities.Command;

/**
 *
 * @author Kay Jay O'Nail
 */
public class FinishBuildingCommand extends Command<ViewManager>
{
    private final BuildingField building;

    public FinishBuildingCommand(BuildingField building)
    {
        this.building = building;
    }
    
    @Override
    public void execute(ViewManager executor)
    {
        executor.finishBuilding(building);
    }
}
