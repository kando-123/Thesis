package my.flow;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.JOptionPane;
//import java.util.Stack;
import my.command.Invoker;
import my.entity.AbstractEntity;
import my.entity.EntityType;
import my.gui.EntityInfoDialog;
import my.gui.EntityPurchaseDialog;
import my.player.Player;
import my.player.PlayerConfiguration;
import my.player.PlayersQueue;
import my.field.AbstractField;
import my.field.BuildingField;
import my.gui.BuildingInfoDialog;
import my.gui.BuildingPurchaseDialog;
import my.gui.Master;
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
        HIRING_IN_PROGRESS,
        MOVING_BEGUN
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
    public void showBuildingInfo(BuildingField building)
    {
        var dialog = new BuildingInfoDialog(master, building);
        dialog.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                master.requestFocus();
            }
        });
        dialog.setVisible(true);
    }

    private BuildingPurchaseDialog buildingDialog;

    public void beginBuilding(BuildingField building)
    {
        if (state == State.IDLE)
        {
            Player player = players.current();
            if (!player.canBuild(building))
            {
                JOptionPane.showMessageDialog(master, """
                        Unfortunately, you cannot buy this building.

                        You do not have a place for this building.
                        Shift-click the building's button for details.""");
                master.requestFocus();
            }
            else if (!player.canAfford(building))
            {
                JOptionPane.showMessageDialog(master, """
                        Unfortunately, you cannot buy this building.

                        You do not have enough money for this building.
                        Shift-click the building's button for details.""");
                master.requestFocus();
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
                buildingDialog.addWindowListener(new WindowAdapter()
                {
                    @Override
                    public void windowClosing(WindowEvent e)
                    {
                        state = State.IDLE;
                        master.requestFocus();
                    }
                });
                buildingDialog.setVisible(true);
            }
        }
    }

    private BuildingField buildingBeingPurchased;

    public void pursueBuilding(BuildingField building)
    {
        if (state == State.BUILDING_BEGUN)
        {
            state = State.BUILDING_IN_PROGRESS;

            buildingDialog.setVisible(false);
            buildingDialog.dispose();
            buildingDialog = null;
            buildingBeingPurchased = building;
            players.current().markFor(buildingBeingPurchased);

            master.requestFocus();
        }
    }

    /* -------------------- EntityManager? -------------------- */
    public void showEntityInfo(AbstractEntity entity)
    {
        var dialog = new EntityInfoDialog(master, entity);
        dialog.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                master.requestFocus();
            }
        });
        dialog.setVisible(true);
    }

    private EntityPurchaseDialog entityDialog;

    public void beginHiring(AbstractEntity entity)
    {
        if (state == State.IDLE)
        {
            Player player = players.current();
            if (!player.canHire(entity))
            {
                JOptionPane.showMessageDialog(master, """
                        Unfortunately, you cannot buy this entity.

                        You do not have a place for this entity.
                        Shift-click the entity's button for details.""");
                master.requestFocus();
            }
            else
            {
                state = State.HIRING_BEGUN;

                var builder = new EntityPurchaseDialog.Builder();
                builder.setFrame(master);
                builder.setInvoker(new Invoker<>(this));
                builder.setEntity(entity);
                builder.setBudget(players.current().getMoney());
                entityDialog = builder.get();
                entityDialog.addWindowListener(new WindowAdapter()
                {
                    @Override
                    public void windowClosing(WindowEvent e)
                    {
                        state = State.IDLE;
                        master.requestFocus();
                    }
                });
                entityDialog.setVisible(true);
            }
        }
    }

    private AbstractEntity entityBeingPurchased;

    public void pursueHiring(AbstractEntity entity)
    {
        if (state == State.HIRING_BEGUN)
        {
            state = State.HIRING_IN_PROGRESS;

            entityDialog.setVisible(false);
            entityDialog.dispose();
            entityDialog = null;
            entityBeingPurchased = entity;
            players.current().markFor(entityBeingPurchased);

            master.requestFocus();
        }
    }

    /* -------------------- Manager -> ... -------------------- */
    private AbstractEntity entityBeingMoved;
    private Map<Hex, List<Hex>> movementRange;

    public void handleFieldClick(AbstractField field)
    {
        switch (state)
        {
            case BUILDING_IN_PROGRESS ->
            {
                if (world.isMarked(field.getHex()))
                {
                    master.setMoney(players.current().buy(buildingBeingPurchased));
                    world.substitute(field, buildingBeingPurchased);
                }
                world.unmarkAll();
                buildingBeingPurchased = null;

                state = State.IDLE;
                master.requestFocus();
            }
            case HIRING_IN_PROGRESS ->
            {
                if (world.isMarked(field.getHex()))
                {
                    master.setMoney(players.current().buy(entityBeingPurchased));
                    field.setEntity(entityBeingPurchased);
                    entityBeingPurchased.changeField(field);
                }
                world.unmarkAll();
                entityBeingPurchased = null;

                state = State.IDLE;
                master.requestFocus();
            }
            case IDLE ->
            {
                if (field.hasEntity())
                {
                    state = State.MOVING_BEGUN;

                    entityBeingMoved = field.getEntity();
                    entityBeingMoved.mark();

                    movementRange = entityBeingMoved.getMovementRange(world.createAccessor());
                    for (var hex : movementRange.keySet())
                    {
                        world.mark(hex);
                    }
                }
            }
            case MOVING_BEGUN ->
            {
                state = State.IDLE;

                if (world.isMarked(field.getHex()))
                {
                    AbstractField begin = entityBeingMoved.changeField(field);
                    Player player = players.current();

                    if (entityBeingMoved.getType() != EntityType.NAVY)
                    {
                        var path = movementRange.get(field.getHex());
                        for (var hex : path)
                        {
                            AbstractField passedField = world.getFieldAt(hex);
                            player.capture(passedField);
                        }
                        player.capture(field);
                        for (var hex : field.getHex().neighbors())
                        {
                            var neighbor = world.getFieldAt(hex);
                            if (neighbor != null
                                    && neighbor.isPlains()
                                    && !neighbor.hasEntity()
                                    && neighbor.getOwner() != player)
                            {
                                player.capture(neighbor);
                            }
                        }
                    }
                    else
                    {
                        if (begin.isMarine())
                        {
                            player.release(begin);
                        }
                        player.capture(field);
                    }
                }
                entityBeingMoved.unmark();
                entityBeingMoved = null;
                world.unmarkAll();
            }
        }
    }

    public void handleFieldShiftClick(AbstractField field)
    {
        /* has entity -> extract (dialog etc.), in case of a ship: extract = disembark
           (a ship cannot produce a new ship) */
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
