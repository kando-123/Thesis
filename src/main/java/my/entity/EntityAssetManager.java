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
public class EntityAssetManager
{
    private Map<EntityType, BufferedImage> images;
    private Map<EntityType, Icon> icons;

    private EntityAssetManager()
    {
        images = new HashMap<>(EntityType.values().length);
        icons = new HashMap<>(EntityType.values().length);
        for (var value : EntityType.values())
        {
            InputStream stream = getClass().getResourceAsStream(value.getFile());
            InputStream iStream = getClass().getResourceAsStream(value.getIconFile());
            try
            {
                BufferedImage entity = ImageIO.read(stream);
                images.put(value, entity);

                BufferedImage iEntity = ImageIO.read(iStream);
                icons.put(value, new ImageIcon(iEntity));
            }
            catch (IOException io)
            {
                System.err.println(io.getMessage());
            }
        }
    }
    
    private static final EntityAssetManager instance = new EntityAssetManager();
    
    public static EntityAssetManager getInstance()
    {
        return instance;
    }

    public BufferedImage getImage(EntityType type)
    {
        return images.get(type);
    }

    public Icon getIcon(EntityType type)
    {
        return icons.get(type);
    }
}