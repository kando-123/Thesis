package my.entity;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
    /* Properties */
    
    private final EntityType type;
    private final BufferedImage image;
    private final BufferedImage brightImage;
    private boolean isMarked;

    protected AbstractField field;
    
    protected int priceIntercept;
    protected int priceSlope;
    
    private int number = DEFAULT_NUMBER;
    private int morale = 0;
    
    /* Static Properties */
    
    private static final EntityAssetManager assetManager = EntityAssetManager.getInstance();
    
    /* Constants */
    
    public static final int DEFAULT_NUMBER = 25;
    public static final int MINIMAL_NUMBER = 1;
    public static final int MAXIMAL_NUMBER = 100;
    public static final int MINIMAL_MORALE = 1;
    public static final int MAXIMAL_MORALE = 100;
    
    /* Creation */

    protected AbstractEntity(EntityType type)
    {
        this.type = type;

        image = assetManager.getImage(type);
        brightImage = assetManager.getBrightImage(type);
        isMarked = false;
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

    public AbstractEntity copy()
    {
        return newInstance(type);
    }
    
    /* Accessors & Mutators */

    public EntityType getType()
    {
        return type;
    }

    public String getName()
    {
        return type.toString();
    }

    public AbstractField changeField(AbstractField newField)
    {
        AbstractField oldField = field;
        field = newField;
        
        if (oldField != null)
        {
            oldField.setEntity(null);
        }
        if (newField != null)
        {
            newField.setEntity(this);
        }
        
        return oldField;
    }
    
    /* Accessors & Mutators: Graphics */

    public BufferedImage getImage()
    {
        return image;
    }
    
    public Icon getIcon()
    {
        return assetManager.getIcon(type);
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
    
    /* Graphics */

    public void draw(Graphics2D graphics, Doublet<Integer> position, Dimension size)
    {
        int x = (int) (position.left + 0.15 * size.width);
        int y = (int) (position.right + 0.30 * size.height);
        int w = (int) (0.7 * size.width);
        int h = (int) (0.7 * size.height);
        graphics.drawImage(!isMarked ? image : brightImage, x, y, w, h, null);
    }

    /* Purchasing */
    
    public abstract String getDescription();

    public abstract String getCondition();

    public abstract String getPricing();
    
    public int computePriceFor(int number)
    {
        return priceSlope * number + priceIntercept;
    }
    
    /* Spawning */
    
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
    
    /* Movement */
    
    public abstract Map<Hex, List<Hex>> getMovementRange(WorldAccessor accessor);
    
    /* Arithmetics */

    public int computePrice()
    {
        return priceSlope * number + priceIntercept;
    }

    public void setNumber(int newNumber)
    {
        if (newNumber > MAXIMAL_NUMBER)
        {
            //throw ...;
        }
        else if (newNumber < MINIMAL_NUMBER)
        {
            //throw ...;
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
    
    public boolean canMerge()
    {
        return number < MAXIMAL_NUMBER;
    }

    public void setMorale(int newMorale)
    {
        if (newMorale > MAXIMAL_MORALE)
        {
            //throw ...;
        }
        else if (newMorale < MINIMAL_MORALE)
        {
            //throw ...;
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

    public static class OutOfRangeException extends Exception {}

    public AbstractEntity extract(int extractedNumber) throws OutOfRangeException
    {
        if (extractedNumber < 1 || extractedNumber >= number)
        {
            throw new OutOfRangeException();
        }

        int extractedMorale = (int) ((double) extractedNumber / (double) number * (double) morale);

        AbstractEntity extractedEntity = copy();
        extractedEntity.number = extractedNumber;
        extractedEntity.morale = extractedMorale;

        number -= extractedNumber;
        morale -= extractedMorale;

        return extractedEntity;
    }

    public AbstractEntity merge(AbstractEntity other)
    {
        int aggregateNumber = number + other.number;
        int aggregateMorale = morale + other.morale;
        
        if (aggregateNumber <= MAXIMAL_NUMBER)
        {
            number = aggregateNumber;
            morale = Math.min(aggregateMorale, MAXIMAL_MORALE);
            
            other.number = 0;
            other.morale = 0;
            
            return null; // no remainder
        }
        else
        {
            int mergedNumber = MAXIMAL_NUMBER;
            int mergedMorale = (int) ((double) MAXIMAL_NUMBER / aggregateNumber * aggregateMorale);
            
            int remainingNumber = MAXIMAL_NUMBER - aggregateNumber;
            int remainingMorale = aggregateMorale - mergedMorale;
            
            number = mergedNumber;
            morale = Math.min(mergedMorale, MAXIMAL_MORALE);
            
            other.number = remainingNumber;
            other.morale = Math.min(remainingMorale, MAXIMAL_MORALE);
            
            return other; // remainder
        }
    }
}
