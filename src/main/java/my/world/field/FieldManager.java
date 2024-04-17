package my.world.field;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import javax.imageio.ImageIO;

/**
 *
 * @author Kay Jay O'Nail
 */
public class FieldManager
{
    private Map<FieldType, List<BufferedImage>> images;
    
    private FieldManager()
    {
        images = new HashMap<>(FieldType.values().length);
        for (var fieldType : FieldType.values())
        {
            List<BufferedImage> imagesList = new ArrayList<>(fieldType.paths.size());
            for (var path : fieldType.paths)
            {
                InputStream stream = getClass().getResourceAsStream(path);
                BufferedImage image;
                try
                {
                    image = ImageIO.read(stream);
                    imagesList.add(image);
                }
                catch (IOException e)
                {
                    /* The exception will not have been thrown in a correct program.
                       Behavior of this function depends only on internal files,
                       not on external input. */
                    System.err.println("Internal error. Some resources are lacking.");
                }                
            }
            images.put(fieldType, imagesList);
        }
    }
    
    private static FieldManager instance = null;
    
    public static FieldManager getInstance()
    {
        if (instance == null)
        {
            instance = new FieldManager();
        }
        return instance;
    }
    
    private static Random random = new Random();
    
    public BufferedImage getImage(FieldType type)
    {
        List<BufferedImage> list = images.get(type);
        return list.get(random.nextInt(0, list.size()));
    }
}
