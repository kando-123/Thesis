package my.game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Set;
import javax.swing.JDialog;
import my.player.Player;
import my.player.PlayerConfiguration;
import my.player.PlayersQueue;
import my.units.Field;
import my.units.FieldType;
import my.utils.Hex;
import my.world.World;
import my.world.WorldConfiguration;

/**
 *
 * @author Kay Jay O'Nail
 */
public class Manager implements ActionListener
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

    public Manager(Master master,
                   WorldConfiguration worldConfiguration,
                   List<PlayerConfiguration> playerConfigurations)
    {
        this.master = master;
        
        state = State.IDLE;
        
        world = new World(worldConfiguration);
        players = new PlayersQueue(playerConfigurations);
        
        Hex[] capitals = world.locateCapitals(playerConfigurations.size());
        players.initCountries(capitals, world);
    }

    private final Master master;
    private final World world;
    private final PlayersQueue players;

    private JDialog purchaseDialog;
    private Set<Field> markedFields;
    
    public World getWorld()
    {
        return world;
    }
    
    public Player getFirstPlayer()
    {
        return players.first();
    }

    public void beginBuilding()
    {
        if (state == State.IDLE)
        {
            state = State.BUILDING_BEGUN;

            Player current = players.current();
            var set = world.getBuildableProperties(current);
            if (purchaseDialog == null)
            {
                purchaseDialog = new PropertiesDialog(master, this, set);
                purchaseDialog.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
            }
            else
            {
                // reset the dialog
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
            markedFields = world.markForPurchase(players.current(), type);

            master.requestFocus();
            
            state = State.BUILDING_IN_PROGRESS;
        }
    }
    
    public void fieldSelected(Field field)
    {
        if (state == State.BUILDING_IN_PROGRESS)
        {
            if (markedFields != null && markedFields.contains(field))
            {
                world.substitute(field, selectedType);
            }
            markedFields = null;
            
            state = State.IDLE;
        }
        else if (state == State.HIRING_IN_PROGRESS)
        {
            
            
            state = State.IDLE;
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        switch (e.getActionCommand())
        {
            case "to-build" ->
            {
                System.out.println("Building begins...");
            }
            case "to-hire" ->
            {
                System.out.println("Hiring begins...");
            }
            case "undo" ->
            {
                System.out.println("Undoing...");
            }
            case "redo" ->
            {
                System.out.println("Redoing...");
            }
            case "done" ->
            {
                System.out.println("Next player...");
            }
        }
    }
}
