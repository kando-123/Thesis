package ge.field;

import ge.entity.Entity;
import ge.player.Player;
import ge.utilities.*;
import java.awt.Graphics2D;
import java.awt.image.*;

/**
 *
 * @author Kay Jay O'Nail
 */
public abstract class Field
{
    protected final Hex coords;
    
    private boolean marked;
    private final BufferedImage image;
    private final BufferedImage brightImage;
    private static final FieldAssetManager assetManager = FieldAssetManager.getInstance();
    
    private Player owner;
    
    private final Entity[] entities;
    private static final int PRIMARY = 0;
    private static final int SECONDARY = 1;
    
    protected Field(Hex coords)
    {
        this.coords = coords;
        
        var name = getName();
        image = assetManager.getImage(name);
        brightImage = assetManager.getBrightImage(name);
        
        entities = new Entity[2];
    }
    
    private String getName()
    {
        String name = getClass().getName();
        return name.substring(name.lastIndexOf('.') + 1, name.lastIndexOf("Field"));
    }
    
    public Hex getHex()
    {
        return coords;
    }
    
    public boolean hasEntity()
    {
        return entities[PRIMARY] != null || entities[SECONDARY] != null;
    }
    
    public void setMarked(boolean m)
    {
        marked = m;
    }
    
    public void draw(Graphics2D graphics, int xPosition, int yPosition, int width, int height)
    {
        graphics.drawImage(!marked ? image : brightImage, xPosition, yPosition, width, height, null);
    }
    
    public void setOwner(Player newOwner)
    {
        owner = newOwner;
    }
    
    public Player getOwner()
    {
        return owner;
    }
    
    public boolean isOwned()
    {
        return owner != null;
    }
    
    public boolean isOwned(Player player)
    {
        return owner == player;
    }
}
