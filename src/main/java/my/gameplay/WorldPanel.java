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
public class WorldPanel extends JPanel implements Runnable
{
    private Dimension panelSize;

    private Pixel worldCenter;
    private final int UNIT_STEP = 0;
    
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

    public void setWorld(World newWorld)
    {
        world = newWorld;
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
        super.paintComponent(graphics);
        
        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setBackground(Color.black);
        graphics2D.clearRect(0, 0, panelSize.width, panelSize.height);

        world.draw(graphics2D, worldCenter, scale, panelSize);
    }
    
    void update()
    {
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
    public void run()
    {
        final long framesPerSecond = 60;
        final long period = (int) ((double) 1_000_000_000 / (double) framesPerSecond);
        long recentInstant = System.nanoTime();
        while (!Thread.currentThread().isInterrupted())
        {
            long currentInstant = System.nanoTime();
            if (currentInstant - recentInstant >= period)
            {
                update();
                repaint();
                recentInstant = currentInstant;
            }
        }
    }
}
