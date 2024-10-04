package my.command;

/*import my.field.BuildingField;*/
import my.flow.Manager;

/**
 *
 * @author Kay Jay O'Nail
 */
public class PursueBuildingCommand extends Command<Manager>
{
    @Override
    public void execute(Manager manager)
    {
        manager.pursueBuilding();
    }
}
