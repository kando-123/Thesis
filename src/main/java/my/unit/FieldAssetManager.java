package my.unit;

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
public class FieldAssetManager
{
    private Map<FieldType, Doublet<BufferedImage>> images;
    private Map<FieldType, Icon> icons;

    private FieldAssetManager()
    {
        images = new HashMap<>(FieldType.values().length);
        icons = new HashMap<>(FieldType.values().length);
        for (var value : FieldType.values())
        {
            InputStream stream = getClass().getResourceAsStream(value.getFile());
            InputStream iStream = getClass().getResourceAsStream(value.getIconFile());
            try
            {
                BufferedImage field = ImageIO.read(stream);
                BufferedImage brightField = brightenImage(field);
                images.put(value, new Doublet<>(field, brightField));

                BufferedImage icon = ImageIO.read(iStream);
                icons.put(value, new ImageIcon(icon));
            }
            catch (IOException io)
            {
                System.err.println(io.getMessage());
            }
        }
    }
    
    private static final FieldAssetManager instance = new FieldAssetManager();
    
    public static FieldAssetManager getInstance()
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

    public BufferedImage getImage(FieldType type)
    {
        return images.get(type).left;
    }

    public BufferedImage getBrightImage(FieldType type)
    {
        return images.get(type).right;
    }

    public Icon getIcon(FieldType type)
    {
        return icons.get(type);
    }
}