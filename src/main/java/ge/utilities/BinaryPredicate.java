package ge.utilities;

/**
 *
 * @author Kay Jay O'Nail
 * @param <L>
 * @param <R>
 */
public interface BinaryPredicate<L, R>
{
    public boolean test(L left, R right);
}
