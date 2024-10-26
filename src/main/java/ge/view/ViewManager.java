package ge.view;

import ge.main.*;
import ge.utilities.*;
import ge.world.*;
import java.awt.*;
import javax.swing.*;

/**
 *
 * @author Kay Jay O'Nail
 */
public class ViewManager
{
    private JFrame frame;
    
    private UserPanel userPanel;
    private WorldPanel worldPanel;
    
    private Invoker<GameplayManager> invoker;
    
    public void init(JFrame frame, WorldRenderer renderer)
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

    public void setInvoker(Invoker<GameplayManager> invoker)
    {
        this.invoker = invoker;
    }

    public void start()
    {
        var thread = new Thread(worldPanel);
        thread.setDaemon(true);
        thread.start();
    }
}
