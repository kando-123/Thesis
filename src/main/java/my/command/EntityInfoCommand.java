package my.command;

import my.unit.AbstractEntity;
import my.flow.Manager;

/**
 *
 * @author Kay Jay O'Nail
 */
public class EntityInfoCommand extends Command<Manager>
{
    private final AbstractEntity entity;
    
    public EntityInfoCommand(AbstractEntity entity)
    {
        this.entity = entity;
    }
    
    @Override
    public void execute(Manager manager)
    {
        manager.showEntityInfo(entity);
    }
}
