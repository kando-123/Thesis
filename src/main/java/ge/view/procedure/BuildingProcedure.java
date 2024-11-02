package ge.view.procedure;

import ge.field.*;
import ge.main.*;
import ge.player.*;
import ge.utilities.*;
import ge.view.*;
import java.awt.event.*;
import javax.swing.*;

/**
 *
 * @author Kay Jay O'Nail
 */
public class BuildingProcedure extends Procedure
{
    private final BuildingType type;
    private final UserPlayer player;
    private final Invoker<GameplayManager> invoker;

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

    public BuildingProcedure(BuildingType building, UserPlayer player, Invoker<GameplayManager> invoker)
    {
        this.type = building;
        this.player = player;
        this.invoker = invoker;

        stage = BuildingStage.INITIATED;
    }

    @Override
    public void advance(Object... args) throws ProcedureException
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
                    throw new ProcedureException("Already terminated");
                }
            }
        }
        catch (ClassCastException cce)
        {
            stage = BuildingStage.ERROR;
            throw new ProcedureException(cce.getMessage());
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
                    .setInvoker(new Invoker<>(this))
                    .setBuilding(type)
                    .setPrice(player.priceForNext(type))
                    .get();
            assert (dialog != null);
            dialog.addWindowListener(new WindowAdapter()
            {
                @Override
                public void windowClosing(WindowEvent e)
                {
                    frame.requestFocus();
                }

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
        
        invoker.invoke(new MarkForBuildingCommand(true, player, type));
    }

    private void finish(Field field)
    {
        if (field != null && field.isMarked())
        {
            stage = BuildingStage.FINISHED;
            
            invoker.invoke(new MarkForBuildingCommand(false, player, type));
            var building = BuildingField.newInstance(type, field.getHex());
            building.setOwner(player);
            player.buy(type);
            invoker.invoke(new BuildCommand(building));
        }
        else
        {
            stage = BuildingStage.ERROR;
            invoker.invoke(new MarkForBuildingCommand(true, player, type));
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
        switch (stage)
        {
            case IN_PROGRESS ->
            {
                stage = BuildingStage.ERROR;
                invoker.invoke(new MarkForBuildingCommand(false, player, type));
//                player.markPlaces(false, type);
            }
        }
    }
}
