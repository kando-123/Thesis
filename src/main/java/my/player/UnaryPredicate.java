package my.player;

/**
 *
 * @author Kay Jay O'Nail
 * @param <T>
 */
public interface UnaryPredicate<T>
{
    public boolean test(T item);
}