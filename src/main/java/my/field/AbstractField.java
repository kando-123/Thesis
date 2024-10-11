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

    public void moveProperties(AbstractField other)
    {
        hex = other.hex;
        owner = other.owner;
        entity = other.entity;
        if (entity != null)
        {
            entity.setField(this);
        }
        
        other.hex = null;
        other.owner = null;
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
    
    public boolean isOwned(Player player)
    {
        return owner == player;
    }

    public boolean isFellow(AbstractField field)
    {
        return field.owner == owner;
    }

    public boolean isFellow(AbstractEntity entity)
    {
        return entity.getField().owner == owner;
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
    
    public AbstractEntity unpin()
    {
        entity.setField(null);
        
        var oldEntity = entity;
        entity = null;
        
        return oldEntity;
    }
    
    public AbstractEntity pin(AbstractEntity newEntity)
    {
        if (entity != null)
        {
            entity.setField(null);
        }
        var oldEntity = entity;
        
        entity = newEntity;
        if (entity != null)
        {
            entity.setField(this);
        }
        
        return oldEntity;
    }

    private AbstractEntity move(AbstractEntity comer)
    {
        var origin = comer.getField();

        entity = comer;
        comer.setField(this);

        origin.entity = null;
        owner = origin.owner;
        
        entity.setMovable(false);
        
        return entity;
    }

    private AbstractEntity merge(AbstractEntity fellow)
    {
        fellow.getField().entity = entity.merge(fellow);
        entity.setMovable(false);
        
        return entity;
    }

    private AbstractEntity militate(AbstractEntity attacker)
    {
        int offensive = attacker.getNumber() + attacker.getMorale();
        
        var fortification = isFortification() ? (Fortification) this : null;
        
        int defensive = 0;
        if (fortification != null)
        {
            defensive += fortification.getDefense();
        }
        if (entity != null)
        {
            defensive += entity.getNumber() + entity.getMorale();
        }
        assert (defensive != 0);
        
        if (offensive > defensive)
        {
            // The attacker wins, but is being damaged.
            
            int number = attacker.getNumber();
            int morale = attacker.getMorale();
            
            double numberFraction = (double) number / (double) offensive;
            double moraleFraction = (double) morale / (double) offensive;
            
            number -= (int) (numberFraction * defensive);
            morale -= (int) (moraleFraction * defensive);
            
            attacker.setNumber(number);
            attacker.setMorale(morale);
            
            // The attacker moves to this field and conquers it.
            
            var origin = attacker.getField();
            origin.entity = null;
            origin.owner.capture(this);
            attacker.setField(this);
            entity = attacker;
            
            attacker.setMovable(false);
            
            return attacker;
        }
        else
        {
            // The defender (be it an entity, a defense, or both) wins,
            // but is being damaged.
            
            int number = (entity != null) ? entity.getNumber() : 0;
            int morale = (entity != null) ? entity.getMorale() : 0;
            int defense = (fortification != null) ? fortification.getDefense() : 0;
            
            double numberFraction = (double) number / (double) defensive;
            double moraleFraction = (double) morale / (double) defensive;
            double defenseFraction = (double) defense / (double) defensive;
            
            number -= (int) (numberFraction * offensive);
            morale -= (int) (moraleFraction * offensive);
            defense -= (int) (defenseFraction * offensive);
            
            if (entity != null)
            {
                entity.setNumber(number);
                entity.setMorale(morale);
            }
            if (fortification != null)
            {
                fortification.setDefense(defense);
            }
            
            // The attacker disappears.
            
            attacker.getField().entity = null;
            
            return null;
        }
    }

    public AbstractEntity interact(AbstractEntity newEntity)
    {
        final boolean isOccupied = hasEntity();
        final boolean isFellow = isFellow(newEntity);
        final boolean isFortification = isFortification();

        if (!isOccupied && (isFellow || !isFortification))
        {
            return move(newEntity);
        }
        else if (isFellow && isOccupied)
        {
            return merge(newEntity);
        }
        else // if (!isFellow && (isOccupied || isDefense))
        {
            return militate(newEntity);
        }
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

    final public boolean isFortification()
    {
        return this instanceof Fortification;
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
