package my.field;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import my.entity.AbstractEntity;
import my.player.Player;
import my.utils.Doublet;
import my.utils.Hex;

class AssetManager
{
    private Map<FieldType, Doublet<BufferedImage>> fields;
    private Map<FieldType, Icon> icons;

    public AssetManager()
    {
        fields = new HashMap<>(FieldType.values().length);
        icons = new HashMap<>(FieldType.values().length);
        for (var value : FieldType.values())
        {
            InputStream stream = getClass().getResourceAsStream(value.getFile());
            InputStream iStream = getClass().getResourceAsStream(value.getIconFile());
            try
            {
                BufferedImage field = ImageIO.read(stream);
                BufferedImage brightField = brightenImage(field);
                fields.put(value, new Doublet<>(field, brightField));

                BufferedImage iField = ImageIO.read(iStream);
                icons.put(value, new ImageIcon(iField));
            }
            catch (IOException io)
            {
                System.err.println(io.getMessage());
            }
        }
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
        return fields.get(type).left;
    }

    public BufferedImage getBrightImage(FieldType type)
    {
        return fields.get(type).right;
    }

    public Icon getIcon(FieldType type)
    {
        return icons.get(type);
    }
}

/**
 *
 * @author Kay Jay O'Nail
 */
public abstract class AbstractField
{
    private final FieldType type;
    protected Hex hex;

    private Player owner;
    private boolean isMarked;
    private final Doublet<BufferedImage> images;

    private AbstractEntity entity;

    protected AbstractField(FieldType type)
    {
        this.type = type;
        images = new Doublet<>();
        images.left = assetManager.getImage(type);
        images.right = assetManager.getBrightImage(type);
    }

    public void cloneProperties(AbstractField other)
    {
        hex = other.hex;
        owner = other.owner;
        entity = other.entity;
        // other.entity = null; // ?
    }

    public FieldType getType()
    {
        return type;
    }

    public Hex setHex(Hex newHex)
    {
        Hex oldHex = hex;
        hex = newHex;
        return oldHex;
    }

    public Hex getHex()
    {
        return hex;
    }

    public int getWidth()
    {
        return images.left.getWidth();
    }

    public int getHeight()
    {
        return images.left.getHeight();
    }

    public boolean isOwned()
    {
        return (owner != null);
    }

    public void setOwner(Player newOwner)
    {
        owner = newOwner;
    }

    public Player getOwner()
    {
        return owner;
    }

    public boolean isFree()
    {
        return entity == null;
    }

    public void mark()
    {
        isMarked = true;
    }

    public void unmark()
    {
        isMarked = false;
    }

    public void draw(Graphics2D graphics, Doublet<Integer> position, Dimension size)
    {
        graphics.drawImage(!isMarked ? images.left : images.right,
                position.left, position.right,
                size.width, size.height,
                null);

        if (owner != null)
        {
            graphics.drawImage(owner.getContour(),
                    position.left, position.right,
                    size.width, size.height,
                    null);
        }

        if (entity != null)
        {
            entity.draw(graphics, position, size);
        }
    }
    
    public Icon getIcon()
    {
        return assetManager.getIcon(type);
    }

    public boolean isNatural()
    {
        return false;
    }

    public boolean isMarine()
    {
        return false;
    }

    public boolean isContinental()
    {
        return false;
    }

    public boolean isMountainous()
    {
        return false;
    }

    public boolean isPlains()
    {
        return false;
    }

    public boolean isProperty()
    {
        return false;
    }

    public boolean isCapital()
    {
        return false;
    }

    public boolean isBuilding()
    {
        return false;
    }

    public boolean isCommercial()
    {
        return false;
    }

    public boolean isSpawner()
    {
        return false;
    }
    
    public boolean isDefense()
    {
        return false;
    }

    private static final AssetManager assetManager = new AssetManager();
}
