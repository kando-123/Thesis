package ge.world;

/**
 *
 * @author Kay Jay O'Nail
 */
public class WorldManager
{
    private final World world;
    
    public WorldManager(WorldConfig config)
    {
        world = new World(config);
    }
    
    public WorldAccessor createAccessor()
    {
        return new WorldAccessor(world);
    }
    
    public WorldRenderer createRenderer()
    {
        return new WorldRenderer(world);
    }
}