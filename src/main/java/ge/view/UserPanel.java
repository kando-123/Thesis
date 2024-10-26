package ge.view;

import ge.entity.*;
import ge.field.*;
import ge.utilities.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 *
 * @author Kay Jay O'Nail
 */
public class UserPanel extends JPanel implements ActionListener
{
    private JLabel userNameLabel;
    private JLabel userMoneyLabel;

    private final Invoker<ViewManager> invoker;
    
    public UserPanel(Invoker<ViewManager> invoker)
    {
        super(new GridBagLayout());
        
        this.invoker = invoker;
        
        var c = new GridBagConstraints();
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
    
    private JPanel makeDataPanel()
    {
        final int INSET = 10;
        
        var dataPanel = new JPanel(new GridBagLayout());
        dataPanel.setOpaque(false);

        var c = new GridBagConstraints();
        c.insets = new Insets(INSET, INSET, 0, INSET);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.weighty = 1;
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.CENTER;
        userNameLabel = new JLabel("Anonymous The Grand Conqueror");
        userNameLabel.setForeground(Color.black);
        userNameLabel.setOpaque(true);
        userNameLabel.setBackground(Color.white);
        userNameLabel.setBorder(BorderFactory.createLineBorder(Color.black));
        dataPanel.add(userNameLabel, c);

        ++c.gridy;
        c.insets.bottom = INSET;
        userMoneyLabel = new JLabel("...whose pocket is empty!");
        userMoneyLabel.setForeground(Color.black);
        userMoneyLabel.setOpaque(true);
        userMoneyLabel.setBackground(Color.white);
        userMoneyLabel.setBorder(BorderFactory.createLineBorder(Color.black));
        userMoneyLabel.setToolTipText("HexCoin");
        dataPanel.add(userMoneyLabel, c);

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
        tabbedPane.add("New Building", makeBuildingButtonsPanel());
        tabbedPane.add("New Entity", makeEntityButtonsPanel());
        shopPanel.add(tabbedPane, c);
        
        ++c.gridy;
        var label = new JLabel("Shift-click to see info");
        label.setOpaque(true);
        label.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        label.setForeground(Color.DARK_GRAY);
        shopPanel.add(label, c);

        return shopPanel;
    }
    
    private JPanel makeBuildingButtonsPanel()
    {
        var panel = new JPanel(new GridLayout(0, 2));
        for (var type : BuildingType.values())
        {
            panel.add(new BuildingButton(type, invoker));
        }
        return panel;
    }
    
    private JPanel makeEntityButtonsPanel()
    {
        var panel = new JPanel(new GridLayout(0, 2));
        for (var type : EntityType.values())
        {
            panel.add(new EntityButton(type, invoker));
        }
        return panel;
    }
    
    private JPanel makeButtonsPanel()
    {
        var buttonsPanel = new JPanel(new GridBagLayout());
        buttonsPanel.setOpaque(false);

        var c = new GridBagConstraints();
        c.weightx = 1;
        c.weighty = 1;
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.BOTH;

        var undoButton = new JButton("Undo ↺");
        undoButton.setActionCommand("undo");
        undoButton.addActionListener(this);
        buttonsPanel.add(undoButton, c);
        
        undoButton.setEnabled(false);
        undoButton.setToolTipText("(Not supported yet!)");

        c.gridx = 1;
        var redoButton = new JButton("Redo ↻");
        redoButton.setActionCommand("redo");
        redoButton.addActionListener(this);
        buttonsPanel.add(redoButton, c);
        
        redoButton.setEnabled(false);
        redoButton.setToolTipText("(Not supported yet!)");

        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 2;
        var doneButton = new JButton("Done ✓");
        doneButton.setActionCommand("done");
        doneButton.addActionListener(this);
        buttonsPanel.add(doneButton, c);

        return buttonsPanel;
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        switch (e.getActionCommand())
        {
            case "undo" ->
            {
                
            }
            case "redo" ->
            {
                
            }
            case "done" ->
            {
                invoker.invoke(new EndRoundCommand());
            }
        }
    }
    
    void setUserName(String name)
    {
        userNameLabel.setText(name);
    }
    
    void setUserMoney(int money)
    {
        userMoneyLabel.setText(String.format("%d Ħ", money));
    }
}
