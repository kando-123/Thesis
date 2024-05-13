package my.player.selection;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JTabbedPane;

/**
 *
 * @author Kay Jay O'Nail
 */
public class TabbedPane extends JTabbedPane implements ActionListener
{
    private List<SelectionPanel> selectionPanels;
    
    public TabbedPane()
    {
        selectionPanels = new ArrayList<>();
        
        
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        String[] commandlets = e.getActionCommand().split(";");
    }
}
