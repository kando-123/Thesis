package ge.field;

import ge.entity.*;

/**
 *
 * @author Kay Jay O'Nail
 */
public interface Spawner
{
    public boolean canSpawn(EntityType type);
    public void spawn(Entity entity);
}
