package my.field;

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
public class FieldsManager
{
    private Map<FieldType, Doublet<BufferedImage>> fields;
    private Map<FieldType, Icon> iFields;
    
    private FieldsManager()
    {
        fields = new HashMap<>(FieldType.values().length);
        iFields = new HashMap<>(FieldType.values().length);
        for (var type : FieldType.values())
        {
            InputStream stream = getClass().getResourceAsStream(type.path);
            InputStream iStream = getClass().getResourceAsStream(type.iPath);
            try
            {
                BufferedImage field = ImageIO.read(stream);
                BufferedImage brightField = brightenImage(field);
                fields.put(type, new Doublet<>(field, brightField));
                
                BufferedImage iField = ImageIO.read(iStream);
                iFields.put(type, new ImageIcon(iField));
            }
            catch (IOException io)
            {
                System.err.println(io.getMessage());
            }
        }
        
    }
    
    private static final FieldsManager instance = new FieldsManager();

    public static FieldsManager getInstance()
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
    
    public BufferedImage getField(FieldType type)
    {
        return fields.get(type).left;
    }

    public BufferedImage getMarkedField(FieldType type)
    {
        return fields.get(type).right;
    }

    public Icon getFieldAsIcon(FieldType type)
    {
        return iFields.get(type);
    }
}
