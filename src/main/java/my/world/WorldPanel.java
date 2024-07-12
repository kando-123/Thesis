package my.world;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;
import my.input.*;

/**
 *
 * @author Kay Jay O'Nail
 */
public class WorldPanel extends JPanel implements Runnable
{
    private Point screenCenter;
    private Point worldCenter;
    private static final double UNIT_STEP = 5;
    private static final double SIN_30DEG = Math.sin(Math.toRadians(30));
    private static final double COS_30DEG = Math.cos(Math.toRadians(30));

    private double scale;
    private static final double SCALE_FACTOR = 1.01;
    private static final double MAX_SCALE = 2.5;
    private static final double MIN_SCALE = 0.25;

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
        
        screenCenter = new Point(newSize.width / 2, newSize.height / 2);
        worldCenter = new Point(newSize.width / 2, newSize.height / 2);
    }

    @Override
    public void paintComponent(Graphics graphics)
    {
        super.paintComponent(graphics);

        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setBackground(Color.black);
        
        Dimension size = getSize();
        graphics2D.clearRect(0, 0, size.width, size.height);

        world.draw(graphics2D, worldCenter, scale, size);
    }

    void update()
    {
        OrthogonalDirection direction = inputHandler.getShiftingDirection();
        if (direction != null)
        {
            switch (direction)
            {
                case EAST ->
                {
                    worldCenter.xCoord -= UNIT_STEP;
                }
                case SOUTH ->
                {
                    worldCenter.yCoord -= UNIT_STEP;
                }
                case WEST ->
                {
                    worldCenter.xCoord += UNIT_STEP;
                }
                case NORTH ->
                {
                    worldCenter.yCoord += UNIT_STEP;
                }
                case SOUTHEAST ->
                {
                    worldCenter.xCoord -= UNIT_STEP * COS_30DEG;
                    worldCenter.yCoord -= UNIT_STEP * SIN_30DEG;
                }
                case SOUTHWEST ->
                {
                    worldCenter.xCoord += UNIT_STEP * COS_30DEG;
                    worldCenter.yCoord -= UNIT_STEP * SIN_30DEG;
                }
                case NORTHWEST ->
                {
                    worldCenter.xCoord += UNIT_STEP * COS_30DEG;
                    worldCenter.yCoord += UNIT_STEP * SIN_30DEG;
                }
                case NORTHEAST ->
                {
                    worldCenter.xCoord -= UNIT_STEP * COS_30DEG;
                    worldCenter.yCoord += UNIT_STEP * SIN_30DEG;
                }
            }
        }

        if (inputHandler.zoomIn() && scale < MAX_SCALE)
        {
            scale = Math.min(scale * SCALE_FACTOR, MAX_SCALE);

            Point relative = screenCenter.minus(worldCenter);
            Point offset = relative.times(SCALE_FACTOR - 1);
            worldCenter.subtract(offset);
        }
        else if (inputHandler.zoomOut() && scale > MIN_SCALE)
        {
            scale = Math.max(scale / SCALE_FACTOR, MIN_SCALE);

            Point relative = screenCenter.minus(worldCenter);
            Point offset = relative.times(SCALE_FACTOR - 1);
            worldCenter.add(offset);
        }

        int side = world.getSide();
        
        double worldWidth = Hex.computeSurfaceWidth(side, scale * World.HEX_OUTER_RADIUS);
        double worldHeight = Hex.computeSurfaceHeight(side, scale * World.HEX_INNER_RADIUS);
        
        Dimension size = getSize();
        int panelWidth = size.width;
        int panelHeight = size.height;
        
        double maxXCoord;
        double minXCoord;
        double maxYCoord;
        double minYCoord;
        
        if (worldWidth > panelWidth)
        {
            maxXCoord = 0.5 * Hex.computeSurfaceWidth(side, scale * World.HEX_OUTER_RADIUS);
            minXCoord = panelWidth - maxXCoord;
        }
        else
        {
            minXCoord = 0.5 * worldWidth;
            maxXCoord = panelWidth - minXCoord;
        }
        
        if (worldHeight > panelHeight)
        {
            maxYCoord = 0.5 * Hex.computeSurfaceHeight(side, scale * World.HEX_INNER_RADIUS);
            minYCoord = panelHeight - maxYCoord;
        }
        else
        {
            minYCoord = 0.5 * worldHeight;
            maxYCoord = panelHeight - minYCoord;
        }

        if (worldCenter.xCoord > maxXCoord)
        {
            worldCenter.xCoord = maxXCoord;
        }
        else if (worldCenter.xCoord < minXCoord)
        {
            worldCenter.xCoord = minXCoord;
        }

        if (worldCenter.yCoord > maxYCoord)
        {
            worldCenter.yCoord = maxYCoord;
        }
        else if (worldCenter.yCoord < minYCoord)
        {
            worldCenter.yCoord = minYCoord;
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
