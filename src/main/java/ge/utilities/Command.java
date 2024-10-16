package ge.utilities;

/**
 *
 * @author Kay Jay O'Nail
 * @param <E>
 */
public abstract class Command<E>
{
    public abstract void execute(E executor);
}
