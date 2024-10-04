package my.flow;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import my.command.Invoker;
import my.gui.BuildingInfoDialog;
import my.gui.BuildingPurchaseDialog;

/**
 *
 * @author Kay Jay O'Nail
 */
public class BuildingPurchaseManager
{
    private BuildingPurchaseDialog buildingDialog;

    public void showInfo()
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

    public void begin()
    {
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
                state = Manager.State.IDLE;
                master.requestFocus();
            }
        });
        buildingDialog.setVisible(true);
    }

    public void pursue()
    {
        
    }

    public void finish()
    {
    }
}
