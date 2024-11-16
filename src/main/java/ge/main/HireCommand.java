package ge.main;

import ge.entity.*;
import ge.field.*;
import ge.utilities.*;

/**
 *
 * @author Kay Jay O'Nail
 */
public class HireCommand extends Command<GameplayManager>
{
    private final Spawner spawner;
    private final Entity entity;

    public HireCommand(Spawner spawner, Entity entity)
    {
        this.spawner = spawner;
        this.entity = entity;
    }
    
    @Override
    public void execute(GameplayManager executor)
    {
        executor.hire(spawner, entity);
    }
}
