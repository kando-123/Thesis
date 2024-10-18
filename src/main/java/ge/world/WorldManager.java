package ge.world;

import ge.main.*;
import ge.utilities.*;

/**
 *
 * @author Kay Jay O'Nail
 */
public class WorldManager
{
    private final Invoker<Engine> invoker;

    public WorldManager(Invoker<Engine> invoker)
    {
        this.invoker = invoker;
    }
}
