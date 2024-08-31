package my.game;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import my.units.ArrowsManager;
import my.units.FieldType;
import my.units.FieldsManager;

/**
 *
 * @author Kay Jay O'Nail
 */
public class BuildingSelectionDialog extends JDialog implements ActionListener
{
    private final Manager manager;
    
    private final List<FieldType> allBuildings;

    private JLabel nameLabel;
    private JLabel propertyLabel;
    private JTextArea descriptionTextArea;
    private JTextArea conditionsTextArea;
    private JTextArea priceTextArea;
    private JButton buyButton;
    
    private final Map<FieldType, Integer> prices;
    private int playerMoney;
    private Set<FieldType> erectableBuildings;
    
    private final FieldsManager fieldsManager;

    public BuildingSelectionDialog(Master master, Manager manager, Map<FieldType, Integer> prices)
    {
        super(master, "Select a Building", true);
        this.manager = manager;
        this.prices = prices;

        fieldsManager = FieldsManager.getInstance();

        allBuildings = new LinkedList<>();
        for (var fieldType : FieldType.values())
        {
            if (fieldType.isBuilding())
            {
                allBuildings.add(fieldType);
            }
        }

        setContentPane(makeContentPane());
        reassignValues();
        pack();

        setResizable(false);
    }
    
    private JPanel makeContentPane()
    {
        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
        contentPane.setPreferredSize(new Dimension(400, 300));

        nameLabel = new JLabel();
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPane.add(nameLabel);
        
        contentPane.add(makeIconPanel());
        contentPane.add(makeDescriptionTextArea());
        contentPane.add(makePurchasePanel());

        return contentPane;
    }
    
    private JPanel makeIconPanel()
    {
        JPanel iconPanel = new JPanel();
        iconPanel.setLayout(new BoxLayout(iconPanel, BoxLayout.X_AXIS));
        iconPanel.setPreferredSize(new Dimension(400, 120));

        Insets zeroInsets = new Insets(0, 0, 0, 0);
        ArrowsManager arrowsManager = ArrowsManager.getInstance();

        Icon leftIcon = arrowsManager.getLeftArrowAsIcon();
        JButton leftArrow = new JButton(leftIcon);
        leftArrow.setMargin(zeroInsets);
        leftArrow.setActionCommand("left");
        leftArrow.addActionListener(this);
        iconPanel.add(leftArrow);

        propertyLabel = new JLabel();
        iconPanel.add(propertyLabel);

        Icon rightIcon = arrowsManager.getRightArrowAsIcon();
        JButton rightArrow = new JButton(rightIcon);
        rightArrow.setMargin(zeroInsets);
        rightArrow.setActionCommand("right");
        rightArrow.addActionListener(this);
        iconPanel.add(rightArrow);
        
        return iconPanel;
    }
    
    private JTextArea makeDescriptionTextArea()
    {
        descriptionTextArea = new JTextArea();
        descriptionTextArea.setBorder(BorderFactory.createTitledBorder("Description"));
        descriptionTextArea.setOpaque(false);
        descriptionTextArea.setLineWrap(true);
        descriptionTextArea.setWrapStyleWord(true);
        descriptionTextArea.setEditable(false);
        descriptionTextArea.setPreferredSize(new Dimension(400, 90));
        
        return descriptionTextArea;
    }
    
    private JPanel makePurchasePanel()
    {
        JPanel purchasePanel = new JPanel(new GridBagLayout());
        purchasePanel.setPreferredSize(new Dimension(400, 90));
        
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
        conditionsTextArea.setPreferredSize(new Dimension(300, 90));
        purchasePanel.add(conditionsTextArea, c);

        c.gridx = 1;
        c.weightx = 1;
        priceTextArea = new JTextArea("? Ħ");
        priceTextArea.setBorder(BorderFactory.createTitledBorder("Price"));
        priceTextArea.setEditable(false);
        priceTextArea.setLineWrap(true);
        priceTextArea.setWrapStyleWord(true);
        priceTextArea.setOpaque(false);
        priceTextArea.setPreferredSize(new Dimension(50, 90));
        purchasePanel.add(priceTextArea);

        c.gridx = 2;
        c.weightx = 1;
        buyButton = new JButton("Build");
        buyButton.setActionCommand("build");
        buyButton.addActionListener(this);
        buyButton.setPreferredSize(new Dimension(50, 90));
        purchasePanel.add(buyButton);

        return purchasePanel;
    }

    private void reassignValues()
    {
        FieldType current = allBuildings.getFirst();
        nameLabel.setText(current.name());
        propertyLabel.setIcon(fieldsManager.getFieldAsIcon(current));
        descriptionTextArea.setText(current.getDescription());
        conditionsTextArea.setText(current.getConditions());
        priceTextArea.setText(String.format("%d Ħ", prices.get(current)));
        
        boolean isAffordable = prices != null && prices.containsKey(current) && prices.get(current) <= playerMoney;
        boolean isErectable = erectableBuildings != null && erectableBuildings.contains(current);
        buyButton.setEnabled(isAffordable && isErectable);
        
        repaint();
    }
    
    public void setPlayerMoney(int money)
    {
        playerMoney = money;
    }
    
    public void setErectableBuildings(Set<FieldType> buildings)
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
                var previous = allBuildings.removeLast();
                allBuildings.addFirst(previous);

                reassignValues();
            }
            case "right" ->
            {
                var previous = allBuildings.removeFirst();
                allBuildings.addLast(previous);

                reassignValues();
            }
            case "buy" ->
            {
//                manager.buildingSelected(properties.getFirst());
            }
        }
    }
}
