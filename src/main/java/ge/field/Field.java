package ge.field;

import ge.entity.Entity;
import ge.utilities.*;
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
        return name.substring(0, name.lastIndexOf("Field"));
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
}
