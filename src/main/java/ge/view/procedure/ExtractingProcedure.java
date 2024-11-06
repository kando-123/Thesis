package ge.view.procedure;

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

    public ExtractingProcedure()
    {
        stage = ExtractingStage.INITIATED;
    }
    
    @Override
    public void advance(Object... args) throws ProcedureException
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
                throw new ProcedureException("Already finished.");
            }
        }
    }
    
    private void begin()
    {
        // Show the dialog.
    }
    
    private void progress()
    {
        // Close the dialog.
        // Substitute the entity with the extract.
        // Mark the movement range.
    }
    
    private void finish()
    {
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
