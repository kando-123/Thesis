package ge.main;

import ge.entity.*;
import ge.player.*;
import ge.utilities.*;

/**
 *
 * @author Kay Jay O'Nail
 */
public class MarkForHiringCommand extends Command<GameplayManager>
{
    private final boolean value;
    private final Player player;
    private final EntityType entity; 

    public MarkForHiringCommand(boolean value, Player player, EntityType entity)
    {
        this.value = value;
        this.player = player;
        this.entity = entity;
    }
    
    @Override
    public void execute(GameplayManager executor)
    {
        executor.markForHiring(value, player, entity);
    }
}
