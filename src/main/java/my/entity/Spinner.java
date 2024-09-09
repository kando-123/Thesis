package my.entity;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;
import javax.swing.BoxLayout;
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
            this.value = value;
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
            else // newValue == value
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

    public Spinner(Model model, Dimension size)
    {
        this.model = model;
        
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setPreferredSize(size);

        int buttonWidth = size.width / 7;
        int labelWidth = size.width - 6 * buttonWidth;
        Dimension buttonSize = new Dimension(buttonWidth, size.height);
        Dimension labelSize = new Dimension(labelWidth, size.height);
        
        minus10Button = new JButton("-10");
        minus10Button.setPreferredSize(buttonSize);
        minus10Button.addActionListener(this);
        add(minus10Button);

        minus5Button = new JButton("-5");
        minus5Button.setPreferredSize(buttonSize);
        minus5Button.addActionListener(this);
        add(minus5Button);

        minus1Button = new JButton("-1");
        minus1Button.setPreferredSize(buttonSize);
        minus1Button.addActionListener(this);
        add(minus1Button);

        valueLabel = new JLabel(String.valueOf(model.getValue()));
        valueLabel.setPreferredSize(labelSize);
        add(valueLabel);

        plus1Button = new JButton("+1");
        plus1Button.setPreferredSize(buttonSize);
        plus1Button.addActionListener(this);
        add(plus1Button);

        plus5Button = new JButton("+5");
        plus5Button.setPreferredSize(buttonSize);
        plus5Button.addActionListener(this);
        add(plus5Button);

        plus10Button = new JButton("+10");
        plus10Button.setPreferredSize(buttonSize);
        plus10Button.addActionListener(this);
        add(plus10Button);

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
