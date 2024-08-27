package my.game;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import my.player.Player;

/**
 *
 * @author Kay Jay O'Nail
 */
public class UserPanel extends JPanel
{
    private JLabel nameLabel;
    private JLabel moneyLabel;
    private JButton buildings;
    private JButton entities;
    private final Manager manager;

    public UserPanel(Manager manager)
    {
        super(new GridBagLayout());
        this.manager = manager;

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.weighty = 1;
        c.gridx = 0;
        c.gridy = 0;
        add(makeDataPanel(), c);

        ++c.gridy;
        add(makeShopPanel(), c);

        ++c.gridy;
        add(makeButtonsPanel(), c);

        setBackground(Color.white);
    }

    private static final int INSET = 10;

    private JPanel makeDataPanel()
    {
        JPanel dataPanel = new JPanel(new GridBagLayout());

        dataPanel.setOpaque(false);

        var c = new GridBagConstraints();
        c.insets = new Insets(INSET, INSET, 0, INSET);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.weighty = 1;
        c.gridx = 0;
        c.gridy = 0;
        nameLabel = new JLabel("Unnamed Player");
        nameLabel.setForeground(Color.black);
        nameLabel.setOpaque(true);
        nameLabel.setBackground(Color.white);
        nameLabel.setBorder(BorderFactory.createLineBorder(Color.black));
        dataPanel.add(nameLabel, c);

        ++c.gridy;
        c.insets.bottom = INSET;
        moneyLabel = new JLabel("No money!");
        moneyLabel.setForeground(Color.black);
        moneyLabel.setOpaque(true);
        moneyLabel.setBackground(Color.white);
        moneyLabel.setBorder(BorderFactory.createLineBorder(Color.black));
        moneyLabel.setToolTipText("HexCoin");
        dataPanel.add(moneyLabel, c);

        return dataPanel;
    }

    private JPanel makeShopPanel()
    {
        JPanel shopPanel = new JPanel(new GridBagLayout());
        shopPanel.setOpaque(false);
        
        GridBagConstraints c = new GridBagConstraints();
        c.weightx = 1;
        c.weighty = 1;
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.BOTH;
        
        buildings = new JButton("Build a New Property");
        buildings.setActionCommand("to-build");
        buildings.addActionListener(manager);
        shopPanel.add(buildings, c);
        
        c.gridx = 0;
        c.gridy = 1;
        entities = new JButton("Hire a New Entity");
        entities.setActionCommand("to-hire");
        entities.addActionListener(manager);
        shopPanel.add(entities, c);

        return shopPanel;
    }

    private JPanel makeButtonsPanel()
    {
        JPanel buttonsPanel = new JPanel(new GridBagLayout());
        buttonsPanel.setOpaque(false);

        GridBagConstraints c = new GridBagConstraints();
        c.weightx = 1;
        c.weighty = 1;
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 0;

        JButton undoButton = new JButton("Undo ↺");
        undoButton.setActionCommand("undo");
        undoButton.addActionListener(manager);
        buttonsPanel.add(undoButton, c);

        c.gridx = 1;
        JButton redoButton = new JButton("Redo ↻");
        redoButton.setActionCommand("redo");
        redoButton.addActionListener(manager);
        buttonsPanel.add(redoButton, c);

        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 2;
        JButton doneButton = new JButton("Done!");
        doneButton.setActionCommand("done");
        doneButton.addActionListener(manager);
        buttonsPanel.add(doneButton, c);

        return buttonsPanel;
    }

    public void setUser(Player user)
    {
        nameLabel.setText(user.getName());
        moneyLabel.setText(String.format("Money: %d Ħ", user.getMoney()));

        Color userColor = user.getColor().colorValue;
        setBackground(userColor);
    }
}
