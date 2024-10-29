package ge.view.procedure;

import ge.field.*;
import ge.player.*;
import ge.utilities.*;
import ge.view.*;

/**
 *
 * @author Kay Jay O'Nail
 */
public class BuildingProcedure extends Procedure
{
    private final BuildingType building;
    private final Player player;
    private final Invoker<ViewManager> invoker;
    
    enum BuildingStage
    {
        INITIATED,
        BEGUN,
        IN_PROGRESS,
        FINISHED,
        ERROR;
    }
    
    private BuildingStage stage;
    
    public BuildingProcedure(BuildingType building, Player player, Invoker<ViewManager> invoker)
    {
        this.building = building;
        this.player = player;
        this.invoker = invoker;
        
        stage = BuildingStage.INITIATED;
    }
    
    @Override
    public void advance(Object... args) throws ProcessException
    {
        switch (stage)
        {
            case INITIATED ->
            {
                begin();
            }
            case BEGUN ->
            {
                progress();
            }
            case IN_PROGRESS ->
            {
                finish();
            }
            case FINISHED, ERROR ->
            {
                throw new ProcessException("Already finished.");
            }
        }
    }
    
    private void begin()
    {
        if (!player.hasPlace(building))
        {
            stage = BuildingStage.ERROR;
            invoker.invoke(new ShowNoPlaceCommand());
        }
        else if (!player.hasMoney(building))
        {
            stage = BuildingStage.ERROR;
            invoker.invoke(new ShowNoMoneyCommand());
        }
        else
        {
            
        }
    }
    
    private void progress()
    {
        
    }
    
    private void finish()
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

    @Override
    public void rollback()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
