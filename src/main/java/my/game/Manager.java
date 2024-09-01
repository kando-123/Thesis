package my.game;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import my.command.ManagerCommand;
import my.player.Player;
import my.player.PlayerConfiguration;
import my.player.PlayersQueue;
import my.units.EntityType;
import my.units.Field;
import my.units.FieldType;
import my.utils.Hex;
import my.world.World;
import my.world.WorldConfiguration;

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
        
        executedCommands = new Stack();
        undoneCommands = new Stack();
    }

    private final Master master;
    private final World world;
    private final PlayersQueue players;
    private Stack<ManagerCommand> executedCommands;
    private Stack<ManagerCommand> undoneCommands;
    
    private BuildingSelectionDialog buildingDialog;

    //private JDialog purchaseDialog;
    //private Set<Field> markedFields;
    
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
        state = State.BUILDING_BEGUN;
        
        Player player = players.current();
        BuildingSelectionDialog.Builder builder = new BuildingSelectionDialog.Builder();
        builder.setFrame(master);
        builder.setManager(this);
        builder.setPrices(player.getPrices());
        builder.setPlayerMoney(player.getMoney());
        builder.setErectableBuildings(world.getErectableBuildings(player));
        buildingDialog = builder.get();
        buildingDialog.setVisible(true);
    }
    
    public void beginHiring()
    {
        
    }
    
    private FieldType selectedBuilding;
    
    public void pursueBuilding(FieldType building)
    {
        state = State.BUILDING_IN_PROGRESS;
        
        buildingDialog.dispose();
        selectedBuilding = building;
        world.mark(players.current(), building);
    }
    
    public void pursueHiring(EntityType entity)
    {
        
    }
    
    public void handleField(Field field)
    {
        
        world.substitute(field, selectedBuilding);
        world.unmarkAll();
        selectedBuilding = null;
    }
    
    public void undo()
    {
        
    }
    
    public void redo()
    {
        
    }
    
    public void nextPlayer()
    {
        
    }

//    public void beginBuilding()
//    {
//        if (state == State.IDLE)
//        {
//            state = State.BUILDING_BEGUN;
//
//            Player current = players.current();
//            var set = world.getBuildableProperties(current);
//            if (purchaseDialog == null)
//            {
//                purchaseDialog = new PropertiesDialog(master, this, set);
//                purchaseDialog.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
//            }
//            else
//            {
//                // reset the dialog
//            }
//            purchaseDialog.setLocationRelativeTo(master);
//            purchaseDialog.setVisible(true);
//
//            master.requestFocus();
//        }
//    }
    
//    private FieldType selectedType = null;
    
//    public void buildingSelected(FieldType type)
//    {
//        if (state == State.BUILDING_BEGUN)
//        {
//            selectedType = type;
//            
//            purchaseDialog.setVisible(false);
//            markedFields = world.mark(players.current(), type);
//
//            master.requestFocus();
//            
//            state = State.BUILDING_IN_PROGRESS;
//        }
//    }
    
//    public void fieldSelected(Field field)
//    {
//        if (state == State.BUILDING_IN_PROGRESS)
//        {
//            if (markedFields != null && markedFields.contains(field))
//            {
//                world.substitute(field, selectedType);
//            }
//            markedFields = null;
//            
//            state = State.IDLE;
//        }
//        else if (state == State.HIRING_IN_PROGRESS)
//        {
//            
//            
//            state = State.IDLE;
//        }
//    }
    
    public void passCommand(ManagerCommand command)
    {
        System.out.println("Manager asserts receiving a command.");
        command.execute(this);
    }
}
