package my.gameplay;

import my.world.InputHandler;
import my.world.WorldPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JPanel;

import my.game.*;
import my.player.*;
import my.world.*;

/**
 *
 * @author Kay Jay O'Nail
 */
public class GameplayContentPane extends JPanel
{
    private final WorldPanel worldPanel;
    private final UserPanel userPanel;

    public GameplayContentPane(Master master, World world, InputHandler inputHandler)
    {
        super(new BorderLayout());

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        int width = (int) (screenSize.width * (0.75));
        int height = (int) (screenSize.height * (0.75));

        Dimension worldPanelSize = new Dimension((int) (0.85 * width), height);
        worldPanel = new WorldPanel();
        worldPanel.setWorld(world);
        worldPanel.setInputHandler(inputHandler);
        worldPanel.setPreferredSize(worldPanelSize);
        add(worldPanel, BorderLayout.CENTER);

        Dimension userPanelSize = new Dimension((int) (0.15 * width), height);
        userPanel = new UserPanel(master);
        userPanel.setPreferredSize(userPanelSize);
        add(userPanel, BorderLayout.WEST);
    }

    public void start()
    {
        Thread thread = new Thread(worldPanel);
        thread.setName("navigator");
        thread.setDaemon(true);
        thread.start();
    }

    public void setCurrentUser(Player user)
    {
        userPanel.setUser(user);
    }
}
