package my.player.selection;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javax.swing.MutableComboBoxModel;
import javax.swing.event.ListDataListener;

/**
 *
 * @author Kay Jay O'Nail
 */
public class ColorModel implements MutableComboBoxModel<PlayerColor>
{
    private final LinkedList<PlayerColor> items;
    private PlayerColor selectedItem;
    
    //private final List<ActionListener> listeners;
    
    public ColorModel()
    {
        items = new LinkedList<>();
        for (var color : PlayerColor.values())
        {
            items.add(color);
        }
        selectedItem = items.getFirst();
        
        //listeners = new ArrayList<>();
    }

    @Override
    public void addElement(PlayerColor item)
    {
        items.add(item); // to be sorted
    }

    @Override
    public void removeElement(Object obj)
    {
        assert (obj.getClass() == PlayerColor.class);
        items.remove((PlayerColor) obj);
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
    public void setSelectedItem(Object anItem)
    {
        assert (anItem.getClass() == PlayerColor.class);
        selectedItem = (PlayerColor) anItem;
        // Inform the listeners that a color is being released, and another is being taken.
    }

    @Override
    public Object getSelectedItem()
    {
        return selectedItem;
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
    public void addListDataListener(ListDataListener l)
    {
        
    }

    @Override
    public void removeListDataListener(ListDataListener l)
    {
        
    }
}
