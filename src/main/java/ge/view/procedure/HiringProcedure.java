package ge.view.procedure;

import ge.entity.*;
import ge.field.*;
import ge.main.*;
import ge.player.*;
import ge.utilities.*;
import java.awt.event.*;
import javax.swing.*;

/**
 *
 * @author Kay Jay O'Nail
 */
public class HiringProcedure extends Procedure
{
    private final EntityType type;
    private final UserPlayer player;
    private final Invoker<GameplayManager> invoker;
    
    private HiringDialog dialog;
    private Integer number;
    
    private enum HiringStage
    {
        INITIATED,
        BEGUN,
        IN_PROGRESS,
        FINISHED,
        ERROR;
    }
    
    private HiringStage stage;

    public HiringProcedure(EntityType type, UserPlayer player, Invoker<GameplayManager> invoker)
    {
        this.type = type;
        this.player = player;
        this.invoker = invoker;
        
        stage = HiringStage.INITIATED;
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
                    number = (Integer) args[0];
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
            stage = HiringStage.ERROR;
            throw new ProcedureException(cce.getMessage());
        }
    }
    
    private void begin(JFrame frame)
    {
        if (!player.hasPlace(type))
        {
            stage = HiringStage.ERROR;
            JOptionPane.showMessageDialog(frame,
                    "You have no place for this building.\nShift-click the button for info.");
            frame.requestFocus();
        }
        else if (!player.hasMoney(type))
        {
            stage = HiringStage.ERROR;
            JOptionPane.showMessageDialog(frame,
                    "You have too little money.\nShift-click the button for info.");
            frame.requestFocus();
        }
        else
        {
            stage = HiringStage.BEGUN;
            var builder = new HiringDialog.Builder();
            dialog = builder.setFrame(frame)
                    .setInvoker(new Invoker<>(this))
                    .setEntity(type)
                    .setBudget(player.getMoney())
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
        stage = HiringStage.IN_PROGRESS;
        dialog.setVisible(false);
        dialog.dispose();
        dialog = null;
        
        invoker.invoke(new MarkForHiringCommand(true, player, type));
    }
    
    private void finish(Field field)
    {
        if (field != null && field.isMarked() && field instanceof Spawner spawner)
        {
            stage = HiringStage.FINISHED;
            
            invoker.invoke(new MarkForHiringCommand(false, player, type));
            var entity = Entity.newInstance(type, player, number);
            spawner.spawn(entity);
            player.buy(type, number);
        }
        else
        {
            stage = HiringStage.ERROR;
            invoker.invoke(new MarkForHiringCommand(false, player, type));
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
                invoker.invoke(new MarkForHiringCommand(false, player, type));
            }
        }
        stage = HiringStage.ERROR;
    }
}
