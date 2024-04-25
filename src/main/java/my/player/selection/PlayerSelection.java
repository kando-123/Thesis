package my.player.selection;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import my.game.GameMode;

/**
 *
 * @author Kay Jay O'Nail
 */
public class PlayerSelection extends JPanel
{
    public PlayerSelection(GameMode gameMode)
    {
        JTabbedPane tabbedPane = new JTabbedPane();
        
        /* Local players selection panel; LOCAL, REMOTE_HOST, REMOTE_GUEST */
        
        JPanel localSelection = new SelectionPanel();
        tabbedPane.add("Local", localSelection);
        
        /* Artificial players selection panel; LOCAL, REMOTE_HOST */
        
        JPanel artificialSelection = new SelectionPanel();
        tabbedPane.add("Artificial", artificialSelection);
        
        /* Remote players selection panel; REMOTE_HOST */
        
        add(tabbedPane);
    }
}
