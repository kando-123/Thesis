package ge.entity;

import ge.player.Player;
import java.awt.image.*;

/**
 *
 * @author Kay Jay O'Nail
 */
public abstract class Entity
{
    private boolean marked;
    private final BufferedImage image;
    private final BufferedImage brightImage;
    private static final EntityAssetManager ASSET_MANAGER = EntityAssetManager.getInstance();

    private final Player owner;
    
    protected Entity(Player owner)
    {
        this.owner = owner;
        
        var name = getName();
        this.image = ASSET_MANAGER.getImage(name);
        this.brightImage = ASSET_MANAGER.getBrightImage(name);
    }
    
    private String getName()
    {
        String name = getClass().getName();
        return name.substring(name.lastIndexOf('.') + 1, name.lastIndexOf("Entity"));
    }
}
