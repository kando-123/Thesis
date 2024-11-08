package ge.field;

import ge.entity.Entity;
import ge.entity.EntityType;
import ge.main.DropCommand;
import ge.main.GameplayManager;
import ge.player.Player;
import ge.utilities.Command;
import ge.utilities.Hex;
import ge.utilities.Invoker;

/**
 *
 * @author Kay Jay O'Nail
 */
public class CapitalField extends PropertyField implements Fortification, Spawner, Commercial
{
    public static final int INCOME = 200;
    
    private final Invoker<GameplayManager> invoker;
    
    public CapitalField(Hex coords, Invoker<GameplayManager> invoker)
    {
        super(coords);
        this.invoker = invoker;
    }

    @Override
    public boolean canSpawn(EntityType type)
    {
        return !isOccupied() && (type == EntityType.CAVALRY || type == EntityType.INFANTRY);
    }

    @Override
    public int getIncome()
    {
        return INCOME;
    }

    @Override
    public void spawn(Entity entity)
    {
        placeEntity(entity);
        entity.setMovable(true);
    }

    @Override
    public Entity placeEntity(Entity comer)
    {
        var oldOwner = owner;
        
        var remainder = super.placeEntity(comer);
        
        if (owner != oldOwner)
        {
            invoker.invoke(new DropCommand(oldOwner));
        }
        
        return remainder;
    }
}
