package my.units;

import java.awt.image.*;
import java.io.*;
import java.util.*;
import javax.imageio.*;
import javax.swing.*;

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
            InputStream stream = getClass().getResourceAsStream(entity.file);
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
