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

    private static final AssetManager assetManager = new AssetManager();
    
    protected AbstractField(FieldType type)
    {
        this.type = type;
        images = new Doublet<>();
        images.left = assetManager.getImage(type);
        images.right = assetManager.getBrightImage(type);
    }
    
    public AbstractField copy()
    {
        return newInstance(type);
    }

    public void copyProperties(AbstractField other)
    {
        hex = other.hex;
        owner = other.owner;
        entity = other.entity;
        other.entity = null;
    }
    
    public String getName()
    {
        return type.toString();
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

    final public boolean isNatural()
    {
        return this instanceof NaturalField;
    }

    final public boolean isMarine()
    {
        return this instanceof SeaField;
    }

    final public boolean isContinental()
    {
        return this instanceof ContinentalField;
    }

    final public boolean isMountainous()
    {
        return this instanceof MountainsField;
    }

    final public boolean isPlains()
    {
        return this instanceof PlainsField;
    }

    final public boolean isProperty()
    {
        return this instanceof PropertyField;
    }

    final public boolean isCapital()
    {
        return this instanceof CapitalField;
    }

    final public boolean isBuilding()
    {
        return this instanceof BuildingField;
    }

    final public boolean isCommercial()
    {
        return this instanceof CommercialField;
    }

    final public boolean isSpawner()
    {
        return this instanceof Spawner;
    }
    
    final public boolean isDefense()
    {
        return this instanceof Defense;
    }

    public static AbstractField newInstance(FieldType type)
    {
        return switch (type)
        {
            case BARRACKS ->
            {
                yield new BarracksField();
            }
            case CAPITAL ->
            {
                yield new CapitalField();
            }
            case FARMFIELD ->
            {
                yield new FarmField();
            }
            case FORTRESS ->
            {
                yield new FortressField();
            }
            case GRASS ->
            {
                yield new GrassField();
            }
            case MEADOW ->
            {
                yield new MeadowField();
            }
            case MINE ->
            {
                yield new MineField();
            }
            case MOUNTAINS ->
            {
                yield new MountainsField();
            }
            case SEA ->
            {
                yield new SeaField();
            }
            case SHIPYARD ->
            {
                yield new ShipyardField();
            }
            case TOWN ->
            {
                yield new TownField();
            }
            case VILLAGE ->
            {
                yield new VillageField();
            }
            case WOOD ->
            {
                yield new WoodField();
            }
        };
    }
}
