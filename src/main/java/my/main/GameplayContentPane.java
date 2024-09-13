package my.main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JPanel;

/**
 *
 * @author Kay Jay O'Nail
 */
public class GameplayContentPane extends JPanel
{
    private final WorldPanel worldPanel;
    private final UserPanel userPanel;

    public GameplayContentPane(Master master, Manager manager, InputHandler inputHandler)
    {
        super(new BorderLayout());

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        int width = (int) (screenSize.width * (0.75));
        int height = (int) (screenSize.height * (0.75));

        Dimension worldPanelSize = new Dimension((int) (0.8 * width), height);
        worldPanel = new WorldPanel(manager.createInvoker());
        worldPanel.setWorld(manager.getWorld());
        worldPanel.setInputHandler(inputHandler);
        worldPanel.setPreferredSize(worldPanelSize);
        add(worldPanel, BorderLayout.CENTER);

        Dimension userPanelSize = new Dimension((int) (0.2 * width), height);
        userPanel = new UserPanel(manager.createInvoker());
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
    
    public UserPanel getUserPanel()
    {
        return userPanel;
    }
    
    public WorldPanel getWorldPanel()
    {
        return worldPanel;
    }
}
