package my.main;

import java.awt.event.WindowEvent;
import java.util.List;
import javax.swing.JOptionPane;
//import java.util.Stack;
import my.command.Invoker;
import my.entity.AbstractEntity;
import my.entity.EntityInfoDialog;
import my.player.Player;
import my.player.PlayerConfiguration;
import my.player.PlayersQueue;
import my.field.AbstractField;
import my.field.BuildingField;
import my.field.BuildingInfoDialog;
import my.field.BuildingPurchaseDialog;
import my.utils.Hex;
import my.world.World;
import my.world.WorldConfiguration;

/**
 *
 * @author Kay Jay O'Nail
 */
public class Manager implements DefaultWindowListener
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
        players = new PlayersQueue(playerConfigurations, world.createAccessor(), world.createMarker());

        Hex[] capitals = world.locateCapitals(playerConfigurations.size());
        players.initCountries(capitals, world);

//        executedCommands = new Stack();
//        undoneCommands = new Stack();
    }

    private final Master master;
    private final World world;
    private final PlayersQueue players;
//    private Stack<ReversibleCommand> executedCommands;
//    private Stack<ReversibleCommand> undoneCommands;

    public World getWorld()
    {
        return world;
    }

    public Player getFirstPlayer()
    {
        return players.first();
    }
    
    /* -------------------- BuildingManager? -------------------- */

    private BuildingPurchaseDialog buildingDialog;

    public void showBuildingInfo(BuildingField building)
    {
        var dialog = new BuildingInfoDialog(master, building);
        dialog.addWindowListener(this);
        dialog.setVisible(true);
    }

    public void beginBuilding(BuildingField building)
    {
        Player player = players.current();
        if (!player.canBuild(building))
        {
            JOptionPane.showMessageDialog(master, """
                    Unfortunately, you cannot buy this building.

                    You do not have a place for this building.
                    Shift-click the building's button for details.
                    """);
        }
        else if (!player.canAfford(building))
        {
            JOptionPane.showMessageDialog(master, """
                    Unfortunately, you cannot buy this building.

                    You do not have enough money for this building.
                    Shift-click the building's button for details.
                    """);
        }
        else
        {
            state = State.BUILDING_BEGUN;
            
            var builder = new BuildingPurchaseDialog.Builder();
            builder.setFrame(master);
            builder.setInvoker(new Invoker<>(this));
            builder.setBuilding(building);
            builder.setPrice(player.computePriceFor(building));
            buildingDialog = builder.get();
            buildingDialog.addWindowListener(this);
            buildingDialog.setVisible(true);
        }
    }

    private BuildingField selectedBuilding;

    public void pursueBuilding(BuildingField building)
    {
        if (state == State.BUILDING_BEGUN)
        {
            state = State.BUILDING_IN_PROGRESS;

            buildingDialog.setVisible(false);
            buildingDialog.dispose();
            buildingDialog = null;
            selectedBuilding = building;
            players.current().markFor(building);
        }
    }
    
    /* -------------------- EntityManager? -------------------- */

    public void showEntityInfo(AbstractEntity entity)
    {
        var dialog = new EntityInfoDialog(master, entity);
        dialog.addWindowListener(this);
        dialog.setVisible(true);
    }

    public void beginHiring(AbstractEntity entity)
    {
        state = State.HIRING_BEGUN;

//        Player player = players.current();
//        entityDialog = new EntitySelectionDialog(master);
//        entityDialog.setManager(this);
//        entityDialog.setPlayerMoney(player.getMoney());
//        entityDialog.reassignValues();
//        entityDialog.setLocationRelativeTo(master);
//        entityDialog.setVisible(true);
    }

    public void pursueHiring(AbstractEntity entity)
    {
        state = State.HIRING_IN_PROGRESS;
    }
    
    /* -------------------- Manager -> ... -------------------- */

    public void handleField(AbstractField field)
    {
        switch (state)
        {
            case BUILDING_IN_PROGRESS ->
            {
                if (world.isMarked(field.getHex()))
                {
                    master.setMoney(players.current().buy(selectedBuilding));

                    world.substitute(field, selectedBuilding);
                }
                world.unmarkAll();
                selectedBuilding = null;
            }
            case HIRING_IN_PROGRESS ->
            {
                
            }
            case IDLE ->
            {
                
            }
        }
    }

    public void undo()
    {

    }

    public void redo()
    {

    }

    public void nextPlayer()
    {
        Player current = players.current();
        current.endRound();

        Player next = players.next();
        master.setUser(next);

        Hex hex = next.getCapitalHex();
        master.setCenter(hex);
    }

    public Invoker<Manager> createInvoker()
    {
        return new Invoker(this);
    }
    
    @Override
    public void windowClosing(WindowEvent e)
    {
        System.out.println("A dialog window is being closed...");
        state = State.IDLE;
        master.requestFocus();
    }
}
