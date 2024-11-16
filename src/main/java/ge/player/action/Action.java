package ge.player.action;

import ge.utilities.*;

/**
 *
 * @param <T>
 * @author Kay Jay O'Nail
 */
public abstract class Action<T>
{
    public abstract void perform(Invoker<T> invoker);
}
