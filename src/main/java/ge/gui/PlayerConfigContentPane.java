package ge.gui;

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
import java.util.stream.Stream;
import javax.swing.*;

/**
 *
 * @author Kay Jay O'Nail
 */
public class PlayerConfigContentPane extends JPanel implements ActionListener
{
    private final Invoker<GUIManager> invoker;

    private final UserSelectionPanel userSelection;
    private final BotSelectionPanel botSelection;

    public PlayerConfigContentPane(Invoker<GUIManager> invoker)
    {
        super(new GridBagLayout());

        this.invoker = invoker;

        var c = new GridBagConstraints();

        var tabbedPane = new JTabbedPane();
        c.gridx = 0;
        c.gridy = 0;
        c.weighty = 1;
        add(tabbedPane, c);

        userSelection = new UserSelectionPanel();
        tabbedPane.add("Users", userSelection);

        botSelection = new BotSelectionPanel();
        tabbedPane.add("Bots", botSelection);

        userSelection.setLimit(AbstractPlayer.MAX_PLAYERS_COUNT - BotSelectionPanel.DEFAULT_SELECTION);
        botSelection.setLimit(AbstractPlayer.MAX_PLAYERS_COUNT - UserSelectionPanel.DEFAULT_SELECTION);
        
        var button = new JButton("Ready");
        button.setActionCommand("->world");
        button.addActionListener(this);

        c.gridx = 0;
        c.gridy = 1;
        c.weighty = 1;
        add(button, c);
    }

    public int getSelected()
    {
        return userSelection.getSelected() + botSelection.getSelected();
    }

    public PlayerConfig[] getConfigs()
    {
        var users = userSelection.getConfigs();
        var bots = botSelection.getConfigs();
        var players = new PlayerConfig[users.length + bots.length];
        System.arraycopy(users, 0, players, 0, users.length);
        System.arraycopy(bots, 0, players, users.length, bots.length);
        return players;
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (e.getActionCommand().equals("->world"))
        {
            invoker.invoke(new BeginWorldConfigCommand());
        }
    }

    private class UserSelectionPanel extends JPanel implements ActionListener
    {
        private final int minimum;
        private final int maximum;

        private final HashMap<Integer, JRadioButton> radios;
        private final HashMap<Integer, JTextField> names;
        private final HashMap<Integer, JComboBox> combos;

        private int selected;
        private int limit;

        static final int DEFAULT_SELECTION = 1;

