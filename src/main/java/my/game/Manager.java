package my.game;

import java.util.List;
import java.util.Stack;
import my.command.ManagerCommand;
import my.player.Player;
import my.player.PlayerConfiguration;
import my.player.PlayersQueue;
import my.entity.EntityType;
import my.field.Field;
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
        players = new PlayersQueue(playerConfigurations, world.createAccessor());

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
            world.mark(players.current(), building);
        }
    }

    public void beginHiring()
    {

    }

    public void pursueHiring(EntityType entity)
    {

    }

    public void handleField(Field field)
    {
        switch (state)
        {
            case BUILDING_IN_PROGRESS ->
            {
                if (world.isMarked(field))
                {
                    Player player = players.current();
                    int cost = player.getPriceFor(selectedBuilding);
                    player.takeMoney(cost);
                    master.setMoney(player.getMoney());

                    world.substitute(field, selectedBuilding);
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

    public void passCommand(ManagerCommand command)
    {
        command.execute(this);
    }
}
