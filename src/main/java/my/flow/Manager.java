package my.flow;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import javax.swing.JOptionPane;
//import java.util.Stack;
import my.command.Invoker;
import my.entity.AbstractEntity;
import my.gui.EntityInfoDialog;
import my.gui.EntityPurchaseDialog;
import my.player.Player;
import my.player.PlayerConfiguration;
import my.player.PlayersQueue;
import my.field.AbstractField;
import my.field.BuildingField;
import my.gui.BuildingInfoDialog;
import my.gui.BuildingPurchaseDialog;
import my.gui.EntityExtractionDialog;
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
        
        MOVING_BEGUN,
        
        EXTRACTION_BEGUN,
        EXTRACTION_IN_PROGRESS;
    }

    private State state;

    private final Master master;
    private final World world;
    private final PlayersQueue players;
//    private Stack<ReversibleCommand> executedCommands;
//    private Stack<ReversibleCommand> undoneCommands;
    
    private final BuildingPurchaseManager buildingPurchaseManager;
//    private final EntityPurchaseManager entityPurchaseManager;
    private final MovementManager movementManager;
//    private final ExtractionManager extractionManager;

    public Manager(Master master, WorldConfiguration worldConfiguration, List<PlayerConfiguration> playerConfigurations)
    {
        this.master = master;

        state = State.IDLE;

        world = new World(worldConfiguration);
        players = new PlayersQueue(playerConfigurations, world.createAccessor(), world.createMarker());

        Hex[] capitals = world.locateCapitals(playerConfigurations.size());
        players.initCountries(capitals, world);

//        executedCommands = new Stack();
//        undoneCommands = new Stack();
        
        buildingPurchaseManager = new BuildingPurchaseManager();
//        entityPurchaseManager = new EntityPurchaseManager();
        movementManager = new MovementManager(world.createMarker(), world.createAccessor());
//        extractionManager = new ExtractionManager();
    }

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
        buildingPurchaseManager.showInfo();
    }

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

                buildingPurchaseManager.begin();
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
    

    public void handleFieldClick(AbstractField field)
    {
        switch (state)
        {
            case BUILDING_IN_PROGRESS ->
            {
                if (field != null && world.isMarked(field.getHex()))
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
                if (field != null && world.isMarked(field.getHex()))
                {
                    master.setMoney(players.current().buy(entityBeingPurchased));
                    field.setEntity(entityBeingPurchased);
                    entityBeingPurchased.setField(field);
                    
                    if (field.isCapital())
                    {
                        entityBeingPurchased.setMovable(true);
                    }
                }
                world.unmarkAll();
                entityBeingPurchased = null;

                state = State.IDLE;
                master.requestFocus();
            }
            case IDLE ->
            {
                if (field != null && field.hasEntity() && field.isOwned(players.current()))
                {
                    state = State.MOVING_BEGUN;

                    movementManager.begin(field.getEntity());
                }
            }
            case MOVING_BEGUN ->
            {
                state = State.IDLE;

                movementManager.finish(field, players.current());
            }
        }
    }

    private AbstractEntity entityBeingExtracted;
            
    public void handleFieldShiftClick(AbstractField field)
    {
        if (field != null && field.hasEntity())
        {
            state = State.EXTRACTION_BEGUN;
            
            var entity = field.getEntity();
            if (entity.canExtract(world.createAccessor()))
            {
                entityBeingExtracted = entity;
                
                var dialog = new EntityExtractionDialog(master, entity);
                dialog.setVisible(true);
                dialog.addWindowListener(new WindowAdapter()
                {
                    @Override
                    public void windowClosed(WindowEvent e)
                    {
                        state = State.IDLE;
                        master.requestFocus();
                    }
                });
            }
        }
    }
    
    public void pursueExtraction()
    {
        
    }

    public void undo()
    {

    }

    public void redo()
    {

    }

    public void nextPlayer()
    {
        state = State.IDLE;
        
        Player current = players.current();
        current.endRound();

        Player next = players.next();
        master.setUser(next);
        next.resetEntities();

        Hex hex = next.getCapitalHex();
        master.setCenter(hex);
    }

    public Invoker<Manager> createInvoker()
    {
        return new Invoker(this);
    }
}
