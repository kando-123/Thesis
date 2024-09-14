package my.world;

import my.utils.Hex;

/**
 *
 * @author Kay Jay O'Nail
 */
public class WorldMarker
{
    private final World world;

    public WorldMarker(World world)
    {
        this.world = world;
    }

    public void mark(Hex hex)
    {
        world.mark(hex);
    }

    public void unmark(Hex hex)
    {
        world.unmark(hex);
    }

    public void unmarkAll()
    {
        world.unmarkAll();
    }

    public boolean isMarked(Hex hex)
    {
        return world.isMarked(hex);
    }
}
