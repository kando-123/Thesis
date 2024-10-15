package my.entity;

import my.field.Spawner;
import my.field.Fortification;
import my.field.AbstractField;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.text.AttributedString;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import javax.swing.Icon;
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

    protected int number = DEFAULT_NUMBER;
    protected int morale;

    protected int radius;
    protected boolean movable;

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

        String bar;
        if (field != null && field.isFortification())
        {
            var fortification = (Fortification) field;
            bar = String.format("N%d M%d D%d", number, morale, fortification.getDefense());
        }
        else
        {
            bar = String.format("N%d M%d", number, morale);
        }

        if (0.2 * size.height > 9)
        {
            AttributedString attributedBar = new AttributedString(bar);
            attributedBar.addAttribute(TextAttribute.SIZE, 0.2 * size.height);
            attributedBar.addAttribute(TextAttribute.BACKGROUND, Color.WHITE);
            graphics.drawString(attributedBar.getIterator(), (float) position.left, (float) (position.right + 0.2 * size.height));
        }
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
        return (var hex) ->
        {
            var place = accessor.getFieldAt(hex);
            if (place != null && place.isSpawner())
            {
                Spawner spawner = (Spawner) place;
                return spawner.canSpawn(AbstractEntity.this);
            }
            else
            {
                return false;
            }
        };
    }

    /* Movement */
    public void setMovable(boolean movable)
    {
        this.movable = movable;
    }

    public boolean isMovable()
    {
        return movable;
    }

    protected abstract boolean isAccessible(AbstractField place);

    protected abstract boolean isTransitable(AbstractField place);

    public Map<Hex, List<Hex>> getMovementRange(WorldAccessor accessor)
    {
        if (!movable)
        {
            return new HashMap<>();
        }

        Map<Hex, List<Hex>> range = new HashMap<>();
        Set<Hex> visited = new HashSet<>();
        Queue<Hex> queue = new LinkedList<>();

        Hex center = field.getHex();
        queue.add(center);
        visited.add(center);

        for (int i = 0; i < radius; ++i)
        {
            for (int j = queue.size(); j > 0; --j)
            {
                Hex current = queue.remove();
                List<Hex> oldPath = range.get(current), newPath = new ArrayList<>();
                if (oldPath != null)
                {
                    newPath.addAll(oldPath);
                    newPath.add(current);
                }

                for (var neighborHex : current.neighbors())
                {
                    if (visited.contains(neighborHex))
                    {
                        continue;
                    }

                    var neighborField = accessor.getFieldAt(neighborHex);
                    if (neighborField != null)
                    {
                        visited.add(neighborHex);
                        if (isAccessible(neighborField))
                        {
                            range.put(neighborHex, newPath);
                            if (isTransitable(neighborField))
                            {
                                queue.add(neighborHex);
                            }
                        }
                    }
                }
            }
        }
        return range;
    }

    public void setField(AbstractField newField)
    {
        field = newField;
    }

    public AbstractField getField()
    {
        return field;
    }
    
    public void occupy(AbstractField newField)
    {
        field = newField;
        if (field != null)
        {
            field.setEntity(this);
        }
    }
    
    public AbstractField pin(AbstractField newField)
    {
        if (field != null)
        {
            field.setEntity(null);
        }
        var oldField = field;
        
        field = newField;
        if (field != null)
        {
            field.setEntity(this);
        }
        
        return oldField;
    }
    
    public AbstractField unpin()
    {
        if (field != null)
        {
            field.setEntity(null);
        }
        
        var oldField = field;
        field = null;
        
        return oldField;
    }

    /* Arithmetics */
    public int computePrice()
    {
        return priceSlope * number + priceIntercept;
    }

    public void setNumber(int newNumber)
    {
        if (newNumber > MAXIMAL_NUMBER)
        {
            number = MAXIMAL_NUMBER;
        }
        else if (newNumber < MINIMAL_NUMBER)
        {
            number = MINIMAL_NUMBER;
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

    public void setMorale(int newMorale)
    {
        if (newMorale > MAXIMAL_MORALE)
        {
            morale = MAXIMAL_MORALE;
        }
        else if (newMorale < MINIMAL_MORALE)
        {
            morale = MINIMAL_MORALE;
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

    public static class OutOfRangeException extends Exception
    {
    }
    
    public static class AccessorIsNeededException extends Exception
    {
    }

    public abstract boolean canExtract() throws AccessorIsNeededException;
    public abstract boolean canExtract(WorldAccessor accessor);
    
    public EntityType getExtractedType()
    {
        return type;
    }

    public AbstractEntity extract(int extractedNumber) throws OutOfRangeException
    {
        if (extractedNumber < 1 || extractedNumber >= number)
        {
            throw new OutOfRangeException();
        }

        int extractedMorale = (int) ((double) extractedNumber / (double) number * (double) morale);

        AbstractEntity extract = newInstance(getExtractedType());
        extract.number = extractedNumber;
        extract.morale = extractedMorale;
        extract.movable = movable;

        number -= extractedNumber;
        morale -= extractedMorale;

        return extract;
    }

    public boolean canMerge(AbstractEntity entity)
    {
        return field.isFellow(entity)
               && entity.type == type
               && number < MAXIMAL_NUMBER;
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

            int remainingNumber = aggregateNumber - mergedNumber;
            int remainingMorale = aggregateMorale - mergedMorale;

            number = mergedNumber;
            morale = Math.min(mergedMorale, MAXIMAL_MORALE);

            other.number = remainingNumber;
            other.morale = Math.min(remainingMorale, MAXIMAL_MORALE);

            return other; // remainder
        }
    }
}
