package my.command;

import my.entity.AbstractEntity;
import my.main.Manager;

/**
 *
 * @author Kay Jay O'Nail
 */
public class BeginHiringCommand extends Command<Manager>
{
    private final AbstractEntity entity;
    
    public BeginHiringCommand(AbstractEntity entity)
    {
        this.entity = entity;
    }
    
    @Override
    public void execute(Manager manager)
    {
        manager.beginHiring(entity);
    }
}
