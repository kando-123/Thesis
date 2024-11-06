package ge.view.procedure;

/**
 *
 * @author Kay Jay O'Nail
 */
public class ExtractingProcedure extends Procedure
{
    @Override
    public void advance(Object... args) throws ProcedureException
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void rollback()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Status status()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
