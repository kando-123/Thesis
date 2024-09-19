 package my.field;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.Icon;
import my.entity.AbstractEntity;
import my.player.Player;
import my.utils.Doublet;
import my.utils.Hex;

/**
 *
 * @author Kay Jay O'Nail
 */
public abstract class AbstractField
{
    /* Properties */
    
    private final FieldType type;
    protected Hex hex;

    private Player owner;
    private boolean isMarked;
    private final BufferedImage image;
    private final BufferedImage brightImage;

    private AbstractEntity entity;
    
    /* Static Properties */

    private static final FieldAssetManager assetManager = FieldAssetManager.getInstance();
    
    /* Creation */
    
    protected AbstractField(FieldType type)
    {
        this.type = type;
        image = assetManager.getImage(type);
        brightImage = assetManager.getBrightImage(type);
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
    
    /* Accessors & Mutators */

    public FieldType getType()
    {
        return type;
    }
    
    public String getName()
    {
        return type.toString();
    }
    
    /* Accessors & Mutators: Interaction with Player */

    public void setOwner(Player newOwner)
    {
        if (owner != null)
        {
            owner.release(this);
        }
        owner = newOwner;
    }

    public Player getOwner()
    {
        return owner;
    }

    public boolean isOwned()
    {
        return (owner != null);
    }
    
    /* Accessors & Mutators: Interactions with Entity */
    
    public void setEntity(AbstractEntity newEntity)
    {
        entity = newEntity;
    }
    
    public AbstractEntity getEntity()
    {
        return entity;
    }

    public boolean hasEntity()
    {
        return entity != null;
    }
    
    /* Accessors & Mutators: Graphics & Geometry */
    
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
        return image.getWidth();
    }

    public int getHeight()
    {
        return image.getHeight();
    }
    
    public void setMarked(boolean marked)
    {
        isMarked = marked;
    }

    public void mark()
    {
        isMarked = true;
    }

    public void unmark()
    {
        isMarked = false;
    }
    
    public Icon getIcon()
    {
        return assetManager.getIcon(type);
    }
    
    /* Class Hierarchy */
    
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
    
    /* Graphics */

    public void draw(Graphics2D graphics, Doublet<Integer> position, Dimension size)
    {
        graphics.drawImage(!isMarked ? image : brightImage,
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
}
