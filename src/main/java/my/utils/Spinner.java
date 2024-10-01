package my.utils;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Kay Jay O'Nail
 */
public class Spinner extends JPanel implements ActionListener
{    
    public static class Model
    {
        private int value;
        private final int minimum;
        private final int maximum;

        public Model(int value, int minimum, int maximum)
        {
            this.minimum = minimum;
            this.maximum = maximum;

            if (value < minimum)
            {
                value = minimum;
            }
            else if (value > maximum)
            {
                value = maximum;
            }
            this.value = value;
        }

        public int getValue()
        {
            return value;
        }

        public Integer setValue(int newValue)
        {
            if (newValue < minimum)
            {
                return value = minimum;
            }
            else if (newValue > maximum)
            {
                return value = maximum;
            }
            else if (newValue != value)
            {
                return value = newValue;
            }
            else // newValue == value, no change
            {
                return null;
            }
        }

        public Integer changeValue(int increment)
        {
            int newValue = value + increment;
            if (newValue > maximum)
            {
                newValue = maximum;
            }
            else if (newValue < minimum)
            {
                newValue = minimum;
            }
            return (newValue != value) ? value = newValue : null;
        }
    }

    private final JButton minus10Button;
    private final JButton minus5Button;
    private final JButton minus1Button;
    private final JButton plus1Button;
    private final JButton plus5Button;
    private final JButton plus10Button;
    private final JLabel valueLabel;

    private final Model model;
    
    private static final Insets noMargin = new Insets(0, 0, 0, 0);

    public Spinner(Model model, Dimension size)
    {
        super(new GridBagLayout());
        this.model = model;
        
        setPreferredSize(size);

        int buttonWidth = size.width / 7;
        int labelWidth = size.width - 6 * buttonWidth;
        Dimension buttonSize = new Dimension(buttonWidth, size.height);
        Dimension labelSize = new Dimension(labelWidth, size.height);
        
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1;
        c.weighty = 1;
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.CENTER;
        minus10Button = new JButton("-10");
        minus10Button.setPreferredSize(buttonSize);
        minus10Button.addActionListener(this);
        minus10Button.setMargin(noMargin);
        add(minus10Button, c);

        ++c.gridx;
        minus5Button = new JButton("-5");
        minus5Button.setPreferredSize(buttonSize);
        minus5Button.addActionListener(this);
        minus5Button.setMargin(noMargin);
        add(minus5Button, c);

        ++c.gridx;
        minus1Button = new JButton("-1");
        minus1Button.setPreferredSize(buttonSize);
        minus1Button.addActionListener(this);
        minus1Button.setMargin(noMargin);
        add(minus1Button, c);

        ++c.gridx;
        valueLabel = new JLabel(String.valueOf(model.getValue()));
        valueLabel.setPreferredSize(labelSize);
        valueLabel.setHorizontalAlignment(JLabel.CENTER);
        valueLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        add(valueLabel, c);

        ++c.gridx;
        plus1Button = new JButton("+1");
        plus1Button.setPreferredSize(buttonSize);
        plus1Button.addActionListener(this);
        plus1Button.setMargin(noMargin);
        add(plus1Button, c);

        ++c.gridx;
        plus5Button = new JButton("+5");
        plus5Button.setPreferredSize(buttonSize);
        plus5Button.addActionListener(this);
        plus5Button.setMargin(noMargin);
        add(plus5Button, c);

        ++c.gridx;
        plus10Button = new JButton("+10");
        plus10Button.setPreferredSize(buttonSize);
        plus10Button.addActionListener(this);
        plus10Button.setMargin(noMargin);
        add(plus10Button, c);

        listeners = new LinkedList<>();
    }

    private void informListeners(int newValue)
    {
        ValueChangeEvent event = new ValueChangeEvent(newValue);
        listeners.forEach((listener) -> listener.valueChanged(event));
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        int increment = Integer.parseInt(e.getActionCommand());
        Integer newValue = model.changeValue(increment);
        if (newValue != null)
        {
            valueLabel.setText(newValue.toString());
            informListeners(newValue);
        }
    }

    public void setValue(int newValue)
    {
        Integer value = model.setValue(newValue);
        if (value != null)
        {
            value = newValue;
            valueLabel.setText(String.valueOf(value));
            informListeners(newValue);
        }
    }

    public int getValue()
    {
        return model.getValue();
    }

    public static class ValueChangeEvent
    {
        public final int newValue;

        public ValueChangeEvent(int newValue)
        {
            this.newValue = newValue;
        }
    }

    public static interface ValueChangeListener
    {
        public void valueChanged(ValueChangeEvent e);
    }

    private final List<ValueChangeListener> listeners;

    public void addValueChangeListener(ValueChangeListener listener)
    {
        listeners.add(listener);
    }

    public void removeValueChangeListener(ValueChangeListener listener)
    {
        listeners.remove(listener);
    }
}
