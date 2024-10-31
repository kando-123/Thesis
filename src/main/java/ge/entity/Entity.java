package ge.entity;

import ge.player.Player;
import java.awt.Graphics2D;
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
    
    public static final int MINIMAL_NUMBER = 1;
    public static final int MAXIMAL_NUMBER = 100;
    
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
    
    public static Entity newInstance(EntityType type, Player owner)
    {
        return switch (type)
        {
            case CAVALRY ->
            {
                yield new CavalryEntity(owner);
            }
            case INFANTRY ->
            {
                yield new InfantryEntity(owner);
            }
            case NAVY ->
            {
                yield new NavyEntity(owner);
            }
        };
    }
    
    public void draw(Graphics2D graphics, int xPosition, int yPosition, int width, int height)
    {
        graphics.drawImage(!marked ? image : brightImage, xPosition, yPosition, width, height, null);
    }
}
