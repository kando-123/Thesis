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
    private final BuildingType type;
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
        this.type = building;
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
        if (!player.hasPlace(type))
        {
            stage = BuildingStage.ERROR;
            JOptionPane.showMessageDialog(frame,
                    "You have no place for this building.\nShift-click the button for info.");
            frame.requestFocus();
        }
        else if (!player.hasMoney(type))
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
                    .setBuilding(type)
                    .setPrice(player.priceForNext(type))
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
        stage = BuildingStage.IN_PROGRESS;
        dialog.setVisible(false);
        dialog.dispose();
        dialog = null;
        
        player.markPlaces(true, type);
    }

    private void finish(Field field)
    {
        if (field != null && field.isMarked())
        {
            stage = BuildingStage.FINISHED;
            
            player.markPlaces(false, type);
            var building = BuildingField.newInstance(type, field.getHex());
            building.setOwner(player);
            player.buy(type);
            invoker.invoke(new FinishBuildingCommand(building));
        }
        else
        {
            stage = BuildingStage.ERROR;
            player.markPlaces(false, type);
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
