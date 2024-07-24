package my.gameplay;

import my.units.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import my.game.*;
import my.player.*;

/**
 *
 * @author Kay Jay O'Nail
 */
public class UserPanel extends JPanel implements ActionListener, ItemListener
{
    private JLabel nameLabel;
    private JLabel moneyLabel;
    private final ActionListener master;

    public UserPanel(Master master)
    {
        super(new GridBagLayout());
        
        this.master = master;

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
        JPanel shopPanel = new JPanel();
        shopPanel.setLayout(new BoxLayout(shopPanel, BoxLayout.Y_AXIS));
        shopPanel.setOpaque(false);

        JComboBox buildings = new JComboBox();
        buildings.addItem("Build New Property");
        buildings.addItem(FieldType.TOWN);
        buildings.addItem(FieldType.VILLAGE);
        buildings.addItem(FieldType.FARMFIELD);
        buildings.addItem(FieldType.MINE);
        buildings.addItem(FieldType.BARRACKS);
        buildings.addItem(FieldType.SHIPYARD);
        buildings.addItem(FieldType.FORTRESS);
        buildings.setRenderer(new Renderer());
        buildings.addActionListener(this);
        buildings.addItemListener(this);
        shopPanel.add(buildings);
        
        JComboBox entities = new JComboBox();
        entities.addItem("Hire New Entity");
        entities.addItem(EntityType.INFANTRY);
        entities.addItem(EntityType.CAVALRY);
        entities.addItem(EntityType.NAVY);
        entities.setRenderer(new Renderer());
        entities.addActionListener(this);
        entities.addItemListener(this);
        shopPanel.add(entities);

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
        undoButton.addActionListener(master);
        buttonsPanel.add(undoButton, c);

        c.gridx = 1;
        JButton redoButton = new JButton("Redo ↻");
        redoButton.setActionCommand("redo");
        redoButton.addActionListener(master);
        buttonsPanel.add(redoButton, c);

        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 2;
        JButton doneButton = new JButton("Done!");
        doneButton.setActionCommand("done");
        doneButton.addActionListener(master);
        buttonsPanel.add(doneButton, c);

        return buttonsPanel;
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        // process the action event and inform the master
        // master.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "temporary"));
        // System.out.println(e.paramString());
    }

    @Override
    public void itemStateChanged(ItemEvent e)
    {
        // process the action event and inform the master
        if (e.getStateChange() == ItemEvent.SELECTED)
        {
            String actionCommand = String.format("to-build;%s", String.valueOf(e.getItem()));
            master.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, actionCommand));
        }
    }

    private class Renderer extends JLabel implements ListCellRenderer<Object>
    {
        private final ImageManager imageManager;

        public Renderer()
        {
            imageManager = ImageManager.getInstance();
        }

        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus)
        {
            if (value.getClass() == FieldType.class)
            {
                FieldType field = (FieldType) value;
                setText(field.name());
                setIcon(imageManager.getFieldAsIcon(field));
            }
            else if (value.getClass() == EntityType.class)
            {
                EntityType entity = (EntityType) value;
                setText(entity.name());
                setIcon(imageManager.getEntityAsIcon(entity));
            }
            else
            {
                setText(String.valueOf(value));
                setIcon(null);
            }

            return this;
        }
    }

    public void setUser(Player user)
    {
        nameLabel.setText(user.getName());
        moneyLabel.setText(String.format("Money: %d Ħ", user.getMoney()));

        Color userColor = user.getColor().colorValue;
        setBackground(userColor);
    }
}
