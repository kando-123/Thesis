package my.units;

import java.awt.image.*;
import java.io.*;
import java.util.*;
import javax.imageio.*;
import my.player.*;

/**
 *
 * @author Kay Jay O'Nail
 */
public class ContoursManager
{
    private Map<PlayerColor, BufferedImage> contours;
    
    private ContoursManager()
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
    
    private static final ContoursManager instance = new ContoursManager();

    public static ContoursManager getInstance()
    {
        return instance;
    }
    
    public BufferedImage getContour(PlayerColor color)
    {
        return contours.get(color);
    }
    
}
