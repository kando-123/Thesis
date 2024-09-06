package my.entity;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 *
 * @author Kay Jay O'Nail
 */
public class EntitiesManager
{
    private Map<EntityType, BufferedImage> entities;
    
    private EntitiesManager()
    {
        entities = new HashMap<>(EntityType.values().length);
        for (var entity : EntityType.values())
        {
            InputStream stream = getClass().getResourceAsStream(entity.getFile());
            try
            {
                BufferedImage image = ImageIO.read(stream);
                entities.put(entity, image);
            }
            catch (IOException io)
            {
                System.err.println(io.getMessage());
            }
        }
    }
    
    private static final EntitiesManager instance = new EntitiesManager();

    public static EntitiesManager getInstance()
    {
        return instance;
    }
    
    public BufferedImage getEntity(EntityType type)
    {
        return entities.get(type);
    }

    public Icon getEntityAsIcon(EntityType type)
    {
        return new ImageIcon(getEntity(type));
    }
}
