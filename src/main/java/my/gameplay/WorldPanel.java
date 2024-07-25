package my.gameplay;

import my.utils.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import my.world.*;


/**
 *
 * @author Kay Jay O'Nail
 */
public class WorldPanel extends JPanel implements Runnable, MouseListener
{
    private my.utils.Point panelCenter;
    private my.utils.Point worldCenter;
    private static final double UNIT_STEP = 5;
    private static final double SIN_30DEG = Math.sin(Math.toRadians(30));
    private static final double COS_30DEG = Math.cos(Math.toRadians(30));

    private double scale;
    private static final double SCALE_FACTOR = 1.01;
    private static final double MAX_SCALE = 2.5;
    private static final double MIN_SCALE = 0.25;

    private InputHandler inputHandler;

    private World world;

    public WorldPanel()
    {
        scale = 1.0;
        addMouseListener(this);
    }

    public void setWorld(World newWorld)
    {
        world = newWorld;
    }
    
    public void setInputHandler(InputHandler newInputHandler)
    {
        inputHandler = newInputHandler;
    }

    @Override
    public void setPreferredSize(Dimension newSize)
    {
        super.setPreferredSize(newSize);
        
        panelCenter = new my.utils.Point(newSize.width / 2, newSize.height / 2);
        worldCenter = new my.utils.Point(newSize.width / 2, newSize.height / 2);
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
        
        Dimension size = getSize();
        panelCenter.xCoord = (double) size.width / 2.0;
        panelCenter.yCoord = (double) size.height / 2.0;
        
        if (inputHandler.zoomIn() && scale < MAX_SCALE)
        {
            scale = Math.min(scale * SCALE_FACTOR, MAX_SCALE);

            my.utils.Point relative = panelCenter.minus(worldCenter);
            my.utils.Point offset = relative.times(SCALE_FACTOR - 1);
            worldCenter.subtract(offset);
        }
        else if (inputHandler.zoomOut() && scale > MIN_SCALE)
        {
            scale = Math.max(scale / SCALE_FACTOR, MIN_SCALE);

            my.utils.Point relative = panelCenter.minus(worldCenter);
            my.utils.Point offset = relative.times(SCALE_FACTOR - 1);
            worldCenter.add(offset);
        }

        int side = world.getSide();
        
        double worldWidth = Hex.computeSurfaceWidth(side, scale * World.HEX_OUTER_RADIUS);
        double worldHeight = Hex.computeSurfaceHeight(side, scale * World.HEX_INNER_RADIUS);
        
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
        while (true)
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

    @Override
    public void mouseClicked(MouseEvent e)
    {
        java.awt.Point point = e.getPoint();
        double globalX = point.x;
        double globalY = point.y;
        double relativeX = globalX - worldCenter.xCoord;
        double relativeY = globalY - worldCenter.yCoord;
        Hex hex = Hex.getHexAt(relativeX, relativeY, World.HEX_OUTER_RADIUS * scale, World.HEX_INNER_RADIUS * scale);
        world.mark((field) -> field.getHex().equals(hex));
    }

    @Override
    public void mousePressed(MouseEvent e)
    {
        return;
    }

    @Override
    public void mouseReleased(MouseEvent e)
    {
        return;
    }

    @Override
    public void mouseEntered(MouseEvent e)
    {
        return;
    }

    @Override
    public void mouseExited(MouseEvent e)
    {
        return;
    }
}
