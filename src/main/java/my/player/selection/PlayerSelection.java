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
        
        /* Local players selection panel: LOCAL, REMOTE_HOST, REMOTE_GUEST */
        
        
        
        /* Artificial players selection panel: LOCAL, REMOTE_HOST */
        
        if (gameMode == GameMode.LOCAL || gameMode == GameMode.REMOTE_HOST)
        {
            JPanel localSelection = new FullSelectionPanel();
            tabbedPane.add("Local", localSelection);
            
            JPanel artificialSelection = new FullSelectionPanel();
            tabbedPane.add("Artificial", artificialSelection);
        }
        else
        {
            JPanel localSelection = new PartialSelectionPanel();
            tabbedPane.add("Local", localSelection);
        }
        
        /* Remote players selection pane: REMOTE_HOST */
        
        if (gameMode == GameMode.REMOTE_HOST)
        {
            JPanel remoteSelection = new FullSelectionPanel();
            tabbedPane.add("Remote", remoteSelection);
        }
        
        add(tabbedPane);
    }
}
