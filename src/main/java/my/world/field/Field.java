package my.world.field;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import my.world.Pixel;

/**
 *
 * @author Kay Jay O'Nail
 */
public class Field
{
    private final FieldType type;
    private final BufferedImage image;
    
    public Field(FieldType type)
    {
        this.type = type;
        FieldManager fieldManager = FieldManager.getInstance();
        image = fieldManager.getImage(type);
    }
    
    public FieldType getType()
    {
        return type;
    }
    
    public int getWidth()
    {
        return image.getWidth();
    }
    
    public int getHeight()
    {
        return image.getHeight();
    }
    
    public void draw(Graphics2D graphics, Pixel position, double scale)
    {
        int x = position.xCoord;
        int y = position.yCoord;
        int width = (int) (scale * image.getWidth());
        int height = (int) (scale * image.getHeight());
        graphics.drawImage(image, x, y, width, height, null);
        
        // draw contour, if any

        // draw entity, if any
        
    }
}
