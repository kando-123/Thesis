package my.command;

import my.game.Manager;
import my.units.EntityType;

/**
 *
 * @author Kay Jay O'Nail
 */
public class PursueHiringCommand extends ManagerCommand
{
    private final EntityType entity;
    
    public PursueHiringCommand(EntityType entity)
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
