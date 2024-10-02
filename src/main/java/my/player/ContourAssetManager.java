package my.player;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;

/**
 *
 * @author Kay Jay O'Nail
 */
public class ContourAssetManager
{
    private Map<PlayerColor, BufferedImage> contours;
    
    private ContourAssetManager()
    {
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
    
    private static final ContourAssetManager instance = new ContourAssetManager();

    public static ContourAssetManager getInstance()
    {
        return instance;
    }
    
    public BufferedImage getContour(PlayerColor color)
    {
        return contours.get(color);
    }
}