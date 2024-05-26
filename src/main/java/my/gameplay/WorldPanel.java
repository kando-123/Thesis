package my.gameplay;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;
import my.input.*;
import my.world.*;
import my.world.field.*;

/**
 *
 * @author Kay Jay O'Nail
 */
public class WorldPanel extends JPanel
{
    private Dimension panelSize;
    private Pixel worldCenter;
    private double scale;
    
    private final double SCALE_FACTOR = 1.01;
    private final double MAX_SCALE = 2.5;
    private final double MIN_SCALE = 0.25;

    private final InputHandler inputHandler;
    
    private World world;

    public WorldPanel()
    {
        scale = 1.0;
        inputHandler = InputHandler.getInstance();
    }

    public void makeWorld(WorldParameters parameters)
    {
        FieldManager.getInstance();
        world = new World(parameters);
    }

    public void update()
    {
        OrthogonalDirection shift = inputHandler.getShiftingDirection();
        if (shift != null)
        {
            worldCenter.add(shift.getOffset());
        }

        if (inputHandler.zoomIn())
        {
            scale = Math.min(scale * SCALE_FACTOR, MAX_SCALE);
        }
        else if (inputHandler.zoomOut())
        {
            scale = Math.max(scale / SCALE_FACTOR, MIN_SCALE);
        }
    }
    
    @Override
    public void setPreferredSize(Dimension newSize)
    {
        super.setPreferredSize(newSize);
        
        panelSize = newSize;
        worldCenter = new Pixel(panelSize.width / 2, panelSize.height / 2);
    }

    @Override
    public void paintComponent(Graphics graphics)
    {
        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setBackground(Color.black);
        graphics2D.clearRect(0, 0, panelSize.width, panelSize.height);
        
        world.draw(graphics2D, worldCenter, scale, panelSize);
    }
}
