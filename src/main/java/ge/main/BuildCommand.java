package ge.main;

import ge.field.*;
import ge.utilities.*;

/**
 *
 * @author Kay Jay O'Nail
 */
public class BuildCommand extends Command<GameplayManager>
{
    private final BuildingField field;

    public BuildCommand(BuildingField field)
    {
        this.field = field;
    }
    
    @Override
    public void execute(GameplayManager executor)
    {
        executor.build(field);
    }
}
