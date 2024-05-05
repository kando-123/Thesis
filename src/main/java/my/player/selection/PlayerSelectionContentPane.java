package my.player.selection;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import my.game.GameMode;
import my.game.Master;
import my.i18n.Dictionary;
import my.i18n.Statement;

/**
 *
 * @author Kay Jay O'Nail
 */
public class PlayerSelectionContentPane extends JPanel
{
    private final Master master;
    private final PlayerSelector selector;
    
    public PlayerSelectionContentPane()
    {
        super(new GridBagLayout());
        
        master = Master.getInstance();
        
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(10, 10, 10, 10);
        JLabel label = new JLabel("Select how many players will partake");
        add(label, c);
        
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 1;
        if (master.getGameMode() == GameMode.HOST)
        {
            HostModeSelectionPanel panel = new HostModeSelectionPanel();
            add(panel, c);
            selector = panel;
        }
        else
        {
            GuestModeSelectionPanel panel = new GuestModeSelectionPanel();
            add(panel, c);
            selector = panel;
        }
        
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 2;
        Dictionary dictionary = Dictionary.getInstance();
        JButton button = new JButton(dictionary.translate(Statement.READY));
        button.setActionCommand("players-selected");
        button.addActionListener(master);
        add(button, c);
    }
    
    public int getLocalPlayersCount()
    {
        return selector.getLocalsNumber();
    }
    
    public int getRemotePlayersCount()
    {
        return selector.getRemotesNumber();
    }
    
    public int getBotsPlayersCount()
    {
        return selector.getBotsNumber();
    }
}
