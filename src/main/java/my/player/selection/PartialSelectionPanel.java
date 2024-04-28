package my.player.selection;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

/**
 *
 * @author Kay Jay O'Nail
 */
public class PartialSelectionPanel extends JPanel implements ActionListener, ColorSelectionListener
{
    private final List<JRadioButton> radioButtons;
    private final List<JTextField> textFields;

    public PartialSelectionPanel()
    {
        super(new GridBagLayout());

        radioButtons = new ArrayList<>(PlayerColor.PLAYERS_COUNT);
        textFields = new ArrayList<>(PlayerColor.PLAYERS_COUNT);
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        add(new JLabel("<html><p align=\"center\">Number<br>of Players</p></html>"), c);

        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 0;
        add(new JLabel("Name"), c);

        ButtonGroup group = new ButtonGroup();

        final int initialNumber = 2;
        for (int i = 0; i < PlayerColor.PLAYERS_COUNT; ++i)
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
                    if (index >= 1 && index <= PlayerColor.PLAYERS_COUNT)
                    {
                        System.out.println(index);
                        for (int i = 0; i < index; ++i)
                        {
                            textFields.get(i).setEnabled(true);
                        }
                        for (int i = index; i < textFields.size(); ++i)
                        {
                            textFields.get(i).setEnabled(false);
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

    @Override
    public void colorSelected(PlayerColor color, ColorModel skip)
    {
        
    }

    @Override
    public void colorDeselected(PlayerColor color, ColorModel skip)
    {
        
    }
}
