package my.units;

import java.awt.*;
import java.awt.image.*;
import my.player.*;
import my.utils.*;

/**
 *
 * @author Kay Jay O'Nail
 */
public class Field
{
    private final FieldType type;
    private final Hex hex;
    
    private boolean isMarked;
    private final BufferedImage image;
    private final BufferedImage markedImage;
    
    private Player owner;
    private BufferedImage contour;
    
    private static ImageManager imageManager = null;
    
    public Field(FieldType type, Hex hex)
    {
        this.type = type;
        this.hex = hex;
        
        isMarked = false;
        if (imageManager == null)
        {
            imageManager = ImageManager.getInstance();
        }
        image = imageManager.getField(type);
        markedImage = imageManager.getMarkedField(type);
        
        owner = null;
        contour = null;
    }
    
    public FieldType getType()
    {
        return type;
    }
    
    public Hex getHex()
    {
        return hex;
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
    
    public void setOwner(Player newOwner)
    {
        owner = newOwner;
        if (owner != null)
        {
            if (imageManager == null)
            {
                imageManager = ImageManager.getInstance();
            }
            PlayerColor color = owner.getColor();
            contour = imageManager.getContour(color);
        }
        else
        {
            contour = null;
        }
    }
    
    public Player getOwner()
    {
        return owner;
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
