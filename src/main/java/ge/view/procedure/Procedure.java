package ge.view.procedure;

/**
 *
 * @author Kay Jay O'Nail
 */
public abstract class Procedure
{
    public enum Status
    {
        ONGOING,
        SUCCESS,
        FAILURE;
    }
    
    public abstract void advance(Object... args) throws ProcedureException;
    public abstract void rollback();
    public abstract Status status();
    
    public static class ProcedureException extends RuntimeException
    {
        public ProcedureException()
        {
            super();
        }
        
        public ProcedureException(String message)
        {
            super(message);
        }
    }
}
