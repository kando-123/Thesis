package ge.gui;

import ge.main.CreatePlayersCommand;
import ge.main.CreateWorldCommand;
import ge.main.Engine;
import ge.utilities.*;
import java.awt.*;
import javax.swing.*;

/**
 *
 * @author Kay Jay O'Nail
 */
public class GUIManager
{
    private final Invoker<Engine> invoker;
    
    private final JFrame frame;
    
    private PlayerConfigContentPane playerContentPane;
    private WorldConfigContentPane worldContentPane;
    
    private final Dimension contentPaneSize;
    
    public GUIManager(Invoker<Engine> invoker)
    {
        this.invoker = invoker;
        
        frame = new JFrame();
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("The Global Empire");
        frame.setResizable(false);

        var pane = new InvitationContentPane(new Invoker<>(this));
        frame.setContentPane(pane);
        frame.pack();
        
        contentPaneSize = pane.getSize();

        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }
    
    void beginPlayerConfig()
    {
        if (playerContentPane == null)
        {
            playerContentPane = new PlayerConfigContentPane(new Invoker<>(this));
            playerContentPane.setPreferredSize(contentPaneSize);
        }
        frame.setContentPane(playerContentPane);
        frame.pack();
    }
    
    void beginWorldConfig()
    {
        if (worldContentPane == null)
        {
            worldContentPane = new WorldConfigContentPane(new Invoker<>(this));
            worldContentPane.setPreferredSize(contentPaneSize);
        }
        frame.setContentPane(worldContentPane);
        frame.pack();
    }
    
    void beginGameplay()
    {
        var playerConfigs = playerContentPane.getConfigs();
        playerContentPane = null;
        
        var worldConfig = worldContentPane.getConfig();
        worldContentPane = null;
        
        invoker.invoke(new CreateWorldCommand(worldConfig));
        invoker.invoke(new CreatePlayersCommand(playerConfigs));
    }
}
