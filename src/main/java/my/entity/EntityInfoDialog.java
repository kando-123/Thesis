package my.entity;

import java.awt.Component;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
 *
 * @author Kay Jay O'Nail
 */
public class EntityInfoDialog extends JDialog
{
    public EntityInfoDialog(JFrame frame, AbstractEntity entity)
    {
        super(frame, true);
        
        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(300, 300));
        
        JLabel nameLabel = new JLabel(entity.getName());
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPane.add(nameLabel);
        
        JLabel iconLabel = new JLabel(entity.getIcon());
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPane.add(iconLabel);
        
        JTextArea descriptionTextArea = new JTextArea();
        descriptionTextArea.setText(entity.getDescription());
        descriptionTextArea.setOpaque(false);
        descriptionTextArea.setEditable(false);
        descriptionTextArea.setBorder(BorderFactory.createTitledBorder("Description"));
        descriptionTextArea.setLineWrap(true);
        descriptionTextArea.setWrapStyleWord(true);
        contentPane.add(descriptionTextArea);
        
        JTextArea conditionTextArea = new JTextArea();
        conditionTextArea.setText(entity.getCondition());
        conditionTextArea.setOpaque(false);
        conditionTextArea.setEditable(false);
        conditionTextArea.setBorder(BorderFactory.createTitledBorder("Requirements"));
        conditionTextArea.setLineWrap(true);
        conditionTextArea.setWrapStyleWord(true);
        contentPane.add(conditionTextArea);
        
        JTextArea pricingTextArea = new JTextArea();
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
