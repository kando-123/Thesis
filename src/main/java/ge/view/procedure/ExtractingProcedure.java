package ge.view.procedure;

import ge.entity.*;
import ge.field.*;
import ge.main.*;
import ge.utilities.*;
import ge.world.*;
import java.awt.event.*;
import java.util.Collection;
import javax.swing.*;

/**
 *
 * @author Kay Jay O'Nail
 */
public class ExtractingProcedure extends Procedure
{
    enum ExtractingStage
    {
        INITIATED,
        BEGUN,
        IN_PROGRESS,
        FINISHED,
        ERROR;
    }

    private ExtractingStage stage;

    private final Field origin;
    
    private Entity extrahend;
    private ExtractingDialog dialog;
    
    private final Invoker<GameplayManager> invoker;
    private final WorldAccessor accessor;
    
    private Collection<Hex> range;

    public ExtractingProcedure(Field field, Invoker<GameplayManager> invoker, WorldAccessor accessor)
    {
        stage = ExtractingStage.INITIATED;
        
        this.origin = field;
        this.invoker = invoker;
        this.accessor = accessor;
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
                    var number = (int) args[0];
                    progress(number);
                }
                case IN_PROGRESS ->
                {
                    var field = (Field) args[0];
                    finish(field);
                }
                case FINISHED, ERROR ->
                {
                    throw new ProcedureException("Already finished.");
                }
            }
        }
        catch (ClassCastException cce)
        {
            throw new ProcedureException("Wrong argument.");
        }
    }

    private void begin(JFrame frame)
    {
        stage = ExtractingStage.BEGUN;
        
        var entity = origin.getEntity();
        int number = entity.getNumber();
        var type = entity.getExtractedType();
        
        // Check if there's place at all.
        
        dialog = new ExtractingDialog(frame, type, number, new Invoker<>(this));
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

    private void progress(int number)
    {
        stage = ExtractingStage.IN_PROGRESS;
        
        dialog.setVisible(false);
        dialog.dispose();
        dialog = null;
        
        extrahend = origin.getEntity();
        var extract = extrahend.extract(number);
        origin.setEntity(extract);
        
        range = extract.range(origin.getHex(), accessor);
        invoker.invoke(new MarkForMovingCommand(true, range));
    }

    private void finish(Field field)
    {
        stage = ExtractingStage.FINISHED;
        
        if (range.contains(field.getHex()))
        {
            invoker.invoke(new MarkForMovingCommand(false, range));
            invoker.invoke(new MoveCommand(origin, field));
            
            if (origin.isOccupied())
            {
                var remainder = origin.getEntity();
                extrahend.merge(remainder);
            }
            origin.setEntity(extrahend);
        }
        else
        {
            rollback();
        }
        
        // Move the extract.
        // Merge the remainder back, if any.
        // Place the entity back.
    }

    @Override
    public void rollback()
    {
        // Merge the extract back.
        // Place the entity back.
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

}
