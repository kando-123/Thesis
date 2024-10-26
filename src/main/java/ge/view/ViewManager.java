package ge.view;

import ge.field.BuildingType;
import ge.main.*;
import ge.player.*;
import ge.utilities.*;
import ge.world.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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
        this.frame = frame;
        
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
    
    void setUser(UserAccessor accessor)
    {
        userPanel.setBackground(accessor.getColor().rgb);
        userPanel.setUserName(accessor.getName());
        userPanel.setUserMoney(accessor.getMoney());
    }
    
    void showBuildingInfo(BuildingType building)
    {
        var info = new BuildingInfoDialog(frame, building);
        info.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                frame.requestFocus();
            }
        });
        info.setVisible(true);
    }
    
    void finish()
    {
        frame.requestFocus();
        invoker.invoke(new NextPlayerCommand());
    }
}
