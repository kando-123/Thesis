package ge.view.procedure;

import ge.entity.*;
import ge.utilities.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 *
 * @author Kay Jay O'Nail
 */
public class ExtractingDialog extends JDialog implements ActionListener, Spinner.ValueChangeListener
{
    private final JLabel iconLabel;
    private final Spinner spinner;
    private final JTextArea summaryTextArea;
    
    private final Invoker<ExtractingProcedure> invoker;
    
    private final int number;
    
    private static final String PATTERN = "%d soldier%s will be extracted.\n%d will remain.";
    
    private String formatPattern(int extract)
    {
        return PATTERN.formatted(extract, extract > 1 ? "s" : "", number - extract);
    }

    ExtractingDialog(JFrame frame, EntityType entity, int number, Invoker<ExtractingProcedure> invoker)
    {
        super(frame, "Extract", true);
        this.number = number;
        this.invoker = invoker;
        
        var contentPane = new JPanel(new GridBagLayout());
        contentPane.setPreferredSize(new Dimension(300, 200));

        var c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1;
        c.weighty = 1;
        c.gridwidth = 2;
        c.gridheight = 1;
        iconLabel = new JLabel(entity.icon());
        iconLabel.setPreferredSize(new Dimension(300, 75));
        contentPane.add(iconLabel, c);

        ++c.gridy;
        int extract = Math.max(number / 2, 1);
        var model = new Spinner.Model(extract, 1, number - 1);
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
        var button = new JButton("Extract");
        button.setActionCommand("extract");
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
        if (e.getActionCommand().equals("extract"))
        {
            invoker.invoke(new Command<ExtractingProcedure>()
            {
                @Override
                public void execute(ExtractingProcedure executor)
                {
                    executor.advance(spinner.getValue());
                }
            });
        }
    }

    @Override
    public void valueChanged(Spinner.ValueChangeEvent e)
    {
        summaryTextArea.setText(formatPattern(e.newValue));
    }
}
