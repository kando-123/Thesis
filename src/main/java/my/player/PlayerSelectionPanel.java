package my.player;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import my.game.GameMode;

/**
 *
 * @author Kay Jay O'Nail
 */
public class PlayerSelectionPanel extends JPanel
{
    public PlayerSelectionPanel(GameMode gameMode)
    {
        JTabbedPane tabbedPane = new JTabbedPane();
        
        /* Local players selection panel; LOCAL, REMOTE_HOST, REMOTE_GUEST */
        
        JPanel localPlayers = new JPanel();
        
        
        /* Artificial players selection panel; LOCAL, REMOTE_HOST */
        
        
        
        /* Remote players selection panel; REMOTE_HOST */
        
        
    }
}
