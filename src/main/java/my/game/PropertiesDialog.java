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
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
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
public class PropertiesDialog extends JDialog implements ActionListener
{
    private final List<FieldType> properties;

    private JLabel nameLabel;
    private JLabel propertyLabel;
    private JTextArea descriptionTextArea;
    private JTextArea conditionsTextArea;
    private JTextArea priceTextArea;
    private final FieldsManager fieldsManager;

    public PropertiesDialog(JFrame parent)
    {
        super(parent, "Purchase a Property", true);

        fieldsManager = FieldsManager.getInstance();

        properties = new LinkedList<>();
        for (var fieldType : FieldType.values())
        {
            if (fieldType.isPurchasable())
            {
                properties.add(fieldType);
            }
        }

        setContentPane(makeContentPane());
        pack();

        setResizable(false);
    }

    private final static Dimension PREFERRED_SIZE = new Dimension(350, 200);

    private JPanel makeContentPane()
    {
        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
        contentPane.setPreferredSize(PREFERRED_SIZE);

        FieldType firstProperty = properties.getFirst();

        nameLabel = new JLabel(firstProperty.name());
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
        iconPanel.setPreferredSize(new Dimension(350, 100));

        Insets zeroInsets = new Insets(0, 0, 0, 0);
        ArrowsManager arrowsManager = ArrowsManager.getInstance();

        Icon leftIcon = arrowsManager.getLeftArrowAsIcon();
        JButton leftArrow = new JButton(leftIcon);
        leftArrow.setMargin(zeroInsets);
        leftArrow.setActionCommand("left");
        leftArrow.addActionListener(this);
        iconPanel.add(leftArrow);

        FieldType firstProperty = properties.getFirst();
        propertyLabel = new JLabel(fieldsManager.getFieldAsIcon(firstProperty));
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
        FieldType firstProperty = properties.getFirst();
        
        descriptionTextArea = new JTextArea(firstProperty.getDescription());
        descriptionTextArea.setBorder(BorderFactory.createTitledBorder("Description"));
        descriptionTextArea.setOpaque(false);
        descriptionTextArea.setLineWrap(true);
        descriptionTextArea.setWrapStyleWord(true);
        descriptionTextArea.setEditable(false);
        descriptionTextArea.setPreferredSize(new Dimension(350, 50));
        
        return descriptionTextArea;
    }
    
    private JPanel makePurchasePanel()
    {
        FieldType firstProperty = properties.getFirst();
        
        JPanel purchasePanel = new JPanel(new GridBagLayout());
        purchasePanel.setPreferredSize(new Dimension(350, 50));
        
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 3;
        c.weighty = 1;
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.CENTER;

        conditionsTextArea = new JTextArea(firstProperty.getConditions());
        conditionsTextArea.setBorder(BorderFactory.createTitledBorder("Conditions"));
        conditionsTextArea.setLineWrap(true);
        conditionsTextArea.setWrapStyleWord(true);
        conditionsTextArea.setEditable(false);
        conditionsTextArea.setOpaque(false);
        conditionsTextArea.setPreferredSize(new Dimension(210, 50));
        purchasePanel.add(conditionsTextArea, c);

        c.gridx = 1;
        c.weightx = 1;
        priceTextArea = new JTextArea("Ħ");
        priceTextArea.setBorder(BorderFactory.createTitledBorder("Price"));
        priceTextArea.setEditable(false);
        priceTextArea.setLineWrap(true);
        priceTextArea.setWrapStyleWord(true);
        priceTextArea.setOpaque(false);
        priceTextArea.setPreferredSize(new Dimension(70, 50));
        purchasePanel.add(priceTextArea);

        c.gridx = 2;
        c.weightx = 1;
        JButton buyButton = new JButton("Buy!");
        buyButton.setActionCommand("buy");
        buyButton.addActionListener(this);
        buyButton.setPreferredSize(new Dimension(70, 50));
        purchasePanel.add(buyButton);

        return purchasePanel;
    }

    private void reassignValues()
    {
        var current = properties.getFirst();
        nameLabel.setText(current.name());
        propertyLabel.setIcon(fieldsManager.getFieldAsIcon(current));
        descriptionTextArea.setText(current.getDescription());
        conditionsTextArea.setText(current.getConditions());
        repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        switch (e.getActionCommand())
        {
            case "left" ->
            {
                var previous = properties.removeLast();
                properties.addFirst(previous);

                reassignValues();
            }
            case "right" ->
            {
                var previous = properties.removeFirst();
                properties.addLast(previous);

                reassignValues();
            }
            case "buy" ->
            {
                System.out.println("Buy");
            }
        }
    }
}
