package my.field;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import my.entity.Entity;
import my.player.Player;
import my.utils.Doublet;
import my.utils.Hex;

/**
 *
 * @author Kay Jay O'Nail
 */
public abstract class AbstractField
{
    private final FieldType type;
    private Hex hex;
    
    private Player owner;
    private boolean isMarked;
    private final BufferedImage image;
    private final BufferedImage markedImage;
    
    private Entity entity;
    
    private static final FieldsManager fieldsManager = FieldsManager.getInstance();
    
    protected AbstractField(FieldType type)
    {
        this.type = type;
        image = fieldsManager.getField(type);
        markedImage = fieldsManager.getMarkedField(type);
    }
    
    public FieldType getType()
    {
        return type;
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
        graphics.drawImage(isMarked ? markedImage : image,
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
    
    public static AbstractField newInstance(FieldType type, Hex hex)
    {
        AbstractField field = switch (type)
        {
            case SEA ->
            {
                yield new SeaField();
            }
            case MOUNTAINS ->
            {
                yield new MountainsField();
            }
            case GRASS ->
            {
                yield new GrassField();
            }
            case MEADOW ->
            {
                yield new MeadowField();
            }
            case WOOD ->
            {
                yield new WoodField();
            }
            case FARMFIELD ->
            {
                yield new FarmField();
            }
            case VILLAGE ->
            {
                yield new VillageField();
            }
            case TOWN ->
            {
                yield new TownField();
            }
            case MINE ->
            {
                yield new MineField();
            }
            case BARRACKS ->
            {
                yield new BarracksField();
            }
            case SHIPYARD ->
            {
                yield new ShipyardField();
            }
            case FORTRESS ->
            {
                yield new FortressField();
            }
            case CAPITAL ->
            {
                yield new CapitalField();
            }
            
        };
        field.hex = hex;
        return field;
    }
}
