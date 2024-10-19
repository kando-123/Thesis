package ge.gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 *
 * @author Kay Jay O'Nail
 */
public class UserPanel extends JPanel implements ActionListener
{
    private JLabel nameLabel;
    private JLabel moneyLabel;

    public UserPanel()
    {
        super(new GridBagLayout());

        var c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.weighty = 1;
        c.gridx = 0;
        c.gridy = 0;
        add(makeDataPanel(), c.clone());

        ++c.gridy;
        add(makeShopPanel(), c.clone());

        ++c.gridy;
        add(makeControlButtons(), c.clone());

        setBackground(Color.white);
    }

    private static final int INSET = 10;

    private JPanel makeDataPanel()
    {
        var dataPanel = new JPanel(new GridBagLayout());

        dataPanel.setOpaque(false);

        var c = new GridBagConstraints();
        c.insets = new Insets(INSET, INSET, 0, INSET);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.weighty = 1;
        c.gridx = 0;
        c.gridy = 0;
        nameLabel = new JLabel();
        nameLabel.setForeground(Color.black);
        nameLabel.setOpaque(true);
        nameLabel.setBackground(Color.white);
        nameLabel.setBorder(BorderFactory.createLineBorder(Color.black));
        dataPanel.add(nameLabel, c.clone());

        ++c.gridy;
        c.insets.bottom = INSET;
        moneyLabel = new JLabel();
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
        var shopPanel = new JPanel(new GridBagLayout());
        shopPanel.setOpaque(false);

        var c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.CENTER;
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1;
        c.weighty = 1;
        var tabbedPane = new JTabbedPane();
        tabbedPane.add("New Building", makeBuildingButtons());
        tabbedPane.add("New Entity", makeEntityButtons());
        shopPanel.add(tabbedPane, c.clone());

        ++c.gridy;
        var label = new JLabel("Shift-click to see info");
        label.setOpaque(true);
        label.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        label.setForeground(Color.DARK_GRAY);
        shopPanel.add(label, c.clone());

        return shopPanel;
    }

    private JPanel makeBuildingButtons()
    {
        var panel = new JPanel(new GridLayout(0, 2));
//        for (FieldType buildingType : FieldType.buildings())
//        {
//            BuildingField building = (BuildingField) AbstractField.newInstance(buildingType);
//            BuildingButton button = new BuildingButton(playerInvoker, building);
//            panel.add(button);
//        }
        return panel;
    }

    private JPanel makeEntityButtons()
    {
        var panel = new JPanel(new GridLayout(0, 2));
//        for (EntityType entityType : EntityType.values())
//        {
//            AbstractEntity entity = AbstractEntity.newInstance(entityType);
//            EntityButton button = new EntityButton(playerInvoker, entity);
//            panel.add(button);
//        }
        return panel;
    }

    private JPanel makeControlButtons()
    {
        var buttonsPanel = new JPanel(new GridBagLayout());
        buttonsPanel.setOpaque(false);

        var c = new GridBagConstraints();
        c.weightx = 1;
        c.weighty = 1;
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 0;

        var undoButton = new JButton("Undo ↺");
        undoButton.setActionCommand("undo");
        undoButton.addActionListener(this);
        buttonsPanel.add(undoButton, c.clone());

        c.gridx = 1;
        var redoButton = new JButton("Redo ↻");
        redoButton.setActionCommand("redo");
        redoButton.addActionListener(this);
        buttonsPanel.add(redoButton, c.clone());

        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 2;
        var doneButton = new JButton("Done ✓");
        doneButton.setActionCommand("done");
        doneButton.addActionListener(this);
        buttonsPanel.add(doneButton, c.clone());

        return buttonsPanel;
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        switch (e.getActionCommand())
        {
            case "undo" ->
            {
                System.out.println("Undo request.");
            }
            case "redo" ->
            {
                System.out.println("Redo request.");
            }
            case "done" ->
            {
                System.out.println("Next player.");
            }
        }
    }
}
