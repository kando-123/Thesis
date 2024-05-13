package my.player.selection;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

public class SelectionPanel extends JPanel implements ActionListener
{
    private final int minimum;
    private final int maximum;
    private int limit;
    private int selected;
    
    private List<JRadioButton> radioButtons;
    private List<JTextField> textFields;
    private List<JComboBox> comboBoxes;
    private List<ColorModel> colorModels;
    
    protected ActionListener parent;
    
    public SelectionPanel(int min, int max, int lim, int sel)
    {
        super(new GridBagLayout());
        assert (0 <= min && min <= sel && sel <= lim && lim <= max);
        
        minimum = min;
        maximum = max;
        limit = lim;
        selected = sel;
        
        radioButtons = new ArrayList<>(maximum - minimum + 1);
        textFields = new ArrayList<>(maximum - minimum + 1);
        comboBoxes = new ArrayList<>(maximum - minimum + 1);
        colorModels = new ArrayList<>(maximum - minimum + 1);
        
        GridBagConstraints c;
        ButtonGroup group = new ButtonGroup(); 
        for (int i = minimum; i <= maximum; ++i)
        {
            c = new GridBagConstraints();
            c.gridx = 0;
            c.gridy = i - minimum;
            JRadioButton button = new JRadioButton(String.valueOf(i));
            button.setSelected(i == selected);
            button.setEnabled(i <= limit);
            group.add(button);
            radioButtons.add(button);
            add(button, c);
            
            if (i > 0)
            {
                c.gridx = 1;
                JTextField field = new JTextField(12);
                field.setEnabled(i <= selected);
                textFields.add(field);
                add(field, c);
                
                c.gridx = 2;
                ColorModel model = new ColorModel();
                colorModels.add(model);
                JComboBox combo = new JComboBox(model);
                comboBoxes.add(combo);
                add(combo, c);
            }
        }
    }
    
    public void setDefaultNames(String name)
    {
        int i = 0;
        for (var field : textFields)
        {
            field.setText(String.format("%s%d", name, ++i));
        }
    }
    
    public void addActionListener(ActionListener parent)
    {
        
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}