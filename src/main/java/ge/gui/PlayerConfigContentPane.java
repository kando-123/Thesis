package ge.gui;

import ge.player.AbstractPlayer;
import ge.utilities.*;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import javax.swing.*;
import my.player.PlayerColor;

/**
 *
 * @author Kay Jay O'Nail
 */
public class PlayerConfigContentPane extends JPanel implements ActionListener
{
    private final Invoker<GUIManager> invoker;

    private UserSelectionPanel userSelection;
    private BotSelectionPanel botSelection;

    public PlayerConfigContentPane(Invoker<GUIManager> invoker)
    {
        super(new GridBagLayout());

        this.invoker = invoker;

        JTabbedPane tabbedPane = new JTabbedPane();
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.weighty = 1;
        add(tabbedPane, c);

        tabbedPane.add("Users", userSelection);
        tabbedPane.add("Bots", botSelection);

        JButton button = new JButton("Ready");
        button.setActionCommand("->world");
        button.addActionListener(this);
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 1;
        c.weighty = 1;
        add(button, c);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (e.getActionCommand().equals("->world"))
        {
            invoker.invoke(new BeginWorldConfigCommand());
        }
    }

    private class UserSelectionPanel extends JPanel
    {
        private final int minimum = 1;
        private final int maximum = AbstractPlayer.MAX_PLAYERS_COUNT;
        
        public UserSelectionPanel()
        {
            super(new GridBagLayout());
            
            var c = new GridBagConstraints();
            c.weightx = c.weighty = 1;
            
            
            for (int i = minimum; i <= maximum; ++i)
            {
                c.gridx = 0;
                c.gridy = i - minimum;
                
                
            }
        }
    }

    private class BotSelectionPanel extends JPanel
    {

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

            PlayerColor newColor = (PlayerColor) newItem;
            if (newColor != selectedColor)
            {
//                if (listener != null)
//                {
//                    String deselection = String.format("DES;%s;CONT", selectedColor.name());
//                    ActionEvent deselected = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, deselection);
//                    listener.actionPerformed(deselected);
//
//                    String selection = String.format("SEL;%s;CONT", newColor.name());
//                    ActionEvent selected = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, selection);
//                    listener.actionPerformed(selected);
//                }

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
