package my.gameplay;

import my.world.WorldPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import my.game.*;
import my.player.*;
import my.world.*;

class UserPanel extends JPanel    
{
    private final JLabel nameLabel;
    private final JButton button;
    
    public UserPanel()
    {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        
        nameLabel = new JLabel("Unnamed Player");
        add(nameLabel);
        
        button = new JButton("Done!");
        button.setActionCommand("done");
        button.addActionListener(Master.getInstance());
        add(button);
        
        setBackground(Color.white);
    }
    
    public void setUser(Player user)
    {
        nameLabel.setText(user.getName());
        setBackground(user.getColor().colorValue);
    }
}

/**
 *
 * @author Kay Jay O'Nail
 */
public class GameplayContentPane extends JPanel
{
    private final WorldPanel worldPanel;
    
    private final UserPanel userPanel;

    public GameplayContentPane(World world)
    {
        super(new BorderLayout());

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        Dimension contentPaneSize = new Dimension();
        contentPaneSize.width = (int) (screenSize.width * (0.75));
        contentPaneSize.height = (int) (screenSize.height * (0.75));

        worldPanel = new WorldPanel();
        worldPanel.setWorld(world);
        worldPanel.setPreferredSize(contentPaneSize);
        add(worldPanel, BorderLayout.CENTER);
        
        userPanel = new UserPanel();
        add(userPanel, BorderLayout.WEST);
    }

    public void start()
    {
        Thread thread = new Thread(worldPanel);
        thread.setDaemon(true);
        thread.start();
    }

    public void setCurrentUser(Player user)
    {
        userPanel.setUser(user);
    }
}
