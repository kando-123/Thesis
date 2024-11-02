package ge.view.procedure;

import ge.entity.*;
import ge.field.*;
import ge.main.*;
import ge.player.*;
import ge.utilities.*;
import ge.world.*;
import java.util.*;

/**
 *
 * @author Kay Jay O'Nail
 */
public class MovementProcedure extends Procedure
{
    private final Field origin;
    private final Entity entity;
    private final UserPlayer player;
    
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

    public MovementProcedure(Field origin, Invoker<GameplayManager> marker)
    {
        this.origin = origin;
        this.entity = origin.getEntity();
        this.player = (UserPlayer) entity.getOwner();
        
        this.invoker = marker;
        
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
                    var accessor = (WorldAccessor) args[0];
                    begin(accessor);
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
    
    private void begin(WorldAccessor accessor)
    {
        stage = MovementStage.BEGUN;
        
        range = entity.range(origin.getHex(), accessor);
        
        invoker.invoke(new MarkForMovingCommand(true, range));
    }
    
    private void finish(Field field)
    {
        if (field.isMarked())
        {
            stage = MovementStage.FINISHED;
            
            invoker.invoke(new MarkForMovingCommand(false, range));
            origin.takeEntity();
            field.placeEntity(entity);
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
