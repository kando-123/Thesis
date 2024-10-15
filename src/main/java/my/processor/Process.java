package my.processor;

/**
 *
 * @param <E>
 * @author Kay Jay O'Nail
 */
public interface Process<E>
{
    public boolean isReady();
    public void process(E executor);
}
