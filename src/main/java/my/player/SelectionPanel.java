package my.player;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
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

    private final HashMap<Integer, JRadioButton> radioButtons;
    private final HashMap<Integer, JTextField> textFields;
    private final HashMap<Integer, JComboBox> comboBoxes;
    private final HashMap<Integer, ColorModel> colorModels;

    private ActionListener parent;
    
    private PlayerType type;

    public SelectionPanel(int min, int max, int lim, int sel)
    {
        super(new GridBagLayout());
        
        assert (0 <= min && min <= sel && sel <= lim && lim <= max);

        minimum = min;
        maximum = max;
        limit = lim;
        selected = sel;

        radioButtons = new HashMap<>(maximum - minimum + 1);
        textFields = new HashMap<>(maximum - minimum + 1);
        comboBoxes = new HashMap<>(maximum - minimum + 1);
        colorModels = new HashMap<>(maximum - minimum + 1);

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
            button.setActionCommand("NUM;%d".formatted(i));
            button.addActionListener(this);
            group.add(button);
            radioButtons.put(i, button);
            add(button, c);

            if (i > 0)
            {
                c.gridx = 1;
                JTextField field = new JTextField(12);
                field.setMinimumSize(new Dimension(60, 20));
                field.setEnabled(i <= selected);
                textFields.put(i, field);
                add(field, c);

                c.gridx = 2;
                ColorModel model = new ColorModel();
                model.addActionListener(this);
                colorModels.put(i, model);
                JComboBox combo = new JComboBox(model);
                combo.setEnabled(i <= selected);
                comboBoxes.put(i, combo);
                add(combo, c);
            }
        }
    }
    
    public void setPlayerType(PlayerType type)
    {
        this.type = type;
    }

    public void setDefaultNames(String name)
    {
        for (int i = minimum; i <= maximum; ++i)
        {
            if (i > 0)
            {
                textFields.get(i).setText(String.format("%s%d", name, i));
            }
        }
    }

    public void addActionListener(ActionListener parent)
    {
        this.parent = parent;
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        
        String[] particles = e.getActionCommand().split(";");
        switch (particles[0])
        {
            case "NUM" ->
            {
                int index = Integer.parseInt(particles[1]);

                assert (minimum <= index && index <= maximum);

                if (index != selected)
                {
                    for (int i = minimum; i <= maximum; ++i)
                    {
                        boolean disable = (i > index);
                        if (i > 0)
                        {
                            JTextField field = textFields.get(i);
                            field.setEnabled(!disable);

                            JComboBox combo = comboBoxes.get(i);
                            combo.setEnabled(!disable);
                            
                            if (disable)
                            {
                                colorModels.get(i).setSelectedItem(PlayerColor.RANDOM);
                            }
                        }
                    }
                    
                    int difference = selected - index;
                    String command = "REL;%d".formatted(difference);
                    ActionEvent event = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, command);
                    parent.actionPerformed(event);

                    selected = index;
                }
            }
            case "REL" ->
            {
                int number = Integer.parseInt(particles[1]);
                limit += number;
                
                assert (minimum <= limit && limit <= maximum);
                
                for (int i = minimum; i <= maximum; ++i)
                {
                    boolean disable = (i > limit);
                    radioButtons.get(i).setEnabled(!disable);
                }
            }
            case "SEL" ->
            {
                PlayerColor color = PlayerColor.valueOf(particles[1]);
                Object source = e.getSource();
                if (color != PlayerColor.RANDOM && source != null)
                {
                    for (int i = minimum; i <= maximum; ++i)
                    {
                        ColorModel model = colorModels.get(i);
                        if (i > 0 && model != source)
                        {
                            model.removeElement(color);
                        }
                    }
                    if (!particles[2].equals("STOP"))
                    {
                        String command = e.getActionCommand();
                        ActionEvent event = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, command);
                        parent.actionPerformed(event);
                    }
                }
            }
            case "DES" ->
            {
                PlayerColor color = PlayerColor.valueOf(particles[1]);
                Object source = e.getSource();
                if (color != PlayerColor.RANDOM && source != null)
                {
                    for (int i = minimum; i <= maximum; ++i)
                    {
                        ColorModel model = colorModels.get(i);
                        if (i > 0 && model != source)
                        {
                            model.addElement(color);
                        }
                    }
                }
                if (!particles[2].equals("STOP"))
                {
                    String command = e.getActionCommand();
                    ActionEvent event = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, command);
                    parent.actionPerformed(event);
                }
            }
        }
    }
    
    public int getSelected()
    {
        return selected;
    }
    
    public List<PlayerConfiguration> getPlayerParameters()
    {
        List<PlayerConfiguration> list = new ArrayList<>();
        for (int i = Math.max(minimum, 1); i <= selected; ++i)
        {
            PlayerConfiguration configuration = new PlayerConfiguration();
            configuration.type = type;
            configuration.name = textFields.get(i).getText();
            configuration.color = (PlayerColor) colorModels.get(i).getSelectedItem();
            list.add(configuration);
        }
        return list;
    }
}
