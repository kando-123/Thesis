package my.command;

import my.entity.AbstractEntity;
import my.main.Manager;

/**
 *
 * @author Kay Jay O'Nail
 */
public class EntityInfoCommand extends Command
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
