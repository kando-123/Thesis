package ge.view.procedure;

import ge.field.*;
import ge.player.*;
import ge.utilities.*;
import ge.view.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author Kay Jay O'Nail
 */
public class BuildingProcedure extends Procedure
{
    private final BuildingType building;
    private final UserPlayer player;
    private final Invoker<ViewManager> invoker;

    private BuildingPurchaseDialog dialog;

    enum BuildingStage
    {
        INITIATED,
        BEGUN,
        IN_PROGRESS,
        FINISHED,
        ERROR;
    }

    private BuildingStage stage;

    public BuildingProcedure(BuildingType building, UserPlayer player, Invoker<ViewManager> invoker)
    {
        this.building = building;
        this.player = player;
        this.invoker = invoker;

        stage = BuildingStage.INITIATED;
    }

    @Override
    public void advance(Object... args) throws ProcessException
    {
        try
        {
            switch (stage)
            {
                case INITIATED ->
                {
                    var frame = (JFrame) args[0];
                    begin(frame);
                }
                case BEGUN ->
                {
                    progress();
                }
                case IN_PROGRESS ->
                {
                    var field = (Field) args[0];
                    finish(field);
                }
                case FINISHED, ERROR ->
                {
                    throw new ProcessException("Already finished.");
                }
            }
        }
        catch (ClassCastException cce)
        {
            stage = BuildingStage.ERROR;
            throw new ProcessException("Wrong argument.");
        }
    }

    private void begin(JFrame frame)
    {
        if (!player.hasPlace(building))
        {
            stage = BuildingStage.ERROR;
            JOptionPane.showMessageDialog(frame,
                    "You have no place for this building.\nShift-click the button for info.");
            frame.requestFocus();
        }
        else if (!player.hasMoney(building))
        {
            stage = BuildingStage.ERROR;
            JOptionPane.showMessageDialog(frame,
                    "You have too little money.\nShift-click the button for info.");
            frame.requestFocus();
        }
        else
        {
            stage = BuildingStage.BEGUN;
            var builder = new BuildingPurchaseDialog.Builder();
            dialog = builder.setFrame(frame)
                    .setInvoker(invoker)
                    .setBuilding(building)
                    .setPrice(player.priceForNext(building))
                    .get();
            assert (dialog != null);
            dialog.addWindowListener(new WindowAdapter()
            {
                @Override
                public void windowClosed(WindowEvent e)
                {
                    frame.requestFocus();
                }
            });
            dialog.setVisible(true);
        }
    }

    private void progress()
    {
        dialog.setVisible(false);
        dialog.dispose();
        dialog = null;
        
        player.markPlaces(true, building);
    }

    private void finish(Field field)
    {
        if (field != null)
        {
            player.markPlaces(false, building);
            // substitute
        }
    }

    @Override
    public Status status()
    {
        return switch (stage)
        {
            case FINISHED ->
            {
                yield Status.SUCCESS;
            }
            case ERROR ->
            {
                yield Status.FAILURE;
            }
            default ->
            {
                yield Status.ONGOING;
            }
        };
    }

    @Override
    public void rollback()
    {

    }

}
