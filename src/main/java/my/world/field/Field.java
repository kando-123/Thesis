package my.world.field;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import my.player.AbstractPlayer;
import my.player.PlayerColor;
import my.world.Pixel;

/**
 *
 * @author Kay Jay O'Nail
 */
public class Field
{
    private final FieldType type;
    private final BufferedImage image;
    private BufferedImage contour;
    private AbstractPlayer owner;
    
    public Field(FieldType type)
    {
        this.type = type;
        
        FieldManager fieldManager = FieldManager.getInstance();
        image = fieldManager.getImage(type);
        
        contour = null;
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
    
    public boolean isOwned()
    {
        return (owner == null);
    }
    
    public void capture(AbstractPlayer newOwner)
    {
        owner = newOwner;
        if (newOwner != null)
        {
            FieldManager fieldManager = FieldManager.getInstance();
            PlayerColor color = newOwner.getColor();
            contour = fieldManager.getContour(color);
        }
        else
        {
            contour = null;
        }
    }
    
    public void draw(Graphics2D graphics, Pixel position, Dimension size)
    {
        graphics.drawImage(image,
                position.xCoord, position.yCoord,
                size.width, size.height,
                null);
        
        if (contour != null)
        {
            graphics.drawImage(contour,
                position.xCoord, position.yCoord,
                size.width, size.height,
                null);
        }
        

        // draw entity, if any
        
    }
}
