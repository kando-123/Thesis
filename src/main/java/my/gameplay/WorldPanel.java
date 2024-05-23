package my.gameplay;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JPanel;
import my.input.*;
import my.world.OrthogonalDirection;
import my.world.Pixel;
import my.world.World;
import my.world.WorldParameters;
import my.world.field.*;

/**
 *
 * @author Kay Jay O'Nail
 */
public class WorldPanel extends JPanel
{
    private int panelWidth;
    private int panelHeight;
    private Pixel centerOffset;
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
        FieldManager.getInstance();
    }

    public void makeWorld(WorldParameters parameters)
    {
        world = new World(parameters);
    }

    public void update()
    {
        OrthogonalDirection shift = inputHandler.getShiftingDirection();
        if (shift != null)
        {
            centerOffset.add(shift.getOffset());
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
    public void setSize(Dimension newSize)
    {
        super.setSize(newSize);
        
        panelWidth = newSize.width;
        panelHeight = newSize.height;
        centerOffset = new Pixel(panelWidth / 2, panelHeight / 2);
    }

//    @Override
//    public void paintComponent(Graphics graphics)
//    {
//        Graphics2D graphics2D = (Graphics2D) graphics;
//        graphics2D.setBackground(Color.black);
//        graphics2D.clearRect(0, 0, panelWidth, panelHeight);
//
//        var iterator = fields.entrySet().iterator();
//        while (iterator.hasNext())
//        {
//            Map.Entry<Hex, Field> entry = iterator.next();
//
//            Hex hex = entry.getKey();
//            Pixel pixel = hex.getCornerPixel(hexOuterRadius, hexInnerRadius);
//
//            Field field = entry.getValue();
//            BufferedImage image = field.getImage();
//
//            int x = centerOffset.xCoord + (int) ((double) pixel.xCoord * scale);
//            int y = centerOffset.yCoord + (int) ((double) pixel.yCoord * scale);
//
//            int w = (int) ((double) hexWidth * scale);
//            int h = (int) ((double) hexHeight * scale);
//
//            if (x + w >= 0 && x < panelWidth && y + h >= 0 && y < panelHeight)
//            {
//                graphics2D.drawImage(image, x, y, w, h, null);
//            }
//        }
//    }
}
