package my.player.configuration;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
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
import my.player.PlayerType;

/**
 *
 * @author Kay Jay O'Nail
 */
public class PlayerConfigurationContentPane extends JPanel implements ActionListener
{
    private final List<SelectionPanel> panels;
    
    public PlayerConfigurationContentPane()
    {
        super(new GridBagLayout());
        
        JTabbedPane tabbedPane = new JTabbedPane();
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.weighty = 1.0;
        add(tabbedPane, c);
        
        panels = new ArrayList<>(2);
        
        final int players = AbstractPlayer.PLAYERS_COUNT;
        
        SelectionPanel usersSelection = new SelectionPanel(1, players, players - 1, 1);
        usersSelection.setPlayerType(PlayerType.USER);
        usersSelection.setDefaultNames("Player");
        usersSelection.addActionListener(this);
        tabbedPane.add("Users", usersSelection);
        panels.add(usersSelection);
        
        SelectionPanel botsSelection = new SelectionPanel(0, players - 1, players - 1, 1);
        botsSelection.setPlayerType(PlayerType.BOT);
        botsSelection.setDefaultNames("Bot");
        botsSelection.addActionListener(this);
        tabbedPane.add("Bots", botsSelection);
        panels.add(botsSelection);
        
        Master master = Master.getInstance();
        JButton button = new JButton("Ready");
        button.setActionCommand("->world");
        button.addActionListener(master);
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 1;
        c.weighty = 1.0;
        add(button, c);
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
    
    public int getTotalNumberOfPlayers()
    {
        int sum = 0;
        for (var panel : panels)
        {
            sum += panel.getSelected();
        }
        return sum;
    }
    
    public List<PlayerParameters> getPlayerParameters()
    {
        List<PlayerParameters> list = new ArrayList<>();
        for (var panel : panels)
        {
            list.addAll(panel.getPlayerParameters());
        }
        return list;
    }
}
