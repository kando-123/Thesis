package ge.main;

import ge.field.*;
import ge.player.*;
import ge.utilities.*;

/**
 *
 * @author Kay Jay O'Nail
 */
public class MarkForBuildingCommand extends Command<GameplayManager>
{
    private final boolean value;
    private final Player player;
    private final BuildingType building;

    public MarkForBuildingCommand(boolean value, Player player, BuildingType building)
    {
        this.value = value;
        this.player = player;
        this.building = building;
    }
    
    @Override
    public void execute(GameplayManager executor)
    {
        executor.markForBuilding(value, player, building);
    }
}
