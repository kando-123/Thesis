package my.gameplay;

import my.world.WorldPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JPanel;
import my.world.*;

/**
 *
 * @author Kay Jay O'Nail
 */
public class GameplayContentPane extends JPanel
{
    private final WorldPanel worldPanel;
    private JPanel userPanel;

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
    }

    public void start()
    {
        Thread thread = new Thread(worldPanel);
        thread.setDaemon(true);
        thread.start();
    }

    public void setUserPanel(JPanel panel)
    {
        userPanel = panel;

        Dimension size = getPreferredSize();

        worldPanel.setPreferredSize(new Dimension((int) (0.9 * size.width), size.height));
        userPanel.setPreferredSize(new Dimension((int) (0.1 * size.width), size.height));

        add(userPanel, BorderLayout.WEST);
    }
}
