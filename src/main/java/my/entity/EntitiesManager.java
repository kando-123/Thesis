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
    private Map<EntityType, Icon> iEntities;
    
    private EntitiesManager()
    {
        entities = new HashMap<>(EntityType.values().length);
        iEntities = new HashMap<>(EntityType.values().length);
        for (var value : EntityType.values())
        {
            InputStream stream = getClass().getResourceAsStream(value.getFile());
            InputStream iStream = getClass().getResourceAsStream(value.getIconFile());
            try
            {
                BufferedImage entity = ImageIO.read(stream);
                entities.put(value, entity);
                
                BufferedImage iEntity = ImageIO.read(iStream);
                iEntities.put(value, new ImageIcon(iEntity));
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
        return iEntities.get(type);
    }
}
