package my.gameplay;

import my.world.*;
import my.world.field.*;

/**
 *
 * @author Kay Jay O'Nail
 */
public class GameplayManager
{
    private World world;
    
    private GameplayManager()
    {
        
    }
    
    private static GameplayManager instance = null;
    
    public static GameplayManager getInstance()
    {
        if (instance == null)
        {
            instance = new GameplayManager();
        }
        return instance;
    }
    
    public void makeWorld(WorldParameters parameters)
    {
        FieldManager.getInstance();
        world = new World(parameters);
    }
    
    public World getWorld()
    {
        return world;
    }
}
