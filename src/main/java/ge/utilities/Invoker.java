package ge.utilities;

/**
 *
 * @author Kay Jay O'Nail
 * @param <E>
 */
public class Invoker<E>
{
    private final E executor;
    
    public Invoker(E executor)
    {
        this.executor = executor;
    }
    
    public void invoke(Command<E> command)
    {
        command.execute(executor);
    }
}
