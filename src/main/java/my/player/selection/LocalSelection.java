package my.player.selection;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.DefaultComboBoxModel;

/**
 *
 * @author Kay Jay O'Nail
 */
public class LocalSelection extends JPanel implements ActionListener
{
    private final List<JRadioButton> radioButtons;
    private final List<JTextField> textFields;
    private final List<JComboBox> comboBoxes;
    private final List<DefaultComboBoxModel> models;
    
    public LocalSelection()
    {
        super (new GridBagLayout());
        
        int playersCount = PlayerColor.values().length;
        radioButtons = new ArrayList<>(playersCount);
        textFields = new ArrayList<>(playersCount);
        comboBoxes = new ArrayList<>(playersCount);
        models = new ArrayList<>(playersCount);
        
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        add(new JLabel("<html><p align=\"center\">Number<br>of Players</p></html>"), c);
        
        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 0;
        add(new JLabel("Name"), c);
        
        c = new GridBagConstraints();
        c.gridx = 2;
        c.gridy = 0;
        add(new JLabel("Color"), c);
        
        ButtonGroup group = new ButtonGroup();
        
        final int initialNumber = 2;
        for (int i = 0; i < PlayerColor.values().length; ++i)
        {
            c = new GridBagConstraints();
            c.gridx = 0;
            c.gridy = i + 1;
            JRadioButton radioButton = new JRadioButton(String.valueOf(i + 1));
            radioButton.setActionCommand("R%d".formatted(i + 1));
            radioButton.addActionListener(this);
            group.add(radioButton);
            radioButtons.add(radioButton);
            add(radioButton, c);
            
            c = new GridBagConstraints();
            c.gridx = 1;
            c.gridy = i + 1;
            JTextField textField = new JTextField("Player%d".formatted(i + 1), 12);
            textField.setEnabled(i < initialNumber);
            textFields.add(textField);
            add(textField, c);
            
            DefaultComboBoxModel model = new DefaultComboBoxModel<String>();
            model.addElement("Random");
            for (var color : PlayerColor.values())
            {
                String name = color.name();
                String nameFormatted = name.substring(0, 1).concat(name.substring(1).toLowerCase());
                model.addElement(nameFormatted);
            }
            models.add(model);
            
            c = new GridBagConstraints();
            c.gridx = 2;
            c.gridy = i + 1;
            JComboBox comboBox = new JComboBox(model);
            comboBox.setEnabled(i < initialNumber);
            comboBoxes.add(comboBox);
            add(comboBox, c);
        }
        radioButtons.get(initialNumber - 1).setSelected(true);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        String actionCommand = e.getActionCommand();
        String initial = actionCommand.substring(0, 1);
        switch (initial)
        {
            case "R" ->
            {
                try
                {
                    String number = actionCommand.substring(1);
                    int index = Integer.parseInt(number);
                    if (index >= 1 && index <= PlayerColor.values().length)
                    {
                        System.out.println(index);
                        for (int i = 0; i < textFields.size(); ++i)
                        {
                            textFields.get(i).setEnabled(i < index);
                            comboBoxes.get(i).setEnabled(i < index);
                        }
                    }
                }
                catch (NumberFormatException n)
                {
                    System.err.println("Impossible happened!");
                }
            }
        }
    }
}
