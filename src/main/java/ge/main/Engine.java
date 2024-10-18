package ge.main;

import ge.gui.*;
import ge.player.*;
import ge.utilities.*;
import ge.world.*;

/**
 *
 * @author Kay Jay O'Nail
 */
public class Engine
{
    public static void main(String[] args)
    {
        var engine = new Engine();
    }
    
    private final GUIManager guiManager;
    private final WorldManager worldManager;
    private final PlayerManager playerManager;
    
    private Engine()
    {
        guiManager = new GUIManager(new Invoker<>(this));
        worldManager = new WorldManager(new Invoker<>(this));
        playerManager = new PlayerManager(new Invoker<>(this));
    }
    
    void createWorld(WorldConfig config)
    {
        
    }
    
    void createPlayers(PlayerConfig[] configs)
    {
        
    }
}
