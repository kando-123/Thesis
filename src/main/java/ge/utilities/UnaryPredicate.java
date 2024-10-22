package ge.utilities;

/**
 *
 * @author Kay Jay O'Nail
 * @param <T>
 */
@FunctionalInterface
public interface UnaryPredicate<T>
{
    public boolean test(T item);
}
