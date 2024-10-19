package ge.manager;

import ge.world.*;

/**
 *
 * @author Kay Jay O'Nail
 */
public class WorldManager
{
    private final World world;
    
    WorldManager(WorldConfig config)
    {
        world = new World(config);
    }
}
