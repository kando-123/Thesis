package my.player.selection;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import my.game.Master;
import my.player.AbstractPlayer;
import my.player.PlayerParameters;

/**
 *
 * @author Kay Jay O'Nail
 */
public class PlayerSelectionContentPane extends JPanel implements ActionListener
{
    private List<SelectionPanel> panels;
    
    public PlayerSelectionContentPane()
    {
        JTabbedPane tabbedPane = new JTabbedPane();
        add(tabbedPane);
        
        panels = new ArrayList<>(2);
        
        final int players = AbstractPlayer.PLAYERS_COUNT;
        
        SelectionPanel usersSelection = new SelectionPanel(1, players, players - 1, 1);
        usersSelection.setDefaultNames("Player");
        usersSelection.addActionListener(this);
        tabbedPane.add("Users", usersSelection);
        panels.add(usersSelection);
        
        SelectionPanel botsSelection = new SelectionPanel(0, players - 1, players - 1, 1);
        botsSelection.setDefaultNames("Bot");
        botsSelection.addActionListener(this);
        tabbedPane.add("Bots", botsSelection);
        panels.add(botsSelection);
        
        Master master = Master.getInstance();
        JButton button = new JButton("Ready");
        button.setActionCommand("players-selected");
        button.addActionListener(master);
        add(button);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        String[] particles = e.getActionCommand().split(";");
        switch (particles[0])
        {
            case "REL" ->
            {
                Object source = e.getSource();
                for (var panel : panels)
                {
                    if (panel != source)
                    {
                        panel.actionPerformed(e);
                    }
                }
            }
            case "DES", "SEL" ->
            {
                Object source = e.getSource();
                String command = "%s;%s;STOP".formatted(particles[0], particles[1]);
                ActionEvent event = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, command);
                for (var panel : panels)
                {
                    if (panel != source)
                    {
                        panel.actionPerformed(event);
                    }
                }
            }
        }
    }
}
