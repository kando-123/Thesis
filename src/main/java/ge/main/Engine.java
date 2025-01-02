package ge.main;

import ge.config.*;
import ge.view.*;
import ge.player.*;
import ge.utilities.*;
import ge.world.*;
import java.awt.event.*;
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
        var menuBar = new JMenuBar();
        var helpMenu = new JMenu("Help");
        var helpItem = new JMenuItem("Show Help");
        helpItem.addActionListener((ActionEvent e) -> showHelpWindow());
        helpMenu.add(helpItem);
        menuBar.add(helpMenu);
        frame.setJMenuBar(menuBar);
    }
    
    private void showHelpWindow()
    {
        var help = new HelpDialog(frame);
        help.setSize(600, 400);
        help.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        help.setLocationRelativeTo(null);
    }
    
    private void beginConfig()
    {
        configManager = new ConfigManager(frame, new Invoker<>(this));
    }
    
    void beginGameplay(PlayerConfig[] players, WorldConfig world)
    {
        configManager = null;
        
        /* The construction of the two managers would need some polishing, but not now. */
        
        gameplayManager = new GameplayManager(world);
        viewManager = new ViewManager(frame, new Invoker<>(this));
        
        gameplayManager.makePlayers(players, new Invoker<>(viewManager));
        viewManager.makeView(gameplayManager.getWorldRenderer(), gameplayManager.getWorldAccessor());
        
        viewManager.setInvoker(new Invoker<>(gameplayManager));
        viewManager.setPlayers(gameplayManager.getPlayersAccessor());
        viewManager.start();
        
        gameplayManager.begin();
    }
    
    void reset()
    {
        configManager = new ConfigManager(frame, new Invoker<>(this));
        viewManager = null;
        gameplayManager = null;
    }
}
