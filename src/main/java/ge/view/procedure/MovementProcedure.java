package ge.view.procedure;

import ge.entity.*;
import ge.field.*;
import ge.main.*;
import ge.player.*;
import ge.utilities.*;

/**
 *
 * @author Kay Jay O'Nail
 */
public class MovementProcedure extends Procedure
{
    private final Field origin;
    private final Entity entity;
    private final UserPlayer player;
    
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
                    begin();
                }
                case BEGUN ->
                {
                    finish();
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
    
    private void begin()
    {
        stage = MovementStage.BEGUN;
        

    }
    
    private void finish()
    {
        stage = MovementStage.FINISHED;
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
