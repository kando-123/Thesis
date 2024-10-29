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
    
    public abstract Status status();
    public abstract void advance(Object... args) throws ProcessException;
    public abstract void rollback();
    
    public static class ProcessException extends RuntimeException
    {
        public ProcessException()
        {
            super();
        }
        
        public ProcessException(String message)
        {
            super(message);
        }
    }
}
