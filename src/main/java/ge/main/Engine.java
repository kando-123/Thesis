package ge.main;

import ge.config.*;
import ge.view.ViewManager;
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
    private ViewManager viewManager;
    private GameplayManager gameplayManager;
    
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
        /* The construction of the two managers would need some polishing, but not now. */
        
        gameplayManager = new GameplayManager();
        viewManager = new ViewManager();
        
        gameplayManager.init(players, world, new Invoker<>(viewManager));
        viewManager.init(frame, gameplayManager.getWorldRenderer());
        
        viewManager.setInvoker(new Invoker<>(gameplayManager));
        viewManager.start();
    }
}
