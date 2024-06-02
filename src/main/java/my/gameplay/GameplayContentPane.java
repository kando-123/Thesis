package my.gameplay;

import my.world.WorldPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import my.game.Master;
import my.world.*;

/**
 *
 * @author Kay Jay O'Nail
 */
public class GameplayContentPane extends JPanel
{
    private final WorldPanel worldPanel;
    
    public GameplayContentPane(WorldConfiguration parameters)
    {
        super(new BorderLayout());
        
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;
        
        GameplayManager manager = GameplayManager.getInstance();
        manager.makeWorld(parameters);
        World world = manager.getWorld();
        
        Dimension panelSize = new Dimension(screenWidth, (int) (0.9 * screenHeight));
        worldPanel = new WorldPanel();
        worldPanel.setWorld(world);
        worldPanel.setPreferredSize(panelSize);
        add(worldPanel, BorderLayout.CENTER);
        
        ThreadPool pool = ThreadPool.getInstance();
        pool.fork(worldPanel);
        
        JPanel panel = new JPanel();
        panel.add(new JLabel("This will be the player's panel."));
        JButton end = new JButton("END");
        end.addActionListener(Master.getInstance());
        panel.add(end);
        panel.setPreferredSize(new Dimension(screenWidth, (int) (0.1 * screenHeight)));
        add(panel, BorderLayout.SOUTH);
    }
}
