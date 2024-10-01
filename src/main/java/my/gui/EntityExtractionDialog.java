package my.gui;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import my.command.ExtractEntityCommand;
import my.command.Invoker;
import my.entity.AbstractEntity;
import my.utils.Spinner;
import my.flow.Manager;

/**
 *
 * @author Kay Jay O'Nail
 */
public class EntityExtractionDialog extends JDialog implements ActionListener, Spinner.ValueChangeListener
{
    private final JLabel nameLabel;
    private final JLabel iconLabel;
    private final Spinner spinner;
    private final JTextArea summaryTextArea;

    private Invoker<Manager> invoker;

    public EntityExtractionDialog(JFrame frame, AbstractEntity entity)
    {
        super(frame, "Extract a Troop", true);

        Container contentPane = new JPanel(new GridBagLayout());
        contentPane.setPreferredSize(new Dimension(300, 200));

        var c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;

        nameLabel = new JLabel(entity.getName());
        nameLabel.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        contentPane.add(nameLabel, c);

        ++c.gridy;
        iconLabel = new JLabel(entity.getIcon());
        contentPane.add(iconLabel, c);

        ++c.gridy;
        int number = entity.getNumber();
        Spinner.Model model = new Spinner.Model(Math.max(number / 2, 1), 1, number - 1);
        spinner = new Spinner(model, new Dimension(300, 50));
        spinner.addValueChangeListener(this);
        contentPane.add(spinner, c);
        
        ++c.gridy;
        summaryTextArea = new JTextArea();
        contentPane.add(summaryTextArea, c);
        
        ++c.gridx;
        JButton button = new JButton("Extract");
        button.setActionCommand("extract");
        button.addActionListener(this);
        contentPane.add(button, c);
        
        setContentPane(contentPane);
        
        pack();
        setResizable(false);
        setLocationRelativeTo(frame);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (e.getActionCommand().equals("extract"))
        {
            invoker.invoke(new ExtractEntityCommand(spinner.getValue()));
        }
    }

    @Override
    public void valueChanged(Spinner.ValueChangeEvent e)
    {
        throw new UnsupportedOperationException("Not supported yet."); 
    }
}
