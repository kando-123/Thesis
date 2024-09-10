package my.game;

import my.field.BuildingSelectionDialog;
import java.util.List;
//import java.util.Stack;
import my.command.ManagerCommand;
import my.entity.EntitySelectionDialog;
import my.player.Player;
import my.player.PlayerConfiguration;
import my.player.PlayersQueue;
import my.entity.EntityType;
import my.field.AbstractField;
import my.field.FieldType;
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
        marker = world.createMarker();
        players = new PlayersQueue(playerConfigurations, world.createAccessor(), world.createMarker());

        Hex[] capitals = world.locateCapitals(playerConfigurations.size());
        players.initCountries(capitals, world);

//        executedCommands = new Stack();
//        undoneCommands = new Stack();
    }

    private final Master master;
    private final World world;
    private final World.Marker marker;
    private final PlayersQueue players;
//    private Stack<ManagerCommand> executedCommands;
//    private Stack<ManagerCommand> undoneCommands;

    private BuildingSelectionDialog buildingDialog;
    private EntitySelectionDialog entityDialog;

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
        builder.setErectableBuildings(player.getErectableBuildings());
        buildingDialog = builder.get();
        buildingDialog.setLocationRelativeTo(master);
        buildingDialog.setVisible(true);
    }

    private FieldType selectedBuilding;

    public void pursueBuilding(FieldType building)
    {
        if (state == State.BUILDING_BEGUN)
        {
            state = State.BUILDING_IN_PROGRESS;

            buildingDialog.dispose();
            selectedBuilding = building;
            players.current().markFor(building);
        }
    }

    public void beginHiring()
    {
        state = State.HIRING_BEGUN;

        Player player = players.current();
        entityDialog = new EntitySelectionDialog(master);
        entityDialog.setManager(this);
        entityDialog.setPlayerMoney(player.getMoney());
        entityDialog.reassignValues();
        entityDialog.setLocationRelativeTo(master);
        entityDialog.setVisible(true);
    }

    public void pursueHiring(EntityType entity)
    {
        state = State.HIRING_IN_PROGRESS;
    }

    public void handleField(AbstractField field)
    {
        switch (state)
        {
            case BUILDING_IN_PROGRESS ->
            {
                if (marker.isMarked(field.getHex()))
                {
                    Player player = players.current();
                    int cost = player.getPriceFor(selectedBuilding);
                    player.takeMoney(cost);
                    master.setMoney(player.getMoney());

                    world.substitute(field, selectedBuilding);
                }
                marker.unmarkAll();
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

    public void passCommand(ManagerCommand command)
    {
        command.execute(this);
    }
}
