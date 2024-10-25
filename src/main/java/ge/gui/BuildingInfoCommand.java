package ge.gui;

import ge.field.*;
import ge.utilities.*;

/**
 *
 * @author Kay Jay O'Nail
 */
public class BuildingInfoCommand extends Command<GUIManager>
{
    private final BuildingType building;

    public BuildingInfoCommand(BuildingType building)
    {
        this.building = building;
    }
    
    @Override
    public void execute(GUIManager executor)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
