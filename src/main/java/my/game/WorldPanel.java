package my.game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JPanel;
import my.utils.DoublesDoublet;
import my.utils.Hex;
import my.world.InputHandler;
import my.world.OrthogonalDirection;
import my.world.World;


/**
 *
 * @author Kay Jay O'Nail
 */
public class WorldPanel extends JPanel implements Runnable, MouseListener
{
    private DoublesDoublet panelCenter;
    private DoublesDoublet worldCenter;
    private static final double UNIT_STEP = 5;
    private static final double SIN_30DEG = Math.sin(Math.toRadians(30));
    private static final double COS_30DEG = Math.cos(Math.toRadians(30));

    private double scale;
    private static final double SCALE_FACTOR = 1.01;
    private static final double MAX_SCALE = 2.5;
    private static final double MIN_SCALE = 0.25;

    private InputHandler inputHandler;

    private World world;
    
    private final Manager manager;

    public WorldPanel(Manager manager)
    {
        this.manager = manager;
        
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
        
        panelCenter = new DoublesDoublet(newSize.width / 2, newSize.height / 2);
        worldCenter = new DoublesDoublet(newSize.width / 2, newSize.height / 2);
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

            DoublesDoublet relative = panelCenter.minus(worldCenter);
            DoublesDoublet offset = relative.times(SCALE_FACTOR - 1);
            worldCenter.subtract(offset);
        }
        else if (inputHandler.zoomOut() && scale > MIN_SCALE)
        {
            scale = Math.max(scale / SCALE_FACTOR, MIN_SCALE);

            DoublesDoublet relative = panelCenter.minus(worldCenter);
            DoublesDoublet offset = relative.times(SCALE_FACTOR - 1);
            worldCenter.add(offset);
        }

        int side = world.getSide();
        
        double worldWidth = Hex.computeSurfaceWidth(side, scale * World.HEX_OUTER_RADIUS);
        double worldHeight = Hex.computeSurfaceHeight(side, scale * World.HEX_INNER_RADIUS);
        
        int panelWidth = size.width;
        int panelHeight = size.height;
        
        double xMax;
        double xMin;
        double yMax;
        double yMin;
        
        if (worldWidth < panelWidth)
        {
            xMin = 0;
            xMax = panelWidth;
        }
        else
        {
            xMin = 0.5 * (panelWidth - worldWidth);
            xMax = 0.5 * (panelWidth + worldWidth);
        }
        
        if (worldHeight < panelHeight)
        {
            yMin = 0;
            yMax = panelHeight;
        }
        else
        {
            yMin = 0.5 * (panelHeight - worldHeight);
            yMax = 0.5 * (panelHeight + worldHeight);
        }

        if (worldCenter.xCoord > xMax)
        {
            worldCenter.xCoord = xMax;
        }
        else if (worldCenter.xCoord < xMin)
        {
            worldCenter.xCoord = xMin;
        }

        if (worldCenter.yCoord > yMax)
        {
            worldCenter.yCoord = yMax;
        }
        else if (worldCenter.yCoord < yMin)
        {
            worldCenter.yCoord = yMin;
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
//        world.unmarkAll();
        
        Point point = e.getPoint();
        double globalX = point.x;
        double globalY = point.y;
        double relativeX = globalX - worldCenter.xCoord;
        double relativeY = globalY - worldCenter.yCoord;
        Hex hex = Hex.getHexAt(relativeX, relativeY, World.HEX_OUTER_RADIUS * scale, World.HEX_INNER_RADIUS * scale);
        
//        manager.fieldSelected(world.getFieldAt(hex));
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
