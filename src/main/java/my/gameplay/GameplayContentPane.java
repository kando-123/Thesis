package my.gameplay;

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
    
    public GameplayContentPane(WorldParameters parameters)
    {
        super(new BorderLayout());
        
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
//        int frameWidth = (int) (screenSize.width / Math.sqrt(2.0));
//        int frameHeight = (int) (screenSize.height / Math.sqrt(2.0));
        int frameWidth = screenSize.width;
        int frameHeight = screenSize.height;
        
        GameplayManager manager = GameplayManager.getInstance();
        manager.makeWorld(parameters);
        World world = manager.getWorld();
        
        Dimension panelSize = new Dimension(frameWidth, (int) (0.9 * frameHeight));
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
        panel.setPreferredSize(new Dimension(frameWidth, (int) (0.1 * frameHeight)));
        add(panel, BorderLayout.SOUTH);
    }
}
