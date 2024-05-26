package my.gameplay;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JLabel;
import javax.swing.JPanel;
import my.world.WorldParameters;

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
        int frameWidth = (int) (screenSize.width / Math.sqrt(2.0));
        int frameHeight = (int) (screenSize.height / Math.sqrt(2.0));
        Dimension frameSize = new Dimension(frameWidth, frameHeight);
        
        worldPanel = new WorldPanel();
        worldPanel.makeWorld(parameters);
        worldPanel.setPreferredSize(frameSize);
        add(worldPanel, BorderLayout.CENTER);
        
        JPanel panel = new JPanel();
        panel.add(new JLabel("This will be the player's panel."));
        panel.setPreferredSize(new Dimension(frameWidth, (int) (0.1 * frameHeight)));
        add(panel, BorderLayout.SOUTH);
    }
}
