package my.player.selection;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.Border;
import my.player.GenericPlayer;

class UnitPanel extends JPanel implements ActionListener
{
    private List<JRadioButton> buttons;
    private ActionListener listener = null;
    private int currentlySelected;
    private int limit;
    private final int minimum;
    private final int maximum;

    public UnitPanel(int min, int max)
    {
        super(new GridBagLayout());

        buttons = new ArrayList<>(max - min + 1);
        currentlySelected = minimum = min;
        limit = maximum = max;

        ButtonGroup group = new ButtonGroup();
        Insets insets = new Insets(5, 10, 5, 10);
        for (int i = minimum; i <= maximum; ++i)
        {
            String index = String.valueOf(i);

            GridBagConstraints c = new GridBagConstraints();
            c.gridx = 0;
            c.gridy = i - minimum;
            c.insets = insets;
            JRadioButton button = new JRadioButton(index);
            button.setSelected(i == currentlySelected);
            button.setActionCommand(index);
            button.addActionListener(this);
            group.add(button);
            buttons.add(button);
            add(button, c);
        }
    }

    public void addActionListener(ActionListener newListener)
    {
        listener = newListener;
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        try
        {
            int index = Integer.parseInt(e.getActionCommand());
            int difference = index - currentlySelected;
            currentlySelected = index;
            
            System.out.print(difference);
            System.out.println(" places taken");
            
            ActionEvent event = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, String.valueOf(difference));
            listener.actionPerformed(event);
        }
        catch (NumberFormatException nf)
        {

        }
    }

    public void take(int places)
    {
        limit -= places;
        if (limit > maximum)
        {
            limit = maximum;
        }
        else if (limit < minimum)
        {
            limit = minimum;
        }
        int i = minimum;
        for (var button : buttons)
        {
            button.setEnabled(i++ <= limit);
        }
    }

    public int getNumber()
    {
        return currentlySelected;
    }
}

/**
 *
 * @author Kay Jay O'Nail
 */
public class HostModeSelectionPanel extends JPanel implements ActionListener, PlayerSelector
{
    private final UnitPanel localPanel;
    private final UnitPanel remotePanel;
    private final UnitPanel botPanel;

    public HostModeSelectionPanel()
    {
        super(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        localPanel = new UnitPanel(1, GenericPlayer.PLAYERS_COUNT);
        Border localBorder = BorderFactory.createTitledBorder("Local");
        localPanel.setBorder(localBorder);
        localPanel.addActionListener(this);
        add(localPanel, c);

        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 0;
        remotePanel = new UnitPanel(0, GenericPlayer.PLAYERS_COUNT - 1);
        Border remoteBorder = BorderFactory.createTitledBorder("Remote");
        remotePanel.setBorder(remoteBorder);
        remotePanel.addActionListener(this);
        add(remotePanel, c);

        c = new GridBagConstraints();
        c.gridx = 2;
        c.gridy = 0;
        botPanel = new UnitPanel(0, GenericPlayer.PLAYERS_COUNT - 1);
        Border botBorder = BorderFactory.createTitledBorder("Bot");
        botPanel.setBorder(botBorder);
        botPanel.addActionListener(this);
        add(botPanel, c);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        assert (e.getSource().getClass() == UnitPanel.class);
        
        UnitPanel source = (UnitPanel) e.getSource();
        Integer difference = Integer.valueOf(e.getActionCommand());
        
        if (source != localPanel)
        {
            localPanel.take(difference);
        }
        if (source != remotePanel)
        {
            remotePanel.take(difference);
        }
        if (source != botPanel)
        {
            botPanel.take(difference);
        }
    }

    @Override
    public int getLocalsNumber()
    {
        return localPanel.getNumber();
    }

    @Override
    public int getRemotesNumber()
    {
        return remotePanel.getNumber();
    }

    @Override
    public int getBotsNumber()
    {
        return botPanel.getNumber();
    }
}
