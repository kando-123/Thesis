package my.gui;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import my.command.Invoker;
import my.command.PursueHiringCommand;
import my.entity.AbstractEntity;
import my.utils.Spinner;
import my.flow.Manager;

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

    private AbstractEntity entity;
    private int budget;
    private Invoker<Manager> invoker;

    private EntityPurchaseDialog(JFrame frame)
    {
        super(frame, true);

        Container contentPane = new JPanel(new GridBagLayout());
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
        Spinner.Model model = new Spinner.Model(AbstractEntity.DEFAULT_NUMBER,
                AbstractEntity.MINIMAL_NUMBER,
                AbstractEntity.MAXIMAL_NUMBER);
        spinner = new Spinner(model, new Dimension(300, 50));
        spinner.addValueChangeListener(this);
        contentPane.add(spinner, c);

        ++c.gridy;
        c.gridwidth = 1;
        priceLabel = new JLabel();
        priceLabel.setBorder(BorderFactory.createTitledBorder("Price"));
        priceLabel.setPreferredSize(new Dimension(100, 50));
        contentPane.add(priceLabel, c);

        ++c.gridx;
        button = new JButton("Buy");
        button.setActionCommand("buy");
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
        if (e.getActionCommand().equals("buy"))
        {
            int number = entity.getNumber();
            entity.setMorale(number / 5);
            invoker.invoke(new PursueHiringCommand(entity));
        }
    }

    private void setEntity(AbstractEntity newEntity)
    {
        entity = newEntity;
        nameLabel.setText(entity.getName());
        iconLabel.setIcon(entity.getIcon());
        spinner.setValue(entity.getNumber());
        setPrice(entity.computePrice());
    }

    private void setBudget(int newBudget)
    {
        budget = newBudget;
    }

    private void setInvoker(Invoker<Manager> newInvoker)
    {
        invoker = newInvoker;
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
        entity.setNumber(e.newValue);
        setPrice(entity.computePrice());
    }

    public static class Builder
    {
        private JFrame frame;
        private AbstractEntity entity;
        private int budget;
        private Invoker<Manager> invoker;

        public void setFrame(JFrame frame)
        {
            this.frame = frame;
        }

        public void setEntity(AbstractEntity entity)
        {
            this.entity = entity;
        }

        public void setBudget(int budget)
        {
            this.budget = budget;
        }

        public void setInvoker(Invoker<Manager> invoker)
        {
            this.invoker = invoker;
        }

        public EntityPurchaseDialog get()
        {
            EntityPurchaseDialog dialog = null;
            if (frame != null && entity != null && invoker != null)
            {
                dialog = new EntityPurchaseDialog(frame);
                dialog.setBudget(budget);
                dialog.setEntity(entity);
                dialog.setInvoker(invoker);
            }
            return dialog;
        }
    }
}
