package my.field;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import my.command.PursueBuildingCommand;
import my.main.Manager;

/**
 *
 * @author Kay Jay O'Nail
 */
public class BuildingPurchaseDialog extends JDialog implements ActionListener
{
    private final JLabel nameLabel;
    private final JLabel iconLabel;
    private final JLabel priceLabel;
    
    private BuildingField building;
    private Manager manager;
    
    private BuildingPurchaseDialog(JFrame frame)
    {
        super(frame, true);
        
        JPanel contentPane = new JPanel(new GridBagLayout());
        contentPane.setPreferredSize(new Dimension(300, 200));
        
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1;
        c.weighty = 1;
        c.gridwidth = GridBagConstraints.REMAINDER;
        nameLabel = new JLabel();
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPane.add(nameLabel, c.clone());
        
        ++c.gridy;
        iconLabel = new JLabel();
        contentPane.add(iconLabel, c.clone());
        
        ++c.gridy;
        c.gridwidth = 1;
        priceLabel = new JLabel();
        priceLabel.setBorder(BorderFactory.createTitledBorder("Price"));
        priceLabel.setPreferredSize(new Dimension(100, 50));
        contentPane.add(priceLabel, c.clone());
        
        ++c.gridx;
        JButton button = new JButton("Buy");
        button.setActionCommand("buy");
        button.addActionListener(this);
        button.setPreferredSize(new Dimension(100, 50));
        contentPane.add(button, c.clone());
        
        setContentPane(contentPane);
        
        pack();
        setResizable(false);
        setLocationRelativeTo(frame);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (e.getActionCommand().equals("buy"))
        {
            manager.passCommand(new PursueBuildingCommand(building));
        }
    }
    
    private void setBuilding(BuildingField newBuilding)
    {
        building = newBuilding;
        nameLabel.setText(building.getName());
        iconLabel.setIcon(building.getIcon());
    }
    
    private void setManager(Manager newManager)
    {
        manager = newManager;
    }
    
    private void setPrice(int newPrice)
    {
        priceLabel.setText(String.format("%d Ħ", newPrice));
    }
    
    public static class Builder
    {
        private JFrame frame;
        private BuildingField building;
        private int price;
        private Manager manager;
        
        public void setFrame(JFrame frame)
        {
            this.frame = frame;
        }
        
        public void setBuilding(BuildingField building)
        {
            this.building = building;
        }
        
        public void setPrice(int price)
        {
            this.price = price;
        }
        
        public void setManager(Manager manager)
        {
            this.manager = manager;
        }
        
        public BuildingPurchaseDialog get()
        {
            BuildingPurchaseDialog dialog = null;
            if (frame != null && building != null && manager != null)
            {
                dialog = new BuildingPurchaseDialog(frame);
                dialog.setBuilding(building);
                dialog.setPrice(price);
                dialog.setManager(manager);
            }
            return dialog;
        }
    }
}
