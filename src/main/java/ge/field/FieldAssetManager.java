package ge.field;

import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;
import javax.imageio.*;
import javax.swing.*;

/**
 *
 * @author Kay Jay O'Nail
 */
public class FieldAssetManager
{
    private final Map<String, BufferedImage> images;
    private final Map<String, BufferedImage> brightImages;
    private final Map<String, Icon> icons;

    private FieldAssetManager()
    {
        images = new HashMap<>();
        brightImages = new HashMap();
        icons = new HashMap<>();
    }

    private static final FieldAssetManager instance = new FieldAssetManager();

    public static FieldAssetManager getInstance()
    {
        return instance;
    }

    BufferedImage getImage(String name)
    {
        var image = images.get(name);

        if (image == null)
        {
            String file = String.format("/Field/%s.png", name);
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

    private static final float RESCALING_FACTOR = 1.33f;
    private static final float RESCALING_OFFSET = 0.00f;
    private static final RescaleOp rescaler = new RescaleOp(RESCALING_FACTOR, RESCALING_OFFSET, null);

    private static BufferedImage brighten(BufferedImage input)
    {
        return rescaler.filter(input, null);
    }

    BufferedImage getBrightImage(String name)
    {
        var brightImage = brightImages.get(name);

        if (brightImage == null)
        {
            var image = getImage(name);
            brightImage = brighten(image);

            brightImages.put(name, brightImage);
        }

        return brightImage;
    }

    private static final int ICON_WIDTH = 80;
    private static final int ICON_HEIGHT = 69;

    private static BufferedImage resize(BufferedImage input)
    {
        var output = new BufferedImage(ICON_WIDTH, ICON_HEIGHT, BufferedImage.TYPE_INT_ARGB);

        var graphics = (Graphics2D) output.getGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics.drawImage(input, 0, 0, ICON_WIDTH, ICON_HEIGHT, null);
        graphics.dispose();

        return output;
    }

    public Icon getIcon(String name)
    {
        var icon = icons.get(name);

        if (icon == null)
        {
            var image = getImage(name);
            var resized = resize(image);
            icon = new ImageIcon(resized);

            icons.put(name, icon);
        }

        return icon;
    }
    
    public static class ResourceNotFoundException extends RuntimeException
    {
        /* ... */
    }
}
