package ge.field;

import ge.entity.*;
import ge.main.*;
import ge.utilities.*;

/**
 *
 * @author Kay Jay O'Nail
 */
public class CapitalField extends PropertyField implements Fortification, Spawner, Commercial
{
    private int fortitude;
    private final Invoker<GameplayManager> invoker;
    
    private static final int FORTITUDE = 200;
    public static final int INCOME = 200;
    
    public CapitalField(Hex coords, Invoker<GameplayManager> invoker)
    {
        super(coords);
        this.invoker = invoker;
        
        fortitude = FORTITUDE;
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

    @Override
    public int getFortitude()
    {
        return fortitude;
    }
}
