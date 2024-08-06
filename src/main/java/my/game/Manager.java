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
    private static enum State
    {
        IDLE,
        
        BUILDING_BEGUN,
        BUILDING_IN_PROGRESS,
        
        HIRING_BEGUN,
        HIRING_IN_PROGRESS;
    }
    
    private State state;

    public Manager()
    {
        state = State.IDLE;
    }

    public void setMaster(Master master)
    {
        this.master = master;
    }

    public void setWorld(World world)
    {
        this.world = world;
    }

    private Master master;
    private World world;

    private JDialog purchaseDialog;
    private Set<Field> markedFields;

    public void beginBuilding()
    {
        if (state == State.IDLE)
        {
            state = State.BUILDING_BEGUN;

            Player current = master.getCurrentPlayer();
            var set = world.getBuildableProperties(current);
            if (purchaseDialog == null)
            {
                purchaseDialog = new PropertiesDialog(master, set);
                purchaseDialog.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
            }
            purchaseDialog.setLocationRelativeTo(master);
            purchaseDialog.setVisible(true);

            master.requestFocus();
        }
    }
    
    private FieldType selectedType = null;
    
    public void buildingSelected(FieldType type)
    {
        if (state == State.BUILDING_BEGUN)
        {
            selectedType = type;
            
            purchaseDialog.setVisible(false);
            markedFields = world.markForPurchase(master.getCurrentPlayer(), type);

            master.requestFocus();
            
            state = State.BUILDING_IN_PROGRESS;
        }
    }
    
    public void fieldSelected(Field field)
    {
        if (state == State.BUILDING_IN_PROGRESS)
        {
            if (markedFields.contains(field))
            {
                world.substitute(field, selectedType);
            }
            
            state = State.IDLE;
        }
        else if (state == State.HIRING_IN_PROGRESS)
        {
            
            
            state = State.IDLE;
        }
    }
}
