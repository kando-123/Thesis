package ge.view;

import ge.utilities.*;
import ge.world.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 *
 * @author Kay Jay O'Nail
 */
public class WorldPanel extends JPanel implements Runnable, MouseListener
{
    private final Doublet<Double> worldCenter;

    private static final double UNIT_STEP = 5;
    private static final double SIN_30DEG = Math.sin(Math.toRadians(30));
    private static final double COS_30DEG = Math.cos(Math.toRadians(30));

    private double scale;
    private static final double SCALE_FACTOR = 1.01;
    private static final double MAX_SCALE = 2.5;
    private static final double MIN_SCALE = 0.25;

    private final InputHandler inputHandler;

    private final WorldRenderer renderer;
    private final WorldAccessor accessor;

    private final Invoker<ViewManager> invoker;

    WorldPanel(WorldRenderer renderer, WorldAccessor accessor, Invoker<ViewManager> invoker)
    {
        this.renderer = renderer;
        this.accessor = accessor;
        this.invoker = invoker;

        worldCenter = new Doublet<>(0., 0.);
        scale = 1.0;

        inputHandler = new InputHandler();

        addMouseListener(this);
    }

    void setCenter(Hex hex)
    {
        double scaledOuterRadius = scale * World.HEX_OUTER_RADIUS;
        double scaledInnerRadius = scale * World.HEX_INNER_RADIUS;
        var center = hex.centralPoint(scaledOuterRadius, scaledInnerRadius);
        var size = getSize();
        worldCenter.x = size.width / 2 - center.x;
        worldCenter.y = size.height / 2 - center.y;
    }

    InputHandler getInputHandler()
    {
        return inputHandler;
    }

    @Override
    public void paintComponent(Graphics graphics)
    {
        super.paintComponent(graphics);

        var graphics2D = (Graphics2D) graphics;
        graphics2D.setBackground(Color.black);

        var size = getSize();
        graphics2D.clearRect(0, 0, size.width, size.height);

        renderer.draw(graphics2D, worldCenter, scale, size);
    }

    void update()
    {
        var direction = inputHandler.getShiftingDirection();
        if (direction != null)
        {
            switch (direction)
            {
                case EAST ->
                {
                    worldCenter.x -= UNIT_STEP;
                }
                case SOUTH ->
                {
                    worldCenter.y -= UNIT_STEP;
                }
                case WEST ->
                {
                    worldCenter.x += UNIT_STEP;
                }
                case NORTH ->
                {
                    worldCenter.y += UNIT_STEP;
                }
                case SOUTHEAST ->
                {
                    worldCenter.x -= UNIT_STEP * COS_30DEG;
                    worldCenter.y -= UNIT_STEP * SIN_30DEG;
                }
                case SOUTHWEST ->
                {
                    worldCenter.x += UNIT_STEP * COS_30DEG;
                    worldCenter.y -= UNIT_STEP * SIN_30DEG;
                }
                case NORTHWEST ->
                {
                    worldCenter.x += UNIT_STEP * COS_30DEG;
                    worldCenter.y += UNIT_STEP * SIN_30DEG;
                }
                case NORTHEAST ->
                {
                    worldCenter.x -= UNIT_STEP * COS_30DEG;
                    worldCenter.y += UNIT_STEP * SIN_30DEG;
                }
            }
        }

        if (inputHandler.zoomIn() && scale < MAX_SCALE)
        {
            scale = Math.min(scale * SCALE_FACTOR, MAX_SCALE);

            var relative = new Doublet<Double>();
            var size = getSize();
            relative.x = size.width / 2 - worldCenter.x;
            relative.y = size.height / 2 - worldCenter.y;

            var offset = new Doublet<Double>();
            offset.x = relative.x * (SCALE_FACTOR - 1);
            offset.y = relative.y * (SCALE_FACTOR - 1);

            worldCenter.x -= offset.x;
            worldCenter.y -= offset.y;
        }
        else if (inputHandler.zoomOut() && scale > MIN_SCALE)
        {
            scale = Math.max(scale / SCALE_FACTOR, MIN_SCALE);

            var relative = new Doublet<Double>();
            var size = getSize();
            relative.x = size.width / 2 - worldCenter.x;
            relative.y = size.height / 2 - worldCenter.y;

            var offset = new Doublet<Double>();
            offset.x = relative.x * (SCALE_FACTOR - 1);
            offset.y = relative.y * (SCALE_FACTOR - 1);

            worldCenter.x += offset.x;
            worldCenter.y += offset.y;
        }

        int side = renderer.getSide();

        double worldWidth = Hex.surfaceWidth(side, scale * World.HEX_OUTER_RADIUS);
        double worldHeight = Hex.surfaceHeight(side, scale * World.HEX_INNER_RADIUS);

        var size = getSize();
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

        if (worldCenter.x > xMax)
        {
            worldCenter.x = xMax;
        }
        else if (worldCenter.x < xMin)
        {
            worldCenter.x = xMin;
        }

        if (worldCenter.y > yMax)
        {
            worldCenter.y = yMax;
        }
        else if (worldCenter.y < yMin)
        {
            worldCenter.y = yMin;
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
        double relativeX = globalX - worldCenter.x;
        double relativeY = globalY - worldCenter.y;
        Hex hex = Hex.at(relativeX, relativeY, World.HEX_OUTER_RADIUS * scale, World.HEX_INNER_RADIUS * scale);

        switch (e.getButton())
        {
            case MouseEvent.BUTTON1 ->
            {
                invoker.invoke(new HandleClickCommand(accessor.getField(hex)));
            }
            case MouseEvent.BUTTON3 ->
            {
                invoker.invoke(new HandleShiftClickCommand(accessor.getField(hex)));
            }
        }
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
