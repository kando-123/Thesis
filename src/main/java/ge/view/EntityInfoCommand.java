package ge.view;

import ge.entity.*;
import ge.utilities.*;

/**
 *
 * @author Kay Jay O'Nail
 */
public class EntityInfoCommand extends Command<ViewManager>
{
    private final EntityType entity;

    public EntityInfoCommand(EntityType entity)
    {
        this.entity = entity;
    }
    
    @Override
    public void execute(ViewManager executor)
    {
        executor.showEntityInfo(entity);
    }
}
