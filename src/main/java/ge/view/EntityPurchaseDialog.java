package ge.view;

import ge.entity.*;
import ge.utilities.*;
import ge.view.procedure.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 *
 * @author Kay Jay O'Nail
 */
public class EntityPurchaseDialog extends JDialog implements ActionListener, Spinner.ValueChangeListener
{
    private final JLabel nameLabel;
    private final JLabel iconLabel;
    private final Spinner spinner;
    private final JLabel priceLabel;
    private final JButton button;
    
    private EntityType entity;
    private int number;
    private int budget;
    private Invoker<HiringProcedure> invoker;

    public EntityPurchaseDialog(JFrame frame)
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
        final int max = Entity.MAXIMAL_NUMBER;
        final int min = Entity.MINIMAL_NUMBER;
        final int def = min + (max - min) / 5;
        spinner = new Spinner(new Spinner.Model(def, min, max), new Dimension(300, 50));
        spinner.addValueChangeListener(this);
        contentPane.add(spinner, c);
        
        number = def;
        
        ++c.gridy;
        c.gridwidth = 1;
        priceLabel = new JLabel();
        priceLabel.setBorder(BorderFactory.createTitledBorder("Price"));
        priceLabel.setPreferredSize(new Dimension(100, 50));
        contentPane.add(priceLabel, c);
        
        ++c.gridx;
        button = new JButton("Confirm");
        button.setActionCommand("confirm");
        button.addActionListener(this);
        button.setPreferredSize(new Dimension(100, 50));
        contentPane.add(button, c);
        
        setContentPane(contentPane);

        pack();
        button.requestFocus();
        setResizable(false);
        setLocationRelativeTo(frame);
    }
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (e.getActionCommand().equals("confirm"))
        {
            invoker.invoke(new Command<HiringProcedure>()
            {
                @Override
                public void execute(HiringProcedure executor)
                {
                    executor.advance(number);
                }
            });
        }
    }
    
    private void setPrice(int newPrice)
    {
        priceLabel.setText(String.format("%d Ħ", newPrice));
        priceLabel.setForeground(newPrice > budget ? Color.RED : Color.BLACK);
        button.setEnabled(newPrice <= budget);

    }

    @Override
    public void valueChanged(Spinner.ValueChangeEvent e)
    {
        number = e.newValue;
        setPrice(entity.price(number));
    }
    
    public static class Builder
    {
        private JFrame frame;
        private EntityType entity;
        private int budget;
        private Invoker<HiringProcedure> invoker;
        
        public Builder setFrame(JFrame frame)
        {
            this.frame = frame;
            return this;
        }

        public Builder setEntity(EntityType entity)
        {
            this.entity = entity;
            return this;
        }

        public Builder setBudget(int budget)
        {
            this.budget = budget;
            return this;
        }

        public Builder setInvoker(Invoker<HiringProcedure> invoker)
        {
            this.invoker = invoker;
            return this;
        }
        
        public EntityPurchaseDialog get()
        {
            EntityPurchaseDialog dialog = null;
            if (frame != null && entity != null && invoker != null)
            {
                dialog = new EntityPurchaseDialog(frame);
                dialog.budget = budget;
                dialog.entity = entity;
                dialog.nameLabel.setText(entity.toString());
                dialog.iconLabel.setIcon(entity.icon());
                dialog.setPrice(entity.price(dialog.number));
                dialog.invoker = invoker;
            }
            return dialog;
        }
    }
}
