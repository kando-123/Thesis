package ge.manager;

import ge.gui.*;
import ge.utilities.*;
import java.awt.*;
import javax.swing.*;

/**
 *
 * @author Kay Jay O'Nail
 */
public class GUIManager
{
    
    
    public GUIManager(JFrame frame)
    {
        this.frame = frame;
    }
    
    void build()
    {
        assert (worldInvoker != null);
        assert (playerInvoker != null);
        
        var toolkit = Toolkit.getDefaultToolkit();
        var size = toolkit.getScreenSize();
        size.width *= 0.75;
        size.height *= 0.75;

        var pane = new JPanel();
        pane.setPreferredSize(size);
        
        var userPanelSize = new Dimension(size);
        userPanelSize.width *= 0.2;
        userPanel = new UserPanel();
        userPanel.setPreferredSize(userPanelSize);
        
        var worldPanelSize = new Dimension(size);
        worldPanelSize.width *= 0.8;
        worldPanel = new WorldPanel();
        worldPanel.setPreferredSize(worldPanelSize);
        
        var thread = new Thread(worldPanel);
        thread.setDaemon(true);
        thread.start();
    }
}
