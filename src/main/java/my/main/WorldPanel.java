package my.main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JPanel;
import my.command.HandleFieldCommand;
import my.command.Invoker;
import my.field.AbstractField;
import my.utils.Doublet;
import my.utils.Hex;
import my.world.OrthogonalDirection;
import my.world.World;


/**
 *
 * @author Kay Jay O'Nail
 */
public class WorldPanel extends JPanel implements Runnable, MouseListener
{
    private Doublet<Double> panelCenter;
    private Doublet<Double> worldCenter;
    private static final double UNIT_STEP = 5;
    private static final double SIN_30DEG = Math.sin(Math.toRadians(30));
    private static final double COS_30DEG = Math.cos(Math.toRadians(30));

    private double scale;
    private static final double SCALE_FACTOR = 1.01;
    private static final double MAX_SCALE = 2.5;
    private static final double MIN_SCALE = 0.25;

    private InputHandler inputHandler;

    private World world;
    
    private final Invoker<Manager> invoker;

    public WorldPanel(Invoker<Manager> invoker)
    {
        this.invoker = invoker;
        
        scale = 1.0;
        addMouseListener(this);
    }

    public void setWorld(World newWorld)
    {
        world = newWorld;
    }
    
    public void setCenter(Hex hex)
    {
        double scaledOuterRadius = scale * World.HEX_OUTER_RADIUS;
        double scaledInnerRadius = scale * World.HEX_INNER_RADIUS;
        Doublet<Double> center = hex.getCentralPoint(scaledOuterRadius, scaledInnerRadius);
        worldCenter.left = panelCenter.left - center.left;
        worldCenter.right = panelCenter.right - center.right;
    }
    
    public void setInputHandler(InputHandler newInputHandler)
    {
        inputHandler = newInputHandler;
    }

    @Override
    public void setPreferredSize(Dimension newSize)
    {
        super.setPreferredSize(newSize);
        
        panelCenter = new Doublet<>((double) newSize.width / 2, (double) newSize.height / 2);
        worldCenter = new Doublet<>((double) newSize.width / 2, (double) newSize.height / 2);
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
                    worldCenter.left -= UNIT_STEP;
                }
                case SOUTH ->
                {
                    worldCenter.right -= UNIT_STEP;
                }
                case WEST ->
                {
                    worldCenter.left += UNIT_STEP;
                }
                case NORTH ->
                {
                    worldCenter.right += UNIT_STEP;
                }
                case SOUTHEAST ->
                {
                    worldCenter.left -= UNIT_STEP * COS_30DEG;
                    worldCenter.right -= UNIT_STEP * SIN_30DEG;
                }
                case SOUTHWEST ->
                {
                    worldCenter.left += UNIT_STEP * COS_30DEG;
                    worldCenter.right -= UNIT_STEP * SIN_30DEG;
                }
                case NORTHWEST ->
                {
                    worldCenter.left += UNIT_STEP * COS_30DEG;
                    worldCenter.right += UNIT_STEP * SIN_30DEG;
                }
                case NORTHEAST ->
                {
                    worldCenter.left -= UNIT_STEP * COS_30DEG;
                    worldCenter.right += UNIT_STEP * SIN_30DEG;
                }
            }
        }
        
        Dimension size = getSize();
        panelCenter.left = (double) size.width / 2.0;
        panelCenter.right = (double) size.height / 2.0;
        
        if (inputHandler.zoomIn() && scale < MAX_SCALE)
        {
            scale = Math.min(scale * SCALE_FACTOR, MAX_SCALE);
            
            Doublet<Double> relative = new Doublet<>();
            relative.left = panelCenter.left - worldCenter.left;
            relative.right = panelCenter.right - worldCenter.right;
            
            Doublet<Double> offset = new Doublet<>();
            offset.left = relative.left * (SCALE_FACTOR - 1);
            offset.right = relative.right * (SCALE_FACTOR - 1);
            
            worldCenter.left -= offset.left;
            worldCenter.right -= offset.right;
        }
        else if (inputHandler.zoomOut() && scale > MIN_SCALE)
        {
            scale = Math.max(scale / SCALE_FACTOR, MIN_SCALE);

            Doublet<Double> relative = new Doublet<>();
            relative.left = panelCenter.left - worldCenter.left;
            relative.right = panelCenter.right - worldCenter.right;
            
            Doublet<Double> offset = new Doublet<>();
            offset.left = relative.left * (SCALE_FACTOR - 1);
            offset.right = relative.right * (SCALE_FACTOR - 1);
            
            worldCenter.left += offset.left;
            worldCenter.right += offset.right;
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

        if (worldCenter.left > xMax)
        {
            worldCenter.left = xMax;
        }
        else if (worldCenter.left < xMin)
        {
            worldCenter.left = xMin;
        }

        if (worldCenter.right > yMax)
        {
            worldCenter.right = yMax;
        }
        else if (worldCenter.right < yMin)
        {
            worldCenter.right = yMin;
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
        Point point = e.getPoint();
        double globalX = point.x;
        double globalY = point.y;
        double relativeX = globalX - worldCenter.left;
        double relativeY = globalY - worldCenter.right;
        Hex hex = Hex.getHexAt(relativeX, relativeY, World.HEX_OUTER_RADIUS * scale, World.HEX_INNER_RADIUS * scale);
        
        AbstractField field = world.getFieldAt(hex);
        invoker.invoke(new HandleFieldCommand(field));
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
