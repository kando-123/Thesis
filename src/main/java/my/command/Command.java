package my.command;

/**
 *
 * @author Kay Jay O'Nail
 * @param <E>
 */
public abstract class Command<E>
{
    public abstract void execute(E executor);
    
    public boolean isReversible()
    {
        return false;
    }
}
