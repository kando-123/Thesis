package ge.gui;

import ge.player.*;
import ge.utilities.*;
import ge.world.*;
import java.awt.*;
import javax.swing.*;

/**
 *
 * @author Kay Jay O'Nail
 */
public class GUIManager
{
    private final UserPanel userPanel;
    private final WorldPanel worldPanel;
    
    private Invoker<WorldManager> worldInvoker;
    private Invoker<PlayerManager> playerInvoker;
    
    public GUIManager(JFrame frame, WorldRenderer renderer)
    {
        var contentPane = new JPanel(new BorderLayout());
        
        userPanel = new UserPanel(new Invoker<>(this));
        worldPanel = new WorldPanel(renderer, new Invoker<>(this));
        
        contentPane.add(userPanel, BorderLayout.WEST);
        contentPane.add(worldPanel, BorderLayout.CENTER);
        
        var toolkit = Toolkit.getDefaultToolkit();
        var size = toolkit.getScreenSize();
        size.width *= 0.75;
        size.height *= 0.75;
        contentPane.setPreferredSize(size);
        
        frame.addKeyListener(worldPanel.getKeyListener());
        
        frame.setContentPane(contentPane);
        frame.pack();
        
        frame.setLocationRelativeTo(null);
        frame.setResizable(true);
        frame.requestFocus();
    }

    public void setWorldInvoker(Invoker<WorldManager> worldInvoker)
    {
        this.worldInvoker = worldInvoker;
    }

    public void setPlayerInvoker(Invoker<PlayerManager> playerInvoker)
    {
        this.playerInvoker = playerInvoker;
    }
    
    public void start()
    {
        var thread = new Thread(worldPanel);
        thread.setDaemon(true);
        thread.start();
    }
}
