package ge.view;

import ge.field.*;
import ge.utilities.*;

/**
 *
 * @author Kay Jay O'Nail
 */
public class BuildingInfoCommand extends Command<ViewManager>
{
    private final BuildingType building;

    public BuildingInfoCommand(BuildingType building)
    {
        this.building = building;
    }
    
    @Override
    public void execute(ViewManager executor)
    {
        executor.showBuildingInfo(building);
    }
}
