package my.field;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import my.player.Player;
import my.player.PlayerColor;
import my.entity.Entity;
import my.entity.EntityType;
import my.utils.Hex;
import my.utils.IntegersDoublet;

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
    
    private Entity entity;
    
    private static final FieldsManager fieldsManager = FieldsManager.getInstance();
    private static final ContoursManager contoursManager = ContoursManager.getInstance();
    
    public Field(FieldType type, Hex hex)
    {
        this.type = type;
        this.hex = hex;
        
        isMarked = false;
        image = fieldsManager.getField(type);
        markedImage = fieldsManager.getMarkedField(type);
        
        owner = null;
        contour = null;
        entity = null;
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
            PlayerColor color = owner.getColor();
            contour = contoursManager.getContour(color);
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
    
    public void draw(Graphics2D graphics, IntegersDoublet position, Dimension size)
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
        
        if (entity != null)
        {
            entity.draw(graphics, position, size);
        }
    }
}
