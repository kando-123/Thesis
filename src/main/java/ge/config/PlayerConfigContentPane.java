package ge.config;

import ge.player.*;
import ge.utilities.*;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import javax.swing.*;

/**
 *
 * @author Kay Jay O'Nail
 */
public class PlayerConfigContentPane extends JPanel implements ActionListener
{
    private final Invoker<ConfigManager> invoker;

    private final UserPanel userPanel;
    private final BotPanel botPanel;

    public PlayerConfigContentPane(Invoker<ConfigManager> invoker)
    {
        super(new GridBagLayout());

        this.invoker = invoker;

        var c = new GridBagConstraints();
        c.weightx = 1;
        c.weighty = 1;

        c.gridx = 0;
        c.gridy = 0;
        userPanel = new UserPanel();
        userPanel.setLimit(Player.MAX_NUMBER - BotPanel.DEFAULT_SELECTION);
        add(userPanel, c);

        c.gridx = 1;
        c.gridy = 0;
        botPanel = new BotPanel();
        botPanel.setLimit(Player.MAX_NUMBER - UserPanel.DEFAULT_SELECTION);
        add(botPanel, c);

        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 2;
        var button = new JButton("Next ↪");
        button.setActionCommand("->world");
        button.addActionListener(this);
        add(button, c);
    }

    public int getSelected()
    {
        return userPanel.getSelected() + botPanel.getSelected();
    }

