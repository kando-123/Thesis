package my.entity;

import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import my.utils.Doublet;

/**
 *
 * @author Kay Jay O'Nail
 */
public class EntityAssetManager
{
    private Map<EntityType, Doublet<BufferedImage>> images;
    private Map<EntityType, Icon> icons;

    private EntityAssetManager()
    {
        images = new HashMap<>(EntityType.values().length);
        icons = new HashMap<>(EntityType.values().length);
        for (var value : EntityType.values())
        {
            InputStream imageStream = getClass().getResourceAsStream(value.getFile());
            InputStream iconStream = getClass().getResourceAsStream(value.getIconFile());
            try
            {
                BufferedImage entity = ImageIO.read(imageStream);
                BufferedImage brightEntity = brightenImage(entity);
                images.put(value, new Doublet<>(entity, brightEntity));

                BufferedImage icon = ImageIO.read(iconStream);
                icons.put(value, new ImageIcon(icon));
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
    
    private final static float RESCALING_FACTOR = 1.33f;
    private final static float RESCALING_OFFSET = 0.0f;
    private static RescaleOp rescaler = null;

    private BufferedImage brightenImage(BufferedImage input)
    {
        if (rescaler == null)
        {
            rescaler = new RescaleOp(RESCALING_FACTOR, RESCALING_OFFSET, null);
        }
        return rescaler.filter(input, null);
    }

    public BufferedImage getImage(EntityType type)
    {
        return images.get(type).left;
    }
    
    public BufferedImage getBrightImage(EntityType type)
    {
        return images.get(type).right;
    }

    public Icon getIcon(EntityType type)
    {
        return icons.get(type);
    }
}