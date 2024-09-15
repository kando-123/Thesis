package my.entity;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.Icon;
import my.field.AbstractField;
import my.field.Spawner;
import my.player.UnaryPredicate;
import my.utils.Doublet;
import my.utils.Hex;
import my.world.WorldAccessor;

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
    
    public AbstractEntity copy()
    {
        return newInstance(type);
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

    public abstract String getDescription();
    public abstract String getCondition();
    public abstract String getPricing();

    private static final EntityAssetManager assetManager = EntityAssetManager.getInstance();
    
    public UnaryPredicate<Hex> getPredicate(WorldAccessor accessor)
    {
        return (Hex hex) ->
        {
            var place = accessor.getFieldAt(hex);
            if (place != null && place.isSpawner())
            {
                Spawner spawner = (Spawner) place;
                return spawner.canSpawn(this);
            }
            else
            {
                return false;
            }
        };
    }
    
    public static final int DEFAULT_NUMBER =  25;
    public static final int MINIMAL_NUMBER =   1;
    public static final int MAXIMAL_NUMBER = 100;
    public static final int MINIMAL_MORALE =   1;
    public static final int MAXIMAL_MORALE = 100;
    
    private int number = DEFAULT_NUMBER;
    
    public void setNumber(int newNumber)
    {
        if (newNumber > MAXIMAL_NUMBER)
        {
            //throw null;
        }
        else if (newNumber < MINIMAL_NUMBER)
        {
            //throw null;
        }
        else
        {
            number = newNumber;
        }
    }
    
    public int getNumber()
    {
        return number;
    }
    
    public int computePrice()
    {
        return priceSlope * number + priceIntercept;
    }
    
    private int morale = 0;
    
    public void setMorale(int newMorale)
    {
        if (newMorale > MAXIMAL_MORALE)
        {
            //throw null;
        }
        else if (newMorale < MINIMAL_MORALE)
        {
            //throw null;
        }
        else
        {
            morale = newMorale;
        }
    }
    
    public int getMorale()
    {
        return morale;
    }
}
