package my.gui;

import my.flow.Manager;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import my.command.Command;
import my.command.Invoker;
import my.command.NextPlayerCommand;
import my.command.RedoCommand;
import my.command.UndoCommand;
import my.entity.AbstractEntity;
import my.entity.EntityType;
import my.field.AbstractField;
import my.field.BuildingField;
import my.field.FieldType;
import my.player.Player;

/**
 *
 * @author Kay Jay O'Nail
 */
public class UserPanel extends JPanel implements ActionListener
{
    private JLabel nameLabel;
    private JLabel moneyLabel;
    private final Invoker<Manager> invoker;

    public UserPanel(Invoker<Manager> ivoker)
    {
        super(new GridBagLayout());
        this.invoker = ivoker;

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.weighty = 1;
        c.gridx = 0;
        c.gridy = 0;
        add(makeDataPanel(), c.clone());

        ++c.gridy;
        add(makeShopPanel(), c.clone());

        ++c.gridy;
        add(makeButtonsPanel(), c.clone());

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
        dataPanel.add(nameLabel, c.clone());

        ++c.gridy;
        c.insets.bottom = INSET;
        moneyLabel = new JLabel("No money!");
        moneyLabel.setForeground(Color.black);
        moneyLabel.setOpaque(true);
        moneyLabel.setBackground(Color.white);
        moneyLabel.setBorder(BorderFactory.createLineBorder(Color.black));
        moneyLabel.setToolTipText("HexCoin");
        dataPanel.add(moneyLabel, c.clone());

        return dataPanel;
    }

    private JPanel makeShopPanel()
    {
        JPanel shopPanel = new JPanel(new GridBagLayout());
        shopPanel.setOpaque(false);

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.CENTER;
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1;
        c.weighty = 1;
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.add("New Building", makeBuildingButtonsPanel());
        tabbedPane.add("New Entity", makeEntityButtonsPanel());
        shopPanel.add(tabbedPane, c.clone());
        
        ++c.gridy;
        JLabel label = new JLabel("Shift-click to see info");
        label.setOpaque(true);
        label.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        label.setForeground(Color.DARK_GRAY);
        shopPanel.add(label, c.clone());

        return shopPanel;
    }

    private JPanel makeBuildingButtonsPanel()
    {
        JPanel panel = new JPanel(new GridLayout(0, 2));
        for (FieldType buildingType : FieldType.buildings())
        {
            BuildingField building = (BuildingField) AbstractField.newInstance(buildingType);
            BuildingButton button = new BuildingButton(invoker, building);
            panel.add(button);
        }
        return panel;
    } 
    
    private JPanel makeEntityButtonsPanel()
    {
        JPanel panel = new JPanel(new GridLayout(0, 2));
        for (EntityType entityType : EntityType.values())
        {
            AbstractEntity entity = AbstractEntity.newInstance(entityType);
            EntityButton button = new EntityButton(invoker, entity);
            panel.add(button);
        }
        return panel;
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
        undoButton.addActionListener(this);
        buttonsPanel.add(undoButton, c.clone());

        c.gridx = 1;
        JButton redoButton = new JButton("Redo ↻");
        redoButton.setActionCommand("redo");
        redoButton.addActionListener(this);
        buttonsPanel.add(redoButton, c.clone());

        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 2;
        JButton doneButton = new JButton("Done ✓");
        doneButton.setActionCommand("done");
        doneButton.addActionListener(this);
        buttonsPanel.add(doneButton, c.clone());

        return buttonsPanel;
    }

    public void setUser(Player user)
    {
        nameLabel.setText(user.getName());
        moneyLabel.setText(String.format("Money: %d Ħ", user.getMoney()));

        Color userColor = user.getColor().colorValue;
        setBackground(userColor);
    }

    public void setMoney(int newMoney)
    {
        moneyLabel.setText(String.format("Money: %d Ħ", newMoney));
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        Command command = switch (e.getActionCommand())
        {
            case "undo" ->
            {
                yield new UndoCommand();
            }
            case "redo" ->
            {
                yield new RedoCommand();
            }
            case "done" ->
            {
                yield new NextPlayerCommand();
            }
            default ->
            {
                /* Never happens. */
                yield null;
            }
        };
        invoker.invoke(command);
    }
}
