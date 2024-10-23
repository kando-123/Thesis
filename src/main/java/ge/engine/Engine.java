package ge.engine;

import ge.config.ConfigManager;
import javax.swing.JFrame;

/**
 *
 * @author Kay Jay O'Nail
 */
public class Engine
{
    private static Engine engine;
    
    private final JFrame frame;
    private final ConfigManager configManager;
    // private GUIManager guiManager;
    // private WorldManager worldManager;
    // private PlayerManager playerManager;
    
    private Engine()
    {
        frame = new JFrame();
        configManager = new ConfigManager(frame);
    }
    
    public static void main(String... args)
    {
        engine = new Engine();
    }
    
    
}
