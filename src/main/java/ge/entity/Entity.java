package ge.entity;

import ge.field.*;
import ge.player.*;
import ge.utilities.*;
import ge.world.*;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.font.*;
import java.awt.image.*;
import java.text.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/**
 *
 * @author Kay Jay O'Nail
 */
public abstract class Entity
{
    private boolean marked;
    private final BufferedImage image;
    private final BufferedImage brightImage;
    private static final EntityAssetManager ASSET_MANAGER = EntityAssetManager.getInstance();

    private final Player owner;

    private int number;
    private int morale;
    private boolean movable;

    public static final int MINIMAL_NUMBER = 1;
    public static final int MAXIMAL_NUMBER = 100;
    public static final int MINIMAL_MORALE = 1;
    public static final int MAXIMAL_MORALE = 100;

    protected Entity(Player owner, int number)
    {
        this.owner = owner;

        var name = getName();
        this.image = ASSET_MANAGER.getImage(name);
        this.brightImage = ASSET_MANAGER.getBrightImage(name);

        this.number = number;
        this.morale = initialMorale(number);
        
        movable = true;
    }

    private int initialMorale(int number)
    {
        return number / 5;
    }

    private String getName()
    {
        String name = getClass().getName();
        return name.substring(name.lastIndexOf('.') + 1, name.lastIndexOf("Entity"));
    }

    public Player getOwner()
    {
        return owner;
    }

    public boolean isFellow(Entity other)
    {
        return owner == other.owner;
    }

    public boolean isFellow(Field field)
    {
        return field.getOwner() == owner;
    }

    public static Entity newInstance(EntityType type, Player owner, int number)
    {
        return switch (type)
        {
            case CAVALRY ->
            {
                yield new CavalryEntity(owner, number);
            }
            case INFANTRY ->
            {
                yield new InfantryEntity(owner, number);
            }
            case NAVY ->
            {
                yield new NavyEntity(owner, number);
            }
        };
    }

    public void draw(Graphics2D graphics, int xPosition, int yPosition, int width, int height)
    {
        draw(graphics, xPosition, yPosition, width, height, null);
    }

    private static final float BAR_HEIGHT_FRACTION = 0.2f;

    public void draw(Graphics2D graphics, int xPosition, int yPosition, int width, int height, String info)
    {
        final int minimalBarHeight = 9;
        if (BAR_HEIGHT_FRACTION * height > minimalBarHeight)
        {
            drawWithBar(graphics, xPosition, yPosition, width, height, info);
        }
        else
        {
            drawWithoutBar(graphics, xPosition, yPosition, width, height);
        }
    }

    private void drawWithBar(Graphics2D graphics, int xPosition, int yPosition, int width, int height, String info)
    {
        final float iconWidthFraction = 0.6f;
        final float iconHeightFraction = 0.7f;
        final float sideMargin = (1 - iconWidthFraction) / 2;
        final float topMargin = (1 - iconHeightFraction);

        int x = (int) (xPosition + sideMargin * width);
        int y = (int) (yPosition + topMargin * height);
        int w = (int) (iconWidthFraction * width);
        int h = (int) (iconHeightFraction * height);

        graphics.drawImage(!marked ? image : brightImage, x, y, w, h, null);

        String bar = (info == null || info.isBlank())
                ? String.format("N%d M%d", number, morale)
                : String.format("N%d M%d %s", number, morale, info);

        var attributedBar = new AttributedString(bar);
        final float size = BAR_HEIGHT_FRACTION * height;
        attributedBar.addAttribute(TextAttribute.SIZE, size);
        attributedBar.addAttribute(TextAttribute.BACKGROUND, Color.WHITE);
        graphics.drawString(attributedBar.getIterator(), (float) xPosition, (float) yPosition + size);
    }

    private void drawWithoutBar(Graphics2D graphics, int xPosition, int yPosition, int width, int height)
    {
        final float iconWidthFraction = 0.8f;
        final float iconHeightFraction = 0.9f;
        final float sideMargin = (1 - iconWidthFraction) / 2;
        final float topMargin = (1 - iconHeightFraction);

        int x = (int) (xPosition + sideMargin * width);
        int y = (int) (yPosition + topMargin * height);
        int w = (int) (iconWidthFraction * width);
        int h = (int) (iconHeightFraction * height);

        graphics.drawImage(!marked ? image : brightImage, x, y, w, h, null);
    }

    public void setMovable(boolean m)
    {
        movable = m;
    }

    public boolean canMove()
    {
        return movable;
    }

    protected abstract boolean canAccess(Field field);

    protected abstract boolean canTransit(Field field);

    protected abstract int radius();

    public Set<Hex> range(Hex center, WorldAccessor accessor)
    {
        if (!movable)
        {
            return new HashSet<>();
        }

        Set<Hex> range = new HashSet<>();
        Set<Hex> visited = new HashSet<>();
        Queue<Hex> queue = new LinkedList<>();

        queue.add(center);
        visited.add(center);

        for (int i = 0; i < radius(); ++i)
        {
            for (int j = queue.size(); j > 0; --j)
            {
                Hex current = queue.remove();

                for (var neighborHex : current.neighbors())
                {
                    if (visited.contains(neighborHex))
                    {
                        continue;
                    }

                    var neighborField = accessor.getField(neighborHex);
                    if (neighborField == null)
                    {
                        continue;
                    }
                    
                    visited.add(neighborHex);
                    if (canAccess(neighborField))
                    {
                        range.add(neighborHex);
                        if (canTransit(neighborField))
                        {
                            queue.add(neighborHex);
                        }
                    }
                }
            }
        }
        return range;
    }
    
    public List<Field> path(Field origin, Field target, WorldAccessor accessor)
    {
        // DFS
        throw new UnsupportedOperationException();
    }
    
    public boolean canMerge(Entity entity)
    {
        return isFellow(entity) && entity.number < MAXIMAL_NUMBER;
    }

    public Entity merge(Entity other)
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

    public int strength()
    {
        return number + morale;
    }

    public void defeat(Entity other)
    {
        double strength = strength();
        double damage = other.strength();

        number = Math.max((int) (number - number / strength * damage), MINIMAL_NUMBER);
        morale = Math.max((int) (morale - morale / strength * damage), MINIMAL_MORALE);
    }
}
