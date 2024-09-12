package my.field;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import java.util.Set;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import my.command.PursueBuildingCommand;
import my.game.ArrowsManager;
import my.game.Manager;

/**
 *
 * @author Kay Jay O'Nail
 */
public class BuildingSelectionDialog extends JDialog implements ActionListener
{
    private final BuildingField[] allBuildings;
    private int current;
    
    private Manager manager;

    private JLabel nameLabel;
    private JLabel iconLabel;
    private JTextArea descriptionTextArea;
    private JTextArea conditionsTextArea;
    private JTextArea priceTextArea;
    private JButton buyButton;
    
    private int playerMoney;
    private Map<FieldType, Integer> counts;
    private Set<FieldType> erectableBuildings;
    
    private final FieldsManager fieldsManager;
    
    private BuildingSelectionDialog(JFrame frame)
    {
        super(frame, "Select a Building", true);

        fieldsManager = FieldsManager.getInstance();

        allBuildings = new BuildingField[7];
        allBuildings[0] = new VillageField();
        allBuildings[1] = new FarmField();
        allBuildings[2] = new TownField();
        allBuildings[3] = new MineField();
        allBuildings[4] = new FortressField();
        allBuildings[5] = new BarracksField();
        allBuildings[6] = new ShipyardField();

        setContentPane(makeContentPane());
        pack();

        setResizable(false);
    }
    
    private JPanel makeContentPane()
    {
        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
        contentPane.setPreferredSize(new Dimension(400, 300));

        contentPane.add(makeNameLabel());           //  15
        contentPane.add(makeIconPanel());           // 115
        contentPane.add(makeDescriptionTextArea()); //  85
        contentPane.add(makePurchasePanel());       //  85

        return contentPane;
    }
    
    private JComponent makeNameLabel()
    {
        nameLabel = new JLabel();
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        nameLabel.setPreferredSize(new Dimension(400, 15));
        return nameLabel;
    }
    
    private JComponent makeIconPanel()
    {
        JPanel iconPanel = new JPanel();
        iconPanel.setLayout(new BoxLayout(iconPanel, BoxLayout.X_AXIS));
        iconPanel.setPreferredSize(new Dimension(400, 115));

        Insets zeroInsets = new Insets(0, 0, 0, 0);
        ArrowsManager arrowsManager = ArrowsManager.getInstance();

        Icon leftIcon = arrowsManager.getLeftArrowAsIcon();
        JButton leftArrow = new JButton(leftIcon);
        leftArrow.setMargin(zeroInsets);
        leftArrow.setActionCommand("left");
        leftArrow.addActionListener(this);
        iconPanel.add(leftArrow);

        iconLabel = new JLabel();
        iconPanel.add(iconLabel);

        Icon rightIcon = arrowsManager.getRightArrowAsIcon();
        JButton rightArrow = new JButton(rightIcon);
        rightArrow.setMargin(zeroInsets);
        rightArrow.setActionCommand("right");
        rightArrow.addActionListener(this);
        iconPanel.add(rightArrow);
        
        return iconPanel;
    }
    
    private JComponent makeDescriptionTextArea()
    {
        descriptionTextArea = new JTextArea();
        descriptionTextArea.setBorder(BorderFactory.createTitledBorder("Description"));
        descriptionTextArea.setOpaque(false);
        descriptionTextArea.setLineWrap(true);
        descriptionTextArea.setWrapStyleWord(true);
        descriptionTextArea.setEditable(false);
        descriptionTextArea.setPreferredSize(new Dimension(400, 85));
        
        return descriptionTextArea;
    }
    
