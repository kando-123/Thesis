package my.world.field;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import my.player.PlayerColor;

/**
 *
 * @author Kay Jay O'Nail
 */
public class FieldManager
{
    private Map<FieldType, List<BufferedImage>> images;
    private Map<PlayerColor, BufferedImage> contours;
    
    private FieldManager()
    {
        images = new HashMap<>(FieldType.values().length);
        for (var type : FieldType.values())
        {
            List<BufferedImage> imagesList = new ArrayList<>(type.paths.size());
            for (var path : type.paths)
            {
                InputStream stream = getClass().getResourceAsStream(path);
                try
                {
                    BufferedImage image = ImageIO.read(stream);
                    imagesList.add(image);
                }
                catch (IOException io)
                {
                    System.err.println(io.getMessage());
                }                
            }
            images.put(type, imagesList);
        }
        
        contours = new HashMap<>(PlayerColor.values().length);
        for (var color : PlayerColor.values())
        {
            if (color == PlayerColor.RANDOM)
            {
                continue;
            }
            
            String path = String.format("/Contours/%s.png", color.toString());
            InputStream stream = getClass().getResourceAsStream(path);
            try
            {
                BufferedImage image = ImageIO.read(stream);
                contours.put(color, image);
            }
            catch (IOException io)
            {
                System.err.println(io.getMessage());
            }
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
    
    private static final Random random = new Random();
    
    public BufferedImage getImage(FieldType type)
    {
        List<BufferedImage> list = images.get(type);
        return list.get(random.nextInt(0, list.size()));
    }
    
    public BufferedImage getContour(PlayerColor color)
    {
        return contours.get(color);
    }
    
    public Icon getIcon(FieldType type)
    {
        return new ImageIcon(getImage(type));
    }
}
