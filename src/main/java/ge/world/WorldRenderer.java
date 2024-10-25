package ge.world;

import ge.utilities.*;
import java.awt.*;

/**
 *
 * @author Kay Jay O'Nail
 */
public class WorldRenderer
{
    private final World world;
    
    WorldRenderer(World world)
    {
        this.world = world;
    }
    
    public void draw(Graphics2D graphics, Doublet<Double> center, double scale, Dimension size)
    {
        world.draw(graphics, center, scale, size);
    }
    
    public int getSide()
    {
        return world.side;
    }
}
