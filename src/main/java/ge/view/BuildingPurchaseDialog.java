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
        var button = new JButton("Confirm");
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
            invoker.invoke(new PursueBuildingCommand());
        }
    }
    
    public static class Builder
    {
        private JFrame frame;
        private BuildingType building;
        private int price;
        private Invoker<ViewManager> invoker;
        
        public Builder setFrame(JFrame frame)
        {
            this.frame = frame;
            return this;
        }
        
        public Builder setBuilding(BuildingType building)
        {
            this.building = building;
            return this;
        }
        
        public Builder setPrice(int price)
        {
            this.price = price;
            return this;
        }
        
        public Builder setInvoker(Invoker<ViewManager> invoker)
        {
            this.invoker = invoker;
            return this;
        }
        
        public BuildingPurchaseDialog get()
        {
            BuildingPurchaseDialog dialog = null;
            if (frame != null && building != null && price > 0 && invoker != null)
            {
                dialog = new BuildingPurchaseDialog(frame);
                dialog.nameLabel.setText(building.toString());
                dialog.iconLabel.setIcon(building.icon());
                dialog.priceLabel.setText(String.format("%d Ħ", price));
                dialog.invoker = invoker;
            }
            return dialog;
        }
    }
}
