package ge.view;

import ge.field.BuildingType;
import ge.utilities.Command;

/**
 *
 * @author Kay Jay O'Nail
 */
public class BeginBuildingCommand extends Command<ViewManager>
{
    private final BuildingType building;
    
    public BeginBuildingCommand(BuildingType building)
    {
        this.building = building;
    }

    @Override
    public void execute(ViewManager executor)
    {
        executor.beginBuildingProcess(building);
    }
}
