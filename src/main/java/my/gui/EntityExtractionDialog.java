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
    
    private final AbstractEntity entity;
    
    private static final String PATTERN = "%d soldier%s will be extracted.\n%d will remain.";
    
    private String formatPattern(int extract)
    {
        int remainder = entity.getNumber() - extract;
        return PATTERN.formatted(extract, extract > 1 ? "s" : "", remainder);
    }

    public EntityExtractionDialog(JFrame frame, AbstractEntity entity)
    {
        super(frame, "Extract a Troop", true);
        this.entity = entity;

        Container contentPane = new JPanel(new GridBagLayout());
        contentPane.setPreferredSize(new Dimension(300, 200));

        var c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1;
        c.weighty = 1;
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.gridheight = 1;
        nameLabel = new JLabel(entity.getName());
        nameLabel.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        nameLabel.setPreferredSize(new Dimension(300, 25));
        contentPane.add(nameLabel, c);

        ++c.gridy;
        iconLabel = new JLabel(entity.getIcon());
        iconLabel.setPreferredSize(new Dimension(300, 75));
        contentPane.add(iconLabel, c);

        ++c.gridy;
        int number = entity.getNumber();
        int extract = Math.max(number / 2, 1);
        Spinner.Model model = new Spinner.Model(extract, 1, number - 1);
        spinner = new Spinner(model, new Dimension(300, 50));
        spinner.addValueChangeListener(this);
        contentPane.add(spinner, c);
        
        ++c.gridy;
        c.gridwidth = 1;
        summaryTextArea = new JTextArea();
        summaryTextArea.setText(formatPattern(extract));
        summaryTextArea.setEditable(false);
        summaryTextArea.setLineWrap(true);
        summaryTextArea.setWrapStyleWord(true);
        summaryTextArea.setPreferredSize(new Dimension(200, 50));
        contentPane.add(summaryTextArea, c);
        
        ++c.gridx;
        JButton button = new JButton("Extract");
        button.setActionCommand("extract");
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
        if (e.getActionCommand().equals("extract"))
        {
            invoker.invoke(new ExtractEntityCommand(spinner.getValue()));
        }
    }

    @Override
    public void valueChanged(Spinner.ValueChangeEvent e)
    {
        summaryTextArea.setText(formatPattern(e.newValue));
    }
}
