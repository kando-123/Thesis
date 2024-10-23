package ge.engine;

import ge.config.*;
import ge.player.*;
import ge.utilities.Invoker;
import ge.world.*;
import javax.swing.*;

/**
 *
 * @author Kay Jay O'Nail
 */
public class Engine
{
    private static Engine engine;
    
    public static void main(String... args)
    {
        engine = new Engine();
        engine.beginConfig();
    }
    
    private final JFrame frame;
    
    private ConfigManager configManager;
    //private GUIManager guiManager;
    private WorldManager worldManager;
    //private PlayerManager playerManager;
    
    private Engine()
    {
        frame = new JFrame();
    }
    
    private void beginConfig()
    {
        configManager = new ConfigManager(frame, new Invoker<>(this));
    }
    
    void beginGameplay(PlayerConfig[] players, WorldConfig world)
    {
        worldManager = new WorldManager(world);
    }
}
