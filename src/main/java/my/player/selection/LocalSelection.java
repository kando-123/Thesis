package my.player.selection;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.MutableComboBoxModel;
import javax.swing.event.ListDataListener;

class Model<E> implements MutableComboBoxModel<E>
{
    @Override
    public void addElement(E item)
    {
        
    }

    @Override
    public void removeElement(Object obj)
    {
        
    }

    @Override
    public void insertElementAt(E item, int index)
    {
        
    }

    @Override
    public void removeElementAt(int index)
    {
        
    }

    @Override
    public void setSelectedItem(Object anItem)
    {
        
    }

    @Override
    public Object getSelectedItem()
    {
        
    }

    @Override
    public int getSize()
    {
        
    }

    @Override
    public E getElementAt(int index)
    {
        
    }

    @Override
    public void addListDataListener(ListDataListener l)
    {
        
    }

    @Override
    public void removeListDataListener(ListDataListener l)
    {
        
    }
    
}

/**
 *
 * @author Kay Jay O'Nail
 */
public class LocalSelection extends JPanel implements ActionListener
{
    private final List<JRadioButton> radioButtons;
    private final List<JTextField> textFields;
    private final List<JComboBox> comboBoxes;
    
    public LocalSelection()
    {
        super (new GridBagLayout());
        
        radioButtons = new ArrayList<>(PlayerColor.values().length);
        textFields = new ArrayList<>(PlayerColor.values().length);
        comboBoxes = new ArrayList<>(PlayerColor.values().length);
        
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
            radioButton.setActionCommand(String.valueOf(i + 1));
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
            
            c = new GridBagConstraints();
            c.gridx = 2;
            c.gridy = i + 1;
            JComboBox comboBox = new JComboBox(PlayerColor.values());
            comboBox.setEnabled(i < initialNumber);
            comboBoxes.add(comboBox);
            add(comboBox, c);
        }
        radioButtons.get(initialNumber - 1).setSelected(true);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        try
        {
            int index = Integer.parseInt(e.getActionCommand());
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
            
        }
    }
}