    private JComponent makePurchasePanel()
    {
        JPanel purchasePanel = new JPanel(new GridBagLayout());
        purchasePanel.setPreferredSize(new Dimension(400, 85));
        
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 3;
        c.weighty = 1;
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.CENTER;

        conditionsTextArea = new JTextArea();
        conditionsTextArea.setBorder(BorderFactory.createTitledBorder("Conditions"));
        conditionsTextArea.setLineWrap(true);
        conditionsTextArea.setWrapStyleWord(true);
        conditionsTextArea.setEditable(false);
        conditionsTextArea.setOpaque(false);
        conditionsTextArea.setPreferredSize(new Dimension(250, 85));
        purchasePanel.add(conditionsTextArea, c.clone());

        c.gridx = 1;
        c.weightx = 1;
        priceTextArea = new JTextArea();
        priceTextArea.setBorder(BorderFactory.createTitledBorder("Price"));
        priceTextArea.setEditable(false);
        priceTextArea.setLineWrap(true);
        priceTextArea.setWrapStyleWord(true);
        priceTextArea.setOpaque(false);
        priceTextArea.setPreferredSize(new Dimension(50, 85));
        purchasePanel.add(priceTextArea, c.clone());

        c.gridx = 2;
        c.weightx = 1;
        c.fill = GridBagConstraints.NONE;
        buyButton = new JButton("Buy");
        buyButton.setActionCommand("buy");
        buyButton.addActionListener(this);
        buyButton.setPreferredSize(new Dimension(70, 40));
        purchasePanel.add(buyButton, c.clone());

        return purchasePanel;
    }

    private void reassignValues()
    {
        BuildingField building = allBuildings[current];
        FieldType type = building.getType();
        nameLabel.setText(type.name());
        iconLabel.setIcon(building.getIcon());
        descriptionTextArea.setText(building.getDescription());
        conditionsTextArea.setText(building.getCondition());
        priceTextArea.setText(String.format("%d Ħ", building.computePrice(counts.get(type))));
        
        boolean isAffordable = counts != null && counts.containsKey(type) && counts.get(type) <= playerMoney;
        boolean isErectable = erectableBuildings != null && erectableBuildings.contains(type);
        buyButton.setEnabled(isAffordable && isErectable);
        
        repaint();
    }
    
    private void setManager(Manager manager)
    {
        this.manager = manager;
    }
    
    private void setPlayerMoney(int money)
    {
        playerMoney = money;
    }
    
    private void setPrices(Map<FieldType, Integer> prices)
    {
        this.counts = prices;
    }
    
    private void setErectableBuildings(Set<FieldType> buildings)
    {
        erectableBuildings = buildings;
        reassignValues();
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        switch (e.getActionCommand())
        {
            case "left" ->
            {
                current = (current > 0) ? current - 1 : allBuildings.length - 1;
                reassignValues();
            }
            case "right" ->
            {
                current = (current < allBuildings.length - 1) ? current + 1 : 0;
                reassignValues();
            }
            case "buy" ->
            {
                manager.passCommand(new PursueBuildingCommand(allBuildings[current]));
            }
        }
    }
    
    public static class Builder
    {
        private JFrame frame;
        private Manager manager;
        private Map<FieldType, Integer> counts;
        private int playerMoney;
        private Set<FieldType> erectableBuildings;
        
        public BuildingSelectionDialog get()
        {
            if (frame != null && manager != null && counts != null && erectableBuildings != null)
            {
                BuildingSelectionDialog dialog = new BuildingSelectionDialog(frame);
                dialog.setManager(manager);
                dialog.setPrices(counts);
                dialog.setPlayerMoney(playerMoney);
                dialog.setErectableBuildings(erectableBuildings);
                dialog.reassignValues();
                return dialog;
            }
            else
            {
                return null;
            }
        }
        
        public void setFrame(JFrame frame)
        {
            this.frame = frame;
        }
        
        public void setManager(Manager manager)
        {
            this.manager = manager;
        }
        
        public void setCounts(Map<FieldType, Integer> counts)
        {
            this.counts = counts;
        }
        
        public void setPlayerMoney(int playerMoney)
        {
            this.playerMoney = playerMoney;
        }
        
        public void setErectableBuildings(Set<FieldType> buildings)
        {
            this.erectableBuildings = buildings;
        }
    }
}
