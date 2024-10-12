package my.flow;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashSet;
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
import my.field.CapitalField;
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

    private final BuildingPurchaseManager buildingManager;
    private final EntityPurchaseManager entityManager;
    private final MovementManager movementManager;
    private final ExtractionManager extractionManager;

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
        buildingManager = new BuildingPurchaseManager();
        entityManager = new EntityPurchaseManager();
        movementManager = new MovementManager();
        extractionManager = new ExtractionManager();
    }

    public World getWorld()
    {
        return world;
    }

    public Player getFirstPlayer()
    {
        return players.first();
    }

    public void showBuildingInfo(BuildingField building)
    {
        buildingManager.showInfo(building);
    }

    public void beginBuilding(BuildingField building)
    {
        buildingManager.begin(building);
    }

    public void pursueBuilding()
    {
        buildingManager.pursue();
    }

    public void showEntityInfo(AbstractEntity entity)
    {
        entityManager.showInfo(entity);
    }

    public void beginHiring(AbstractEntity entity)
    {
        entityManager.begin(entity);
    }

    public void pursueHiring()
    {
        entityManager.pursue();
    }

    public void handleFieldClick(AbstractField field)
    {
        switch (state)
        {
            case BUILDING_IN_PROGRESS ->
            {
                buildingManager.finish(field);
            }
            case HIRING_IN_PROGRESS ->
            {
                entityManager.finish(field);
            }
            case IDLE ->
            {
                movementManager.begin(field);
            }
            case MOVING_BEGUN ->
            {
                movementManager.finish(field);
            }
            case EXTRACTION_IN_PROGRESS ->
            {
                extractionManager.finish(field);
            }
        }
    }

    public void handleFieldShiftClick(AbstractField field)
    {
        if (field != null && field.hasEntity())
        {
            extractionManager.begin(field);
        }
    }

    public void pursueExtraction(int number)
    {
        extractionManager.pursue(number);
    }
    
    public void conquerCountry(Player loser)
    {
        JOptionPane.showMessageDialog(master, "%s loses!".formatted(loser.getName()), "Vae victis!", JOptionPane.INFORMATION_MESSAGE);
        loser.die();
        players.remove(loser);
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

    private class BuildingPurchaseManager
    {
        private BuildingField building;
        private BuildingPurchaseDialog dialog;

        void showInfo(BuildingField building)
        {
            var info = new BuildingInfoDialog(master, building);
            info.addWindowListener(new WindowAdapter()
            {
                @Override
                public void windowClosing(WindowEvent e)
                {
                    master.requestFocus();
                }
            });
            info.setVisible(true);
        }

        void begin(BuildingField building)
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
                this.building = building;

                var builder = new BuildingPurchaseDialog.Builder();
                builder.setFrame(master);
                builder.setInvoker(createInvoker());
                builder.setBuilding(building);
                builder.setPrice(players.current().computePriceFor(building));
                dialog = builder.get();
                dialog.addWindowListener(new WindowAdapter()
                {
                    @Override
                    public void windowClosing(WindowEvent e)
                    {
                        state = Manager.State.IDLE;
                        master.requestFocus();
                    }
                });
                dialog.setVisible(true);
            }
        }

        void pursue()
        {
            if (state == State.BUILDING_BEGUN)
            {
                state = State.BUILDING_IN_PROGRESS;

                dialog.setVisible(false);
                dialog.dispose();
                dialog = null;
                players.current().markFor(building);

                master.requestFocus();
            }
        }

        void finish(AbstractField field)
        {
            if (field != null && world.isMarked(field.getHex()))
            {
                master.setMoney(players.current().buy(building));
                world.substitute(field, building);
            }
            world.unmarkAll();
            building = null;

            state = State.IDLE;
            master.requestFocus();
        }
    }

    private class EntityPurchaseManager
    {
        private AbstractEntity entity;
        private EntityPurchaseDialog dialog;

        void showInfo(AbstractEntity entity)
        {
            var info = new EntityInfoDialog(master, entity);
            info.addWindowListener(new WindowAdapter()
            {
                @Override
                public void windowClosing(WindowEvent e)
                {
                    master.requestFocus();
                }
            });
            info.setVisible(true);
        }

        void begin(AbstractEntity entity)
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

                    this.entity = entity;

                    var builder = new EntityPurchaseDialog.Builder();
                    builder.setFrame(master);
                    builder.setInvoker(createInvoker());
                    builder.setEntity(entity);
                    builder.setBudget(players.current().getMoney());
                    dialog = builder.get();
                    dialog.addWindowListener(new WindowAdapter()
                    {
                        @Override
                        public void windowClosing(WindowEvent e)
                        {
                            state = State.IDLE;
                            master.requestFocus();
                        }
                    });
                    dialog.setVisible(true);
                }
            }
        }

        void pursue()
        {
            if (state == State.HIRING_BEGUN)
            {
                state = State.HIRING_IN_PROGRESS;

                dialog.setVisible(false);
                dialog.dispose();
                dialog = null;

                players.current().markFor(entity);

                master.requestFocus();
            }
        }

        void finish(AbstractField field)
        {
            if (field != null && world.isMarked(field.getHex()))
            {
                master.setMoney(players.current().buy(entity));
                field.pin(entity);

                if (field.isCapital())
                {
                    entity.setMovable(true);
                }
            }
            world.unmarkAll();
            entity = null;

            state = State.IDLE;
            master.requestFocus();
        }
    }

    private class MovementManager
    {
        private AbstractEntity entity;
        private Map<Hex, List<Hex>> range;

        void begin(AbstractField field)
        {
            if (field != null && field.hasEntity() && field.isOwned(players.current()))
            {
                state = State.MOVING_BEGUN;

                this.entity = field.getEntity();
                entity.mark();

                range = entity.getMovementRange(world.createAccessor());
                for (var hex : range.keySet())
                {
                    world.mark(hex);
                }
            }
        }

        void finish(AbstractField field)
        {
            state = State.IDLE;

            entity.unmark();
            if (field != null && world.isMarked(field.getHex()))
            {
                AbstractField origin = entity.getField();
                
                Player previousOwner = field.getOwner();
                AbstractEntity resultant = field.interact(entity);

                if (resultant != null)
                {
                    var player = players.current();
                    if (resultant.getType() != EntityType.NAVY)
                    {
                        List<Hex> path = range.get(field.getHex());

                        if (field.isOwned(player))
                        {
                            path.add(field.getHex());
                        }

                        Set<Hex> way = makeWay(path);

                        for (var hex : way)
                        {
                            AbstractField passedField = world.getFieldAt(hex);
                            player.capture(passedField);
                        }
                        
                        if (field.isCapital() && previousOwner != player)
                        {
                            conquerCountry(previousOwner);
                        }
                    }
                    else
                    {
                        if (origin.isMarine())
                        {
                            player.release(origin);
                        }
                        player.capture(field);
                    }
                }
            }
            entity = null;
            world.unmarkAll();
        }

        private Set<Hex> makeWay(List<Hex> path)
        {
            Set<Hex> way = new HashSet<>();

            var player = players.current();
            for (int i = path.size() - 1; i >= 0; --i)
            {
                Hex hex = path.get(i);
                way.add(hex);

                if (((path.size() - 1 - i) & 1) == 0)
                {
                    for (var neighborHex : hex.neighbors())
                    {
                        var neighborField = world.getFieldAt(neighborHex);
                        if (neighborField != null
                            && neighborField.isPlains()
                            && !neighborField.hasEntity()
                            && neighborField.getOwner() != player)
                        {
                            way.add(neighborHex);
                        }
                    }
                }
            }

            return way;
        }
    }

    private class ExtractionManager
    {
        private AbstractEntity entity;
        private AbstractEntity extract;
        private EntityExtractionDialog dialog;
        
        private Map<Hex, List<Hex>> range;

        void begin(AbstractField field)
        {
            state = State.EXTRACTION_BEGUN;

            entity = field.getEntity();

            boolean canExtract;
            try
            {
                canExtract = entity.canExtract();
            }
            catch (AbstractEntity.AccessorIsNeededException e)
            {
                canExtract = entity.canExtract(world.createAccessor());
            }
            if (canExtract)
            {
                var builder = new EntityExtractionDialog.Builder();
                builder.setFrame(master);
                builder.setEntity(entity);
                builder.setInvoker(createInvoker());
                dialog = builder.get();
                dialog.addWindowListener(new WindowAdapter()
                {
                    @Override
                    public void windowClosed(WindowEvent e)
                    {
                        //state = State.IDLE;
                        master.requestFocus();
                    }
                });
                dialog.setVisible(true);
            }
        }

        void pursue(int number)
        {
            state = State.EXTRACTION_IN_PROGRESS;
            
            dialog.setVisible(false);
            dialog.dispose();
            dialog = null;
            
            try
            {
                extract = entity.extract(number);
                
                var field = entity.getField();
                entity.setField(null);
                
                field.pin(extract);
                
                // Now, `entity` "hangs fieldlessly in the air", and `extract` temporarily
                // substitutes `entity` in the original field.
                
                extract.mark();
                
                range = extract.getMovementRange(world.createAccessor());
                for (var hex : range.keySet())
                {
                    world.mark(hex);
                }
            }
            catch (AbstractEntity.OutOfRangeException oor)
            {
                // In practice, no exception will be thrown.
            }
        }

        void finish(AbstractField field)
        {
            state = State.IDLE;

            extract.unmark();
            if (field != null && world.isMarked(field.getHex()))
            {
                // If the clicked field was valid, move the extract to that field.
                // The extrahend should be then placed back to the field of origin.
                // If the extract is being merged with another entity, the remainder
                // should be merged back with the extrahend.
                
                AbstractField origin = extract.getField();
                AbstractEntity resultant = field.interact(extract);

                if (resultant != null)
                {
                    // Unless a lost battle happened, conquer the way.
                    
                    var player = players.current();
                    List<Hex> path = range.get(field.getHex());

                    if (field.isOwned(player))
                    {
                        path.add(field.getHex());
                    }

                    Set<Hex> way = makeWay(path);

                    for (var hex : way)
                    {
                        AbstractField passedField = world.getFieldAt(hex);
                        player.capture(passedField);
                    }                    
                }
                
                AbstractEntity remainder = origin.getEntity();
                entity.pin(origin);
                
                if (remainder != null)
                {
                    entity.merge(remainder);
                }
            }
            else
            {
                extract.unpin().pin(entity);
                entity.merge(extract);
            }
            entity = null;
            world.unmarkAll();
        }
        
        private Set<Hex> makeWay(List<Hex> path)
        {
            Set<Hex> way = new HashSet<>();

            var player = players.current();
            for (int i = path.size() - 1; i >= 0; --i)
            {
                Hex hex = path.get(i);
                way.add(hex);

                if (((path.size() - 1 - i) & 1) == 0)
                {
                    for (var neighborHex : hex.neighbors())
                    {
                        var neighborField = world.getFieldAt(neighborHex);
                        if (neighborField != null
                            && neighborField.isPlains()
                            && !neighborField.hasEntity()
                            && neighborField.getOwner() != player)
                        {
                            way.add(neighborHex);
                        }
                    }
                }
            }

            return way;
        }
    }
}
