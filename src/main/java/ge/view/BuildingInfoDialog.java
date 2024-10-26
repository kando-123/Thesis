package ge.view;

import ge.field.*;
import java.awt.*;
import javax.swing.*;

/**
 *
 * @author Kay Jay O'Nail
 */
public class BuildingInfoDialog extends JDialog
{
    public BuildingInfoDialog(JFrame frame, BuildingType building)
    {
        super(frame, true);
        
        var contentPane = new JPanel();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(300, 300));
        
        var nameLabel = new JLabel(building.toString());
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPane.add(nameLabel);
        
        var iconLabel = new JLabel(building.getIcon());
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPane.add(iconLabel);
        
        var descriptionTextArea = new JTextArea();
        descriptionTextArea.setText(building.getDescription());
        descriptionTextArea.setOpaque(false);
        descriptionTextArea.setEditable(false);
        descriptionTextArea.setBorder(BorderFactory.createTitledBorder("Description"));
        descriptionTextArea.setLineWrap(true);
        descriptionTextArea.setWrapStyleWord(true);
        contentPane.add(descriptionTextArea);
        
        var conditionTextArea = new JTextArea();
        conditionTextArea.setText(building.getConditions());
        conditionTextArea.setOpaque(false);
        conditionTextArea.setEditable(false);
        conditionTextArea.setBorder(BorderFactory.createTitledBorder("Requirements"));
        conditionTextArea.setLineWrap(true);
        conditionTextArea.setWrapStyleWord(true);
        contentPane.add(conditionTextArea);
        
        var pricingTextArea = new JTextArea();
        pricingTextArea.setText(building.getPricing());
        pricingTextArea.setOpaque(false);
        pricingTextArea.setEditable(false);
        pricingTextArea.setBorder(BorderFactory.createTitledBorder("Price"));
        pricingTextArea.setLineWrap(true);
        pricingTextArea.setWrapStyleWord(true);
        contentPane.add(pricingTextArea);
        
        setContentPane(contentPane);
        pack();
        setLocationRelativeTo(frame);
        setResizable(false);
    }
}
