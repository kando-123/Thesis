package my.units;

import java.awt.image.*;
import java.io.*;
import java.util.*;
import javax.imageio.*;
import javax.swing.*;
import my.player.*;
import my.utils.*;

/**
 *
 * @author Kay Jay O'Nail
 */
public class ImageManager
{
    private Map<FieldType, List<Doublet<BufferedImage>>> fields;
    private Map<PlayerColor, BufferedImage> contours;
    private Map<EntityType, BufferedImage> entities;

    private ImageManager()
    {
        loadFields();
        loadContours();
        loadEntities();
    }

    private void loadFields()
    {
        fields = new HashMap<>(FieldType.values().length);
        for (var type : FieldType.values())
        {
            List<Doublet<BufferedImage>> imagesList = new ArrayList<>(type.paths.size());

            for (var path : type.paths)
            {
                InputStream stream = getClass().getResourceAsStream(path);
                try
                {
                    BufferedImage image = ImageIO.read(stream);
                    BufferedImage markedImage = brightenImage(image);
                    imagesList.add(new Doublet<>(image, markedImage));
                }
                catch (IOException io)
                {
                    System.err.println(io.getMessage());
                }
            }
            fields.put(type, imagesList);
        }
    }
    
    private final static float RESCALING_FACTOR = 1.5f;
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

    private void loadContours()
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

    private void loadEntities()
    {
        entities = new HashMap<>(EntityType.values().length);
        for (var entity : EntityType.values())
        {
            InputStream stream = getClass().getResourceAsStream(entity.file);
            try
            {
                BufferedImage image = ImageIO.read(stream);
                entities.put(entity, image);
            }
            catch (IOException io)
            {
                System.err.println(io.getMessage());
            }
        }
    }

    private static ImageManager instance = null;

    public static ImageManager getInstance()
    {
        if (instance == null)
        {
            instance = new ImageManager();
        }
        return instance;
    }

    private static final Random random = new Random();

    public BufferedImage getField(FieldType type)
    {
        List<Doublet<BufferedImage>> list = fields.get(type);
        return list.size() > 1
                ? list.get(random.nextInt(0, list.size())).left
                : list.getFirst().left;
    }

    public BufferedImage getMarkedField(FieldType type)
    {
        List<Doublet<BufferedImage>> list = fields.get(type);
        return list.size() > 1
                ? list.get(random.nextInt(0, list.size())).right
                : list.getFirst().right;
    }

    public BufferedImage getContour(PlayerColor color)
    {
        return contours.get(color);
    }

    public Icon getFieldAsIcon(FieldType type)
    {
        return new ImageIcon(getField(type));
    }

    public BufferedImage getEntity(EntityType type)
    {
        return entities.get(type);
    }

    public Icon getEntityAsIcon(EntityType type)
    {
        return new ImageIcon(getEntity(type));
    }
}
