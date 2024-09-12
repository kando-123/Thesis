package my.command;

import my.entity.AbstractEntity;
import my.game.Manager;

/**
 *
 * @author Kay Jay O'Nail
 */
public class PursueHiringCommand extends ManagerCommand
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

    @Override
    public void undo(Manager manager)
    {
        
    }    
}
