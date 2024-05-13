package my.player.selection;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import my.player.AbstractPlayer;

/**
 *
 * @author Kay Jay O'Nail
 */
public class PlayerSelectionContentPane extends JPanel
{
    public PlayerSelectionContentPane()
    {
        JTabbedPane tabbedPane = new JTabbedPane();
        add(tabbedPane);
        
        final int players = AbstractPlayer.PLAYERS_COUNT;
        SelectionPanel usersSelection = new SelectionPanel(1, players, players - 1, 1);
        usersSelection.setDefaultNames("Player");
        tabbedPane.add("Users", usersSelection);
        SelectionPanel botsSelection = new SelectionPanel(0, players - 1, players - 1, 1);
        botsSelection.setDefaultNames("Bot");
        tabbedPane.add("Bots", botsSelection);
    }
}
