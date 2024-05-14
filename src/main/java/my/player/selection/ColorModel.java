package my.player.selection;

import my.player.PlayerColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import javax.swing.AbstractListModel;
import javax.swing.MutableComboBoxModel;

/**
 *
 * @author Kay Jay O'Nail
 */
public class ColorModel extends AbstractListModel<PlayerColor> implements MutableComboBoxModel<PlayerColor>
{
    private final List<PlayerColor> items;
    private PlayerColor selectedColor;
    private ActionListener listener;

    public ColorModel()
    {
        items = new LinkedList<>();
        items.addAll(Arrays.asList(PlayerColor.values()));
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
            if (listener != null)
            {
                String deselection = String.format("DES;%s;CONT", selectedColor.name());
                ActionEvent deselected = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, deselection);
                listener.actionPerformed(deselected);

                String selection = String.format("SEL;%s;CONT", newColor.name());
                ActionEvent selected = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, selection);
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
