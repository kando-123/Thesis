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
        return null;
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
        private final int minimum = 1;
        private final int maximum = AbstractPlayer.MAX_PLAYERS_COUNT;

        private final HashMap<Integer, JRadioButton> radioButtons;
        private final HashMap<Integer, JTextField> textFields;
        private final HashMap<Integer, JComboBox> comboBoxes;

        private int selected;

        public UserSelectionPanel()
        {
            super(new GridBagLayout());

            radioButtons = new HashMap<>(maximum - minimum + 1);
            textFields = new HashMap<>(maximum - minimum + 1);
            comboBoxes = new HashMap<>(maximum - minimum + 1);

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
                group.add(button);
                radioButtons.put(i, button);
                add(button, c);

                c.gridx = 1;
                var name = String.format("Player #%d", i);
                var field = new JTextField(name, 12);
                field.setMinimumSize(new Dimension(60, 20));
                textFields.put(i, field);
                add(field, c);

                c.gridx = 2;
                var model = new ColorModel();
                model.addActionListener(this);
                var combo = new JComboBox(model);
                comboBoxes.put(i, combo);
                add(combo, c);
            }
        }

        int getSelected()
        {
            return selected;
        }

        PlayerConfig[] getConfigs()
        {
            return null;
        }

        @Override
        public void actionPerformed(ActionEvent e)
        {
            System.out.println(e.getActionCommand());
        }
    }

    private class BotSelectionPanel extends JPanel implements ActionListener
    {
        private final int minimum = 1;
        private final int maximum = AbstractPlayer.MAX_PLAYERS_COUNT - 1;

        private final HashMap<Integer, JRadioButton> radioButtons;
        private final HashMap<Integer, JLabel> nameLabels;
        private final HashMap<Integer, JComboBox> comboBoxes;

        private int selected;

        public BotSelectionPanel()
        {
            super(new GridBagLayout());

            radioButtons = new HashMap<>(maximum - minimum + 1);
            nameLabels = new HashMap<>(maximum - minimum + 1);
            comboBoxes = new HashMap<>(maximum - minimum + 1);

            var c = new GridBagConstraints();
            c.weightx = c.weighty = 1;
            var group = new ButtonGroup();

            c.gridx = 0;
            c.gridy = 0;
            var noBotsButton = new JRadioButton("0");
            noBotsButton.setActionCommand("NUM=0");
            noBotsButton.addActionListener(this);
            group.add(noBotsButton);
            radioButtons.put(0, noBotsButton);
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
                group.add(button);
                radioButtons.put(i, button);
                add(button, c);

                c.gridx = 1;
                var name = String.format("Bot #%d", i);
                var label = new JLabel(name);
                label.setMinimumSize(new Dimension(60, 20));
                nameLabels.put(i, label);
                add(label, c);

                c.gridx = 2;
                var model = new ColorModel();
                model.addActionListener(this);
                var combo = new JComboBox(model);
                comboBoxes.put(i, combo);
                add(combo, c);
            }
        }

        int getSelected()
        {
            return selected;
        }

        PlayerConfig[] getConfigs()
        {
            return null;
        }

        @Override
        public void actionPerformed(ActionEvent e)
        {
            System.out.println(e.getActionCommand());
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
