package ge.view.procedure;

import ge.entity.*;
import ge.field.*;
import ge.main.*;
import ge.utilities.*;
import ge.world.*;
import java.util.*;

/**
 *
 * @author Kay Jay O'Nail
 */
public class MovingProcedure extends Procedure
{
    private Field origin;
    private Entity entity;
    
    private final WorldAccessor accessor;
    
    private Set<Hex> range;
    
    private final Invoker<GameplayManager> invoker;
    
    private enum MovementStage
    {
        INITIATED,
        BEGUN,
        FINISHED,
        ERROR;
    }
    
    private MovementStage stage;

    public MovingProcedure(WorldAccessor accessor, Invoker<GameplayManager> invoker)
    {
        this.accessor = accessor;
        this.invoker = invoker;
        
        stage = MovementStage.INITIATED;
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
                    var field = (Field) args[0];
                    begin(field);
                }
                case BEGUN ->
                {
                    var field = (Field) args[0];
                    finish(field);
                }
                case FINISHED, ERROR ->
                {
                    throw new ProcedureException("Already terminated.");
                }
            }
        }
        catch (ClassCastException cce)
        {
            stage = MovementStage.ERROR;
            throw new ProcedureException(cce.getMessage());
        }
    }
    
    private void begin(Field origin)
    {
        stage = MovementStage.BEGUN;
        
        this.origin = origin;
        this.entity = origin.getEntity();
        
        range = entity.range(origin.getHex(), accessor);
        
        if (!range.isEmpty())
        {
            invoker.invoke(new MarkForMovingCommand(true, range));
        }
        else
        {
            stage = MovementStage.ERROR;
        }
    }
    
    private void finish(Field target)
    {
        if (target.isMarked())
        {
            stage = MovementStage.FINISHED;
            invoker.invoke(new MarkForMovingCommand(false, range));
            invoker.invoke(new MoveCommand(origin, target));
        }
        else
        {
            stage = MovementStage.ERROR;
            
            invoker.invoke(new MarkForMovingCommand(false, range));
        }
    }

    @Override
    public void rollback()
    {
        switch (stage)
        {
            case BEGUN ->
            {
                stage = MovementStage.ERROR;
                
                invoker.invoke(new MarkForMovingCommand(false, range));
                range = null;
            }
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
}
