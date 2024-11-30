package ge.player.action;

import ge.entity.Entity;
import ge.field.Spawner;
import ge.main.*;
import ge.utilities.*;

/**
 *
 * @author Kay Jay O'Nail
 */
public class HireAction extends Action<GameplayManager>
{
    private final Spawner spawner;
    private final Entity entity;

    public HireAction(Spawner spawner, Entity entity)
    {
        this.spawner = spawner;
        this.entity = entity;
    }
    
    @Override
    public void perform(Invoker<GameplayManager> invoker)
    {
        invoker.invoke(new HireCommand(spawner, entity));
    }
}
