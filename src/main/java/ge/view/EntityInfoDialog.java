package ge.view;

import ge.entity.*;
import java.awt.*;
import javax.swing.*;

/**
 *
 * @author Kay Jay O'Nail
 */
public class EntityInfoDialog extends JDialog
{
    public EntityInfoDialog(JFrame frame, EntityType entity)
    {
        super(frame, true);
        
        var contentPane = new JPanel();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(300, 300));
        
        var nameLabel = new JLabel(entity.toString());
        nameLabel.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        contentPane.add(nameLabel);
        
        var iconLabel = new JLabel(entity.getIcon());
        iconLabel.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        contentPane.add(iconLabel);
        
        var descriptionTextArea = new JTextArea();
        descriptionTextArea.setText(entity.getDescription());
        descriptionTextArea.setOpaque(false);
        descriptionTextArea.setEditable(false);
        descriptionTextArea.setBorder(BorderFactory.createTitledBorder("Description"));
        descriptionTextArea.setLineWrap(true);
        descriptionTextArea.setWrapStyleWord(true);
        contentPane.add(descriptionTextArea);
        
        var conditionTextArea = new JTextArea();
        conditionTextArea.setText(entity.getConditions());
        conditionTextArea.setOpaque(false);
        conditionTextArea.setEditable(false);
        conditionTextArea.setBorder(BorderFactory.createTitledBorder("Requirements"));
        conditionTextArea.setLineWrap(true);
        conditionTextArea.setWrapStyleWord(true);
        contentPane.add(conditionTextArea);
        
        var pricingTextArea = new JTextArea();
        pricingTextArea.setText(entity.getPricing());
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
