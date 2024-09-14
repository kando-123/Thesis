package my.main;

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

    private BuildingPurchaseDialog buildingDialog;

    public void showBuildingInfo(BuildingField building)
    {
        var dialog = new BuildingInfoDialog(master, building);
        dialog.setVisible(true);
    }

    public void beginBuilding(BuildingField building)
    {
        System.out.println("Begin building for: %s".formatted(building.getName()));

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

    public void showEntityInfo(AbstractEntity entity)
    {
        var dialog = new EntityInfoDialog(master, entity);
        dialog.setVisible(true);
    }

    public void beginHiring(AbstractEntity entity)
    {
        System.out.println("Begin hiring for: %s".formatted(entity.getName()));

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

    public void handleField(AbstractField field)
    {
        switch (state)
        {
            case BUILDING_IN_PROGRESS ->
            {
                if (world.isMarked(field.getHex()))
                {
//                    Player player = players.current();
//                    int count = player.getCount(selectedBuilding.getType());
//                    int cost = selectedBuilding.computePrice(count);
//                    player.spendMoney(cost);
//                    master.setMoney(player.getMoney());
//
//                    world.substitute(field, selectedBuilding);
                }
                world.unmarkAll();
                selectedBuilding = null;
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
}
