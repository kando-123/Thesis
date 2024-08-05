package my.game;

import java.util.Set;
import javax.swing.JDialog;
import my.player.Player;
import my.units.Field;
import my.units.FieldType;
import my.world.World;

/**
 *
 * @author Kay Jay O'Nail
 */
public class Manager
{
    public static enum Event
    {
        TO_BUILD,
        TO_HIRE,
        DO_BUILD,
        DO_HIRE;
    }
    
    public Manager()
    {
        
    }
    
    public void setMaster(Master master)
    {
        this.master = master;
    }
    
    public void setWorld(World world)
    {
        this.world = world;
    }
    
    public void setWorldPanel(WorldPanel worldPanel)
    {
        this.worldPanel = worldPanel;
    }
    
    private Master master;
    private World world;
    private WorldPanel worldPanel;
    
    private JDialog purchaseDialog;
    private Set<Field> markedFields;
    
    public void manageEvent(Event event, Object arg)
    {
        switch (event)
        {
            case TO_BUILD ->
            {
                Player current = master.getCurrentPlayer();
                var set = world.getBuildableProperties(current);
                if (purchaseDialog == null)
                {
                    purchaseDialog = new PropertiesDialog(master, set);
                    purchaseDialog.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
                }
                purchaseDialog.setLocationRelativeTo(master);
                purchaseDialog.setVisible(true);
                worldPanel.beginAwaitingClick();
                
                master.requestFocus();
            }
            case DO_BUILD ->
            {
                purchaseDialog.setVisible(false);
                markedFields = world.markForPurchase(master.getCurrentPlayer(), (FieldType) arg);
                
                master.requestFocus();
            }
            case TO_HIRE ->
            {
                
            }
            case DO_HIRE ->
            {
                
            }
        }
    }
    
}
