package my.entity;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import my.command.PursueBuildingCommand;
import my.command.PursueHiringCommand;
import my.game.ArrowsManager;
import my.game.Manager;

/**
 *
 * @author Kay Jay O'Nail
 */
public class EntitySelectionDialog extends JDialog implements ActionListener, ChangeListener
{
    private final EntityType[] allEntities;
    private int current;

    private Manager manager;

    private JLabel nameLabel;
    private JLabel iconLabel;

    private JTextArea descriptionTextArea;
    private JSpinner numberSpinner;
    private JTextArea priceTextArea;
    private JButton buyButton;

    private int playerMoney;
    private final EntityPriceCalculator priceCalculator;

    private final EntitiesManager entitiesManager;

    public EntitySelectionDialog(JFrame frame)
    {
        super(frame, "Select an entity", true);

        allEntities = EntityType.values().clone();

        priceCalculator = EntityPriceCalculator.getInstance();
        entitiesManager = EntitiesManager.getInstance();

        setContentPane(makeContentPane());
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
        c.weightx = 1;
        c.weighty = 1;
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.CENTER;
        
        SpinnerModel model = new SpinnerNumberModel(10, 1, 99, 5);
        numberSpinner = new JSpinner(model);
        numberSpinner.addChangeListener(this);
        purchasePanel.add(numberSpinner, c);
        
        c.gridx = 1;
        c.weightx = 1;
        priceTextArea = new JTextArea();
        priceTextArea.setBorder(BorderFactory.createTitledBorder("Price"));
        priceTextArea.setEditable(false);
        priceTextArea.setLineWrap(true);
        priceTextArea.setWrapStyleWord(true);
        priceTextArea.setOpaque(false);
        priceTextArea.setPreferredSize(new Dimension(50, 90));
        purchasePanel.add(priceTextArea);
        
        c.gridx = 2;
        c.weightx = 1;
        buyButton = new JButton("Hire");
        buyButton.setActionCommand("hire");
        buyButton.addActionListener(this);
        buyButton.setPreferredSize(new Dimension(50, 90));
        purchasePanel.add(buyButton);
        
        return purchasePanel;
    }
    
    public void setManager(Manager manager)
    {
        this.manager = manager;
    }
    
    public void setPlayerMoney(int money)
    {
        playerMoney = money;
    }
    
    public void reassignValues()
    {
        EntityType entity = allEntities[current];
        nameLabel.setText(entity.name());
        iconLabel.setIcon(entitiesManager.getEntityAsIcon(entity));
        descriptionTextArea.setText(entity.getDescription());
        numberSpinner.setValue(10);
        resetPrice();
        
        repaint();
    }
    
    private void resetPrice()
    {
        SpinnerNumberModel model = (SpinnerNumberModel) numberSpinner.getModel();
        int value = model.getNumber().intValue();
        int price = priceCalculator.getPriceFor(value, allEntities[current]);
        priceTextArea.setText(String.format("%d Ħ", price));
        buyButton.setEnabled(price <= playerMoney);
    }

    @Override
    public void stateChanged(ChangeEvent e)
    {
        resetPrice();
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        switch (e.getActionCommand())
        {
            case "left" ->
            {
                current = (current > 0) ? current - 1 : allEntities.length - 1;
                reassignValues();
            }
            case "right" ->
            {
                current = (current < allEntities.length - 1) ? current + 1 : 0;
                reassignValues();
            }
            case "build" ->
            {
                manager.passCommand(new PursueHiringCommand(allEntities[current]));
            }
        }
    }
}
