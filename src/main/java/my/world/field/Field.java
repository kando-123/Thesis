package my.world.field;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import my.player.AbstractPlayer;
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
    
    private AbstractPlayer owner;
    
    public boolean isOwned()
    {
        return (owner == null);
    }
    
    public void draw(Graphics2D graphics, Pixel position, Dimension size)
    {
        graphics.drawImage(image,
                position.xCoord, position.yCoord,
                size.width, size.height,
                null);
        
        // draw contour, if any
        

        // draw entity, if any
        
    }
}
