package my.player.selection;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.Border;
import my.player.GenericPlayer;

/**
 *
 * @author Kay Jay O'Nail
 */
public class GuestModeSelectionPanel extends JPanel implements PlayerSelector, ActionListener
{
    private int currentlySelected;
    
    public GuestModeSelectionPanel()
    {
        super(new GridBagLayout());
        
        currentlySelected = 1;
        
        Border border = BorderFactory.createTitledBorder("Local");
        setBorder(border);
        
        ButtonGroup group = new ButtonGroup();
        for (int i = 1; i < GenericPlayer.PLAYERS_COUNT; ++i)
        {
            String index = String.valueOf(i);
            
            GridBagConstraints c = new GridBagConstraints();
            c.gridx = 0;
            c.gridy = i - 1;
            c.insets = new Insets(5, 10, 5, 10);
            JRadioButton button = new JRadioButton(index);
            button.setSelected(i == currentlySelected);
            button.setActionCommand(index);
            button.addActionListener(this);
            group.add(button);
            add(button, c);
        }
    }
    
    @Override
    public PlayersNumber getCounts()
    {
        return new PlayersNumber(currentlySelected, 0, 0);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        try
        {
            currentlySelected = Integer.parseInt(e.getActionCommand());
        }
        catch (NumberFormatException nf)
        {
            
        }
    }
}
