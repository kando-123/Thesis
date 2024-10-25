package ge.gui;

import ge.field.BuildingType;
import ge.utilities.Command;

/**
 *
 * @author Kay Jay O'Nail
 */
public class BeginBuildingCommand extends Command<GUIManager>
{
    private BuildingType building;
    
    public BeginBuildingCommand(BuildingType building)
    {
        this.building = building;
    }

    @Override
    public void execute(GUIManager executor)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
