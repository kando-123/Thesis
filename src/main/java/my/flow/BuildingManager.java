package my.flow;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JOptionPane;
import my.command.Invoker;
import my.gui.BuildingInfoDialog;
import my.gui.BuildingPurchaseDialog;
import my.gui.Master;
import my.player.Player;
import my.field.AbstractField;
import my.field.BuildingField;
import my.world.WorldMarker;
import my.world.WorldMutator;

/**
 *
 * @author Kay Jay O'Nail
 */
public class BuildingManager
{
    private final Master master;
    private final WorldMarker marker;
    private final WorldMutator mutator;
    
    private BuildingField building;
    private Player player;
    
    private BuildingPurchaseDialog dialog;

    public BuildingManager(Master master, WorldMarker marker, WorldMutator mutator)
    {
        this.master = master;
        this.marker = marker;
        this.mutator = mutator;
    }

    public void showInfo(BuildingField building)
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

    public void begin(BuildingField building, Player player, Invoker<Manager> invoker)
    {
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
            this.building = building;
            this.player = player;

            var builder = new BuildingPurchaseDialog.Builder();
            builder.setFrame(master);
            builder.setInvoker(invoker);
            builder.setBuilding(building);
            builder.setPrice(player.computePriceFor(building));
            dialog = builder.get();
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
    }

    public void pursue()
    {
        dialog.setVisible(false);
        dialog.dispose();
        dialog = null;
        player.markFor(building);

        master.requestFocus();
    }

    public void finish(AbstractField field)
    {
        build(building, field);
        building = null;
    }

    public void build(BuildingField newBuilding, AbstractField oldField)
    {
        if (oldField != null && marker.isMarked(oldField.getHex()))
        {
            master.setMoney(player.buy(newBuilding));
            mutator.substitute(oldField, newBuilding);
        }
        marker.unmarkAll();
        master.requestFocus();
    }
    
    private Invoker<BuildingManager> createInvoker()
    {
        return new Invoker<>(this);
    }
}
