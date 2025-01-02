package ge.player;

import java.awt.image.*;
import java.io.*;
import java.util.*;
import javax.imageio.*;

/**
 *
 * @author Kay Jay O'Nail
 */
public class ContourAssetManager
{
    private final Map<String, BufferedImage> images;

    private ContourAssetManager()
    {
        images = new HashMap<>();
    }

    private static final ContourAssetManager instance = new ContourAssetManager();

    static ContourAssetManager getInstance()
    {
        return instance;
    }

    BufferedImage getImage(String name)
    {
        var image = images.get(name);

        if (image == null)
        {
            String file = String.format("/Contour/%s.png", name);
            InputStream stream = getClass().getResourceAsStream(file);
            try
            {
                image = ImageIO.read(stream);

                images.put(name, image);
            }
            catch (IOException io)
            {
                throw new ResourceNotFoundException();
            }
        }

        return image;
    }

    public static class ResourceNotFoundException extends RuntimeException
    {
        /* ... */
    }
}
