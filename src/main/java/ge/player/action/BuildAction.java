package ge.player.action;

import ge.field.*;
import ge.main.*;
import ge.utilities.*;

/**
 *
 * @author Kay Jay O'Nail
 */
public class BuildAction extends Action<GameplayManager>
{
    private final BuildingField building;

    public BuildAction(BuildingField building)
    {
        this.building = building;
    }
    
    @Override
    public void perform(Invoker<GameplayManager> invoker)
    {
        invoker.invoke(new BuildCommand(building));
    }
}