    public PlayerConfig[] getConfigs()
    {
        UserConfig.Builder[] userBuilders = userPanel.getConfigs();
        BotConfig.Builder[] botBuilders = botPanel.getConfigs();
        
        // "Colorful" builders first.
        Arrays.sort(userBuilders, (o1, o2) -> o1.hasColor()
                ? (o2.hasColor() ? 0 : +1)
                : (o2.hasColor() ? -1 : 0));
        Arrays.sort(botBuilders, (o1, o2) -> o1.hasColor()
                ? (o2.hasColor() ? 0 : +1)
                : (o2.hasColor() ? -1 : 0));
        
        var list = new LinkedList<>(Arrays.asList(Player.ContourColor.values()));
        
        int firstColorlessUser = 0;
        for (int i = 0; i < userBuilders.length; ++i)
        {
            if (userBuilders[i].hasColor())
            {
                list.remove(userBuilders[i].getColor());
            }
            else
            {
                firstColorlessUser = i;
                break;
            }
        }
        
        int firstColorlessBot = 0;
        for (int i = 0; i < botBuilders.length; ++i)
        {
            if (botBuilders[i].hasColor())
            {
                list.remove(botBuilders[i].getColor());
            }
            else
            {
                firstColorlessBot = i;
            }
        }
        
        var random = new Random();
        for (int i = firstColorlessUser; i < userBuilders.length; ++i)
        {
            userBuilders[i].setColor(list.remove(random.nextInt(list.size())));
        }
        for (int i = firstColorlessBot; i < botBuilders.length; ++i)
        {
            botBuilders[i].setColor(list.remove(random.nextInt(list.size())));
        }

        PlayerConfig[] configs = new PlayerConfig[getSelected()];
        
        int index = 0;
        for (var builder : userBuilders)
        {
            configs[index++] = builder.get();
        }
        for (var builder : botBuilders)
        {
            configs[index++] = builder.get();
        }
        
//        //Shuffle, because why not to?
//        for (int i = 0; i < configs.length; ++i)
//        {
//            int j = random.nextInt(configs.length);
//            var config = configs[j];
//            configs[j] = configs[i];
//            configs[i] = config;
//        }
        
        return configs;
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (e.getActionCommand().equals("->world"))
        {
            if (getSelected() > 1)
            {
                invoker.invoke(new BeginWorldConfigCommand());
            }
            else
            {
                JOptionPane.showMessageDialog(this,
                        "Choose at least two players.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    @Override
    public void setPreferredSize(Dimension preferredSize)
    {
        super.setPreferredSize(preferredSize);

        var panelSize = new Dimension(preferredSize);
        panelSize.width *= 0.45;
        panelSize.height *= 0.85;
        userPanel.setPreferredSize(panelSize);
        botPanel.setPreferredSize(panelSize);
    }

    private class UserPanel extends JPanel implements ActionListener
    {
        private final int minimum;
        private final int maximum;

        private final HashMap<Integer, JRadioButton> radios;
        private final HashMap<Integer, JTextField> names;
        private final HashMap<Integer, JComboBox> combos;

        private int selected;
        private int limit;

        static final int DEFAULT_SELECTION = 1;

        UserPanel()
        {
            super(new GridBagLayout());

            minimum = 1;
            maximum = limit = Player.MAX_NUMBER;
            selected = DEFAULT_SELECTION;

            radios = new HashMap<>(maximum - minimum + 1);
            names = new HashMap<>(maximum - minimum + 1);
            combos = new HashMap<>(maximum - minimum + 1);

            var c = new GridBagConstraints();
            c.weightx = 1;
            c.weighty = 0;
            c.gridwidth = 3;
            add(new JLabel("The Human Players"), c);

            c.gridwidth = 1;

            var group = new ButtonGroup();
            for (int i = minimum; i <= maximum; ++i)
            {
                c.gridx = 0;
                c.gridy = i - minimum + 1;

                var button = new JRadioButton(String.valueOf(i));
                var command = String.format("NUM=%d", i);
                button.setActionCommand(command);
                button.addActionListener(this);
                button.setEnabled(i <= limit);
                group.add(button);
                radios.put(i, button);
                add(button, c);

                c.gridx = 1;
                var name = String.format("Player #%d", i);
                var field = new JTextField(name, 12);
                field.setMinimumSize(new Dimension(60, 20));
                field.setEnabled(i <= selected);
                names.put(i, field);
                add(field, c);

                c.gridx = 2;
                var model = new ColorModel();
                model.addActionListener(this);
                var combo = new JComboBox(model);
                combo.setEnabled(i <= selected);
                combos.put(i, combo);
                add(combo, c);
            }

            radios.get(DEFAULT_SELECTION).setSelected(true);
        }

        void setLimit(int newLimit)
        {
            assert (newLimit >= minimum && newLimit <= maximum);

            limit = newLimit;

            for (int i = minimum; i <= maximum; ++i)
            {
                radios.get(i).setEnabled(i <= limit);
            }
        }

        int getSelected()
        {
            return selected;
        }

        UserConfig.Builder[] getConfigs()
        {
            UserConfig.Builder[] builders = new UserConfig.Builder[selected];
            for (int i = minimum; i <= selected; ++i)
            {
                var builder = new UserConfig.Builder();

                var name = names.get(i).getText();
                builder.setName(!name.isBlank() ? name : "Anonymous the Conqueror");

                var selection = (SelectableColor) combos.get(i).getModel().getSelectedItem();
                builder.setColor(selection.color);

                builders[i - minimum] = builder;
            }
            return builders;
        }

        void addColor(SelectableColor color, Object exception)
        {
            for (var combo : combos.values())
            {
                var model = (ColorModel) combo.getModel();
                if (model != exception)
                {
                    model.addElement(color);
                }
            }
        }

        void addColor(SelectableColor color)
        {
            addColor(color, null);
        }

        void removeColor(SelectableColor color, Object exception)
        {
            for (var combo : combos.values())
            {
                var model = (ColorModel) combo.getModel();
                if (model != exception)
                {
                    model.removeElement(color);
                }
            }
        }

        void removeColor(SelectableColor color)
        {
            removeColor(color, null);
        }

        void disableUnused()
        {
            for (int i = minimum; i <= limit; ++i)
            {
                names.get(i).setEnabled(i <= selected);
                combos.get(i).setEnabled(i <= selected);
            }
        }

        @Override
        public void actionPerformed(ActionEvent e)
        {
            var stringlets = e.getActionCommand().split("=");
            switch (stringlets[0])
            {
                case "NUM" ->
                {
                    selected = Integer.parseInt(stringlets[1]);
                    botPanel.setLimit(Player.MAX_NUMBER - selected);

                    disableUnused();
                }
                case "DES" ->
                {
                    var color = SelectableColor.valueOf(stringlets[1]);
                    if (color != SelectableColor.RANDOM)
                    {
                        addColor(color, e.getSource());
                        botPanel.addColor(color);
                    }
                }
                case "SEL" ->
                {
                    var color = SelectableColor.valueOf(stringlets[1]);
                    if (color != SelectableColor.RANDOM)
                    {
                        removeColor(color, e.getSource());
                        botPanel.removeColor(color);
                    }
                }
            }
        }
    }

    private class BotPanel extends JPanel implements ActionListener
    {
        private final int minimum;
        private final int maximum;

        private final HashMap<Integer, JRadioButton> radios;
        private final HashMap<Integer, JLabel> names;
        private final HashMap<Integer, JComboBox> combos;

        private int selected;
        private int limit;

        static final int DEFAULT_SELECTION = 1;

        BotPanel()
        {
            super(new GridBagLayout());

            minimum = 1;
            maximum = limit = Player.MAX_NUMBER - 1;
            selected = DEFAULT_SELECTION;

            radios = new HashMap<>(maximum - minimum + 1);
            names = new HashMap<>(maximum - minimum + 1);
            combos = new HashMap<>(maximum - minimum + 1);

            var c = new GridBagConstraints();
            c.weightx = 1;
            c.weighty = 0;
            c.gridwidth = 3;
            add(new JLabel("The Bot Players"), c);

            var group = new ButtonGroup();

            c.gridwidth = 1;
            c.gridx = 0;
            c.gridy = 1;
            var noBotsButton = new JRadioButton("0");
            noBotsButton.setActionCommand("NUM=0");
            noBotsButton.addActionListener(this);
            group.add(noBotsButton);
            radios.put(0, noBotsButton);
            add(noBotsButton, c);

            c.gridx = 1;
            c.gridwidth = GridBagConstraints.REMAINDER;
            var noBotsLabel = new JLabel("(play without bots)");
            noBotsLabel.setEnabled(selected == 0);
            names.put(0, noBotsLabel);
            add(noBotsLabel, c);

            c.gridwidth = 1;

            for (int i = minimum; i <= maximum; ++i)
            {
                c.gridx = 0;
                c.gridy = i - minimum + 2;

                var button = new JRadioButton(String.valueOf(i));
                var command = String.format("NUM=%d", i);
                button.setActionCommand(command);
                button.addActionListener(this);
                button.setEnabled(i <= limit);
                group.add(button);
                radios.put(i, button);
                add(button, c);

                c.gridx = 1;
                var name = String.format("Bot #%d", i);
                var label = new JLabel(name);
                label.setMinimumSize(new Dimension(60, 20));
                label.setEnabled(i <= selected);
                names.put(i, label);
                add(label, c);

                c.gridx = 2;
                var model = new ColorModel();
                model.addActionListener(this);
                var combo = new JComboBox(model);
                combo.setEnabled(i <= selected);
                combos.put(i, combo);
                add(combo, c);
            }

            radios.get(DEFAULT_SELECTION).setSelected(true);
        }

        void setLimit(int newLimit)
        {
            assert (newLimit >= minimum && newLimit <= maximum);

            limit = newLimit;

            for (int i = minimum; i <= maximum; ++i)
            {
                radios.get(i).setEnabled(i <= limit);
            }
        }

        int getSelected()
        {
            return selected;
        }

        BotConfig.Builder[] getConfigs()
        {
            BotConfig.Builder[] builders = new BotConfig.Builder[selected];
            for (int i = minimum; i <= selected; ++i)
            {
                var builder = new BotConfig.Builder();

                var selection = (SelectableColor) combos.get(i).getModel().getSelectedItem();
                builder.setColor(selection.color);

                builders[i - minimum] = builder;
            }
            return builders;
        }

        void addColor(SelectableColor color, Object exception)
        {
            for (var combo : combos.values())
            {
                var model = (ColorModel) combo.getModel();
                if (model != exception)
                {
                    model.addElement(color);
                }
            }
        }

        void addColor(SelectableColor color)
        {
            addColor(color, null);
        }

        void removeColor(SelectableColor color, Object exception)
        {
            for (var combo : combos.values())
            {
                var model = (ColorModel) combo.getModel();
                if (model != exception)
                {
                    model.removeElement(color);
                }
            }
        }

        void removeColor(SelectableColor color)
        {
            removeColor(color, null);
        }

        void disableUnused()
        {
            for (int i = minimum; i <= limit; ++i)
            {
                names.get(i).setEnabled(i <= selected);
                combos.get(i).setEnabled(i <= selected);
            }
            names.get(0).setEnabled(selected == 0);
        }

        @Override
        public void actionPerformed(ActionEvent e)
        {
            var stringlets = e.getActionCommand().split("=");
            switch (stringlets[0])
            {
                case "NUM" ->
                {
                    selected = Integer.parseInt(stringlets[1]);
                    int userLimit = Player.MAX_NUMBER - selected;
                    userPanel.setLimit(userLimit);

                    disableUnused();
                }
                case "DES" ->
                {
                    var color = SelectableColor.valueOf(stringlets[1]);
                    if (color != SelectableColor.RANDOM)
                    {
                        addColor(color, e.getSource());
                        userPanel.addColor(color);
                    }
                }
                case "SEL" ->
                {
                    var color = SelectableColor.valueOf(stringlets[1]);
                    if (color != SelectableColor.RANDOM)
                    {
                        removeColor(color, e.getSource());
                        userPanel.removeColor(color);
                    }
                }
            }
        }
    }

    private enum SelectableColor
    {
        RANDOM(null),
        RED(Player.ContourColor.RED),
        ORANGE(Player.ContourColor.ORANGE),
        YELLOW(Player.ContourColor.YELLOW),
        GREEN(Player.ContourColor.GREEN),
        CYAN(Player.ContourColor.CYAN),
        BLUE(Player.ContourColor.BLUE),
        VIOLET(Player.ContourColor.VIOLET),
        MAGENTA(Player.ContourColor.MAGENTA);

        private final Player.ContourColor color;
        private final String string;

        private SelectableColor(Player.ContourColor color)
        {
            this.color = color;
            string = name().substring(0, 1).concat(name().substring(1).toLowerCase());
        }

        @Override
        public String toString()
        {
            return string;
        }
    }

    private static class ColorModel extends AbstractListModel<SelectableColor> implements MutableComboBoxModel<SelectableColor>
    {
        private final List<SelectableColor> items;
        private SelectableColor current;
        private ActionListener listener;

        public ColorModel()
        {
            items = new LinkedList<>(Arrays.asList(SelectableColor.values()));
            current = SelectableColor.RANDOM;
        }

        @Override
        public int getSize()
        {
            return items.size();
        }

        @Override
        public SelectableColor getElementAt(int index)
        {
            return items.get(index);
        }

        @Override
        public void addElement(SelectableColor item)
        {
            int index = 0;
            while (index < items.size())
            {
                if (item.compareTo(items.get(index)) < 0)
                {
                    break;
                }
                else
                {
                    ++index;
                }
            }
            items.add(index, item);
        }

        @Override
        public void removeElement(Object object)
        {
            assert (object.getClass() == SelectableColor.class);

            items.remove((SelectableColor) object);
        }

        @Override
        public void insertElementAt(SelectableColor item, int index)
        {
            items.add(index, item);
        }

        @Override
        public void removeElementAt(int index)
        {
            items.remove(index);
        }

        @Override
        public void setSelectedItem(Object newItem)
        {
            assert (newItem.getClass() == SelectableColor.class);

            var newColor = (SelectableColor) newItem;
            if (newColor != current)
            {
                if (listener != null)
                {
                    var deselection = String.format("DES=%s", current.name());
                    var deselected = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, deselection);
                    listener.actionPerformed(deselected);

                    var selection = String.format("SEL=%s", newColor.name());
                    var selected = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, selection);
                    listener.actionPerformed(selected);
                }

                current = newColor;
            }
        }

        @Override
        public Object getSelectedItem()
        {
            return current;
        }

        public void addActionListener(ActionListener listener)
        {
            this.listener = listener;
        }
    }
}