        UserSelectionPanel()
        {
            super(new GridBagLayout());
            
            minimum = 1;
            maximum = limit = AbstractPlayer.MAX_PLAYERS_COUNT;
            selected = DEFAULT_SELECTION;

            radios = new HashMap<>(maximum - minimum + 1);
            names = new HashMap<>(maximum - minimum + 1);
            combos = new HashMap<>(maximum - minimum + 1);

            var c = new GridBagConstraints();
            c.weightx = c.weighty = 1;
            var group = new ButtonGroup();
            for (int i = minimum; i <= maximum; ++i)
            {
                c.gridx = 0;
                c.gridy = i - minimum;

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

        PlayerConfig[] getConfigs()
        {
            var configs = new PlayerConfig[selected];
            for (int i = minimum; i <= selected; ++i)
            {
                var color = (PlayerColor) combos.get(i).getModel().getSelectedItem();
                var name = names.get(i).getText();
                configs[i - minimum] = new PlayerConfig(PlayerType.USER, color, name);
            }
            return configs;
        }

        void addColor(PlayerColor color, Object exception)
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

        void addColor(PlayerColor color)
        {
            addColor(color, null);
        }

        void removeColor(PlayerColor color, Object exception)
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

        void removeColor(PlayerColor color)
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
                    int botLimit = AbstractPlayer.MAX_PLAYERS_COUNT - selected;
                    botSelection.setLimit(botLimit);

                    disableUnused();
                }
                case "DES" ->
                {
                    var color = PlayerColor.valueOf(stringlets[1]);
                    if (color != PlayerColor.RANDOM)
                    {
                        addColor(color, e.getSource());
                        botSelection.addColor(color);
                    }
                }
                case "SEL" ->
                {
                    var color = PlayerColor.valueOf(stringlets[1]);
                    if (color != PlayerColor.RANDOM)
                    {
                        removeColor(color, e.getSource());
                        botSelection.removeColor(color);
                    }
                }
            }
        }
    }

    private class BotSelectionPanel extends JPanel implements ActionListener
    {
        private final int minimum;
        private final int maximum;

        private final HashMap<Integer, JRadioButton> radios;
        private final HashMap<Integer, JLabel> names;
        private final HashMap<Integer, JComboBox> combos;

        private int selected;
        private int limit;

        static final int DEFAULT_SELECTION = 1;

        BotSelectionPanel()
        {
            super(new GridBagLayout());
            
            minimum = 1;
            maximum = limit = AbstractPlayer.MAX_PLAYERS_COUNT - 1;
            selected = DEFAULT_SELECTION;

            radios = new HashMap<>(maximum - minimum + 1);
            names = new HashMap<>(maximum - minimum + 1);
            combos = new HashMap<>(maximum - minimum + 1);

            var c = new GridBagConstraints();
            c.weightx = c.weighty = 1;
            var group = new ButtonGroup();

            c.gridx = 0;
            c.gridy = 0;
            var noBotsButton = new JRadioButton("0");
            noBotsButton.setActionCommand("NUM=0");
            noBotsButton.addActionListener(this);
            group.add(noBotsButton);
            radios.put(0, noBotsButton);
            add(noBotsButton, c);

            c.gridx = 1;
            c.gridwidth = GridBagConstraints.REMAINDER;
            add(new JLabel("play without bots"), c);
            c.gridwidth = 1;

            for (int i = minimum; i <= maximum; ++i)
            {
                c.gridx = 0;
                c.gridy = 1 + i - minimum;

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

        PlayerConfig[] getConfigs()
        {
            var configs = new PlayerConfig[selected];
            for (int i = minimum; i <= selected; ++i)
            {
                var color = (PlayerColor) combos.get(i).getModel().getSelectedItem();
                var name = names.get(i).getText();
                configs[i - minimum] = new PlayerConfig(PlayerType.USER, color, name);
            }
            return configs;
        }

        void addColor(PlayerColor color, Object exception)
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

        void addColor(PlayerColor color)
        {
            addColor(color, null);
        }

        void removeColor(PlayerColor color, Object exception)
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

        void removeColor(PlayerColor color)
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
                    int userLimit = AbstractPlayer.MAX_PLAYERS_COUNT - selected;
                    userSelection.setLimit(userLimit);

                    disableUnused();
                }
                case "DES" ->
                {
                    var color = PlayerColor.valueOf(stringlets[1]);
                    if (color != PlayerColor.RANDOM)
                    {
                        addColor(color, e.getSource());
                        userSelection.addColor(color);
                    }
                }
                case "SEL" ->
                {
                    var color = PlayerColor.valueOf(stringlets[1]);
                    if (color != PlayerColor.RANDOM)
                    {
                        removeColor(color, e.getSource());
                        userSelection.removeColor(color);
                    }
                }
            }
        }
    }

    private static class ColorModel extends AbstractListModel<PlayerColor> implements MutableComboBoxModel<PlayerColor>
    {
        private final List<PlayerColor> items;
        private PlayerColor selectedColor;
        private ActionListener listener;

        public ColorModel()
        {
            items = new LinkedList<>(Arrays.asList(PlayerColor.values()));
            selectedColor = PlayerColor.RANDOM;
        }

        @Override
        public int getSize()
        {
            return items.size();
        }

        @Override
        public PlayerColor getElementAt(int index)
        {
            return items.get(index);
        }

        @Override
        public void addElement(PlayerColor item)
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
            assert (object.getClass() == PlayerColor.class);

            items.remove((PlayerColor) object);
        }

        @Override
        public void insertElementAt(PlayerColor item, int index)
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
            assert (newItem.getClass() == PlayerColor.class);

            var newColor = (PlayerColor) newItem;
            if (newColor != selectedColor)
            {
                if (listener != null)
                {
                    var deselection = String.format("DES=%s", selectedColor.name());
                    var deselected = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, deselection);
                    listener.actionPerformed(deselected);

                    var selection = String.format("SEL=%s", newColor.name());
                    var selected = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, selection);
                    listener.actionPerformed(selected);
                }

                selectedColor = newColor;
            }
        }

        @Override
        public Object getSelectedItem()
        {
            return selectedColor;
        }

        public void addActionListener(ActionListener listener)
        {
            this.listener = listener;
        }
    }
}
