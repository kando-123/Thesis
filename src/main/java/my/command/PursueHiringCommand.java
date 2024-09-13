package my.command;

import my.entity.AbstractEntity;
import my.main.Manager;

/**
 *
 * @author Kay Jay O'Nail
 */
public class PursueHiringCommand extends Command<Manager>
{
    private final AbstractEntity entity;
    
    public PursueHiringCommand(AbstractEntity entity)
    {
        this.entity = entity;
    }
    
    @Override
    public void execute(Manager manager)
    {
        manager.pursueHiring(entity);
    }  
}
