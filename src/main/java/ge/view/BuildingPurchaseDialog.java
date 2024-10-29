package ge.view;

import ge.field.*;
import ge.utilities.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 *
 * @author Kay Jay O'Nail
 */
public class BuildingPurchaseDialog extends JDialog implements ActionListener
{
    private final JLabel nameLabel;
    private final JLabel iconLabel;
    private final JLabel priceLabel;
    
    private BuildingType building;
    private Invoker<ViewManager> invoker;
    
    private BuildingPurchaseDialog(JFrame frame)
    {
        super(frame, true);
        
        var contentPane = new JPanel(new GridBagLayout());
        contentPane.setPreferredSize(new Dimension(300, 200));
        
        var c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1;
        c.weighty = 1;
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.gridheight = 1;
        nameLabel = new JLabel();
        nameLabel.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        contentPane.add(nameLabel, c);
        
        ++c.gridy;
        iconLabel = new JLabel();
        contentPane.add(iconLabel, c);
        
        ++c.gridy;
        c.gridwidth = 1;
        priceLabel = new JLabel();
        priceLabel.setBorder(BorderFactory.createTitledBorder("Price"));
        priceLabel.setPreferredSize(new Dimension(100, 50));
        contentPane.add(priceLabel, c);
        
        ++c.gridx;
        JButton button = new JButton("Confirm");
        button.setActionCommand("confirm");
        button.addActionListener(this);
        button.setPreferredSize(new Dimension(100, 50));
        contentPane.add(button, c);
        
        setContentPane(contentPane);
        
        pack();
        setResizable(false);
        setLocationRelativeTo(frame);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (e.getActionCommand().equals("confirm"))
        {
            invoker.invoke(null);
        }
    }
}
