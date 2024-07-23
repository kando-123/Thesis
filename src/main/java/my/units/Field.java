package my.units;

import my.utils.Pixel;
import java.awt.*;
import java.awt.image.*;
import my.player.*;

/**
 *
 * @author Kay Jay O'Nail
 */
public class Field
{
    private final FieldType type;
    
    private boolean isMarked;
    private final BufferedImage image;
    private final BufferedImage markedImage;
    
    private BufferedImage contour;
    
    private static ImageManager imageManager = null;
    
    public Field(FieldType type)
    {
        this.type = type;
        
        isMarked = false;
        if (imageManager == null)
        {
            imageManager = ImageManager.getInstance();
        }
        image = imageManager.getField(type);
        markedImage = imageManager.getMarkedField(type);
        
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
        return (contour != null);
    }
    
    public void setOwnership(Player newOwner)
    {
        if (newOwner != null)
        {
            if (imageManager == null)
            {
                imageManager = ImageManager.getInstance();
            }
            PlayerColor color = newOwner.getColor();
            contour = imageManager.getContour(color);
        }
        else
        {
            contour = null;
        }
    }
    
    public void mark()
    {
        isMarked = true;
    }
    
    public void unmark()
    {
        isMarked = false;
    }
    
    public void draw(Graphics2D graphics, Pixel position, Dimension size)
    {
        graphics.drawImage(isMarked ? markedImage : image,
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
