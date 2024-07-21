package my.gameplay;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

import my.game.*;
import my.player.*;
import my.world.field.FieldManager;
import my.world.field.FieldType;

/**
 *
 * @author Kay Jay O'Nail
 */
public class UserPanel extends JPanel
{
    private JLabel nameLabel;
    private JLabel moneyLabel;

    public UserPanel(Master master)
    {
        super(new GridBagLayout());
        
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
        add(makeButtonsPanel(master), c);

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
        shopPanel.setOpaque(false);
        
        JComboBox buildings = new JComboBox();
        buildings.addItem("New Building");
        buildings.addItem(FieldType.TOWN);
        buildings.addItem(FieldType.VILLAGE);
        buildings.addItem(FieldType.FARMFIELD);
        buildings.addItem(FieldType.MINE);
        buildings.addItem(FieldType.BARRACKS);
        buildings.addItem(FieldType.SHIPYARD);
        buildings.setRenderer(new Renderer());
        shopPanel.add(buildings);
        
        return shopPanel;
    }
    
    private JPanel makeButtonsPanel(Master master)
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
    
    private class Renderer extends JLabel implements ListCellRenderer<Object>
    {
        private final FieldManager fieldManager;
        
        public Renderer()
        {
            fieldManager = FieldManager.getInstance();
        }
        
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus)
        {
            if (value.getClass() == FieldType.class) 
            {
                FieldType field = (FieldType) value;
                setText(field.toString());
                setIcon(fieldManager.getIcon(field));
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