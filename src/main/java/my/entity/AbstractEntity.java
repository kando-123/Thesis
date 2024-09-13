package my.entity;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import my.field.AbstractField;
import my.utils.Doublet;

class AssetManager
{
    private Map<EntityType, BufferedImage> images;
    private Map<EntityType, Icon> icons;

    public AssetManager()
    {
        images = new HashMap<>(EntityType.values().length);
        icons = new HashMap<>(EntityType.values().length);
        for (var value : EntityType.values())
        {
            InputStream stream = getClass().getResourceAsStream(value.getFile());
            InputStream iStream = getClass().getResourceAsStream(value.getIconFile());
            try
            {
                BufferedImage entity = ImageIO.read(stream);
                images.put(value, entity);

                BufferedImage iEntity = ImageIO.read(iStream);
                icons.put(value, new ImageIcon(iEntity));
            }
            catch (IOException io)
            {
                System.err.println(io.getMessage());
            }
        }
    }

    public BufferedImage getImage(EntityType type)
    {
        return images.get(type);
    }

    public Icon getIcon(EntityType type)
    {
        return icons.get(type);
    }
}

/**
 *
 * @author Kay Jay O'Nail
 */
public abstract class AbstractEntity
{
    private final EntityType type;
    private final BufferedImage image;

    private AbstractField field;

    protected AbstractEntity(EntityType type)
    {
        this.type = type;
        this.image = assetManager.getImage(type);
    }

    public BufferedImage getImage()
    {
        return image;
    }

    public Icon getIcon()
    {
        return assetManager.getIcon(type);
    }

    public static AbstractEntity newInstance(EntityType type)
    {
        return switch (type)
        {
            case INFANTRY ->
            {
                yield new InfantryEntity();
            }
            case CAVALRY ->
            {
                yield new CavalryEntity();
            }
            case NAVY ->
            {
                yield new NavyEntity();
            }
        };
    }
    
    public String getName()
    {
        return type.toString();
    }

    public EntityType getType()
    {
        return type;
    }

    public AbstractField setField(AbstractField newField)
    {
        AbstractField oldField = field;
        field = newField;
        return oldField;
    }

    public void draw(Graphics2D graphics, Doublet<Integer> position, Dimension size)
    {
        int x = (int) (position.left + 0.15 * size.width);
        int y = (int) (position.right + 0.30 * size.height);
        int w = (int) (0.7 * size.width);
        int h = (int) (0.7 * size.height);
        graphics.drawImage(image, x, y, w, h, null);
    }

    protected int priceIntercept;
    protected int priceSlope;

    public int computePriceFor(int number)
    {
        return priceSlope * number + priceIntercept;
    }
    
    public int getPriceIntercept()
    {
        return priceIntercept;
    }
    
    public int getPriceSlope()
    {
        return priceSlope;
    }

    public abstract String getDescription();

    public abstract String getCondition();

    private static final AssetManager assetManager = new AssetManager();
}
