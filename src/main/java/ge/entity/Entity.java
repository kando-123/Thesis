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
import java.util.*;

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
    }

    private int initialMorale(int number)
    {
        final int divisor = 5;
        
        return number / divisor;
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
    
    public abstract EntityType type();

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

    public static class TooFarAwayException extends Exception
    {
    }

    public static class GoalNotReachedException extends Exception
    {
    }

    public List<Hex> path(Field origin, Field target, WorldAccessor accessor) throws TooFarAwayException, GoalNotReachedException
    {
        // A* Algorithm
        
        // Hexes of the origin and the targer for future reference.
        final Hex originHex = origin.getHex();
        final Hex targetHex = target.getHex();
        
        // Radius - for further reference.
        final int radius = radius();
        
        // Check if it is possible to find the path at all.
        if (originHex.distance(targetHex) > radius)
        {
            throw new TooFarAwayException();
        }

        // Heuristic for the A* algorithm.
        @FunctionalInterface
        interface Heuristic
        {
            int of(Hex hex);
        }
        final Heuristic heuristic = (Hex hex) -> hex.distance(targetHex);

        // The queue of the hexes of the fields to visit.
        // The first hex to consider is the closest to the target.
        Queue<Hex> frontier = new PriorityQueue<>((h1, h2) -> heuristic.of(h1) - heuristic.of(h2));
        frontier.add(originHex);

        // The map of predecessors to later retrieve the path.
        Map<Hex, Hex> predecessor = new HashMap<>();

        // Distances from the origin along the currently cheapest paths.
        Map<Hex, Integer> distance = new HashMap<>();
        distance.put(originHex, 0);

        // Propagate.
        while (!frontier.isEmpty())
        {
            // Consider the hex that is closest to the target.
            Hex current = frontier.poll();
            
            // If current is the target, finish with success.
            if (current.equals(targetHex))
            {
                // Retrieve the path.
                List<Hex> path = new LinkedList<>();
                path.add(current);
                while (predecessor.containsKey(current))
                {
                    current = predecessor.get(current);
                    path.addFirst(current);
                }
                return path;
            }
            
            // If this field is intransitable (unless it is the origin), continue.
            var currentField = accessor.getField(current);
            if (!canTransit(currentField) && currentField != origin)
            {
                continue;
            }

            // Consider the neighbors.
            // ! Mind that some hexes can be outside of the world.
            // ! Mind that if this field is accessible but not transitable, it cannot be
            //   gone further.
            //   ! However, from the origin, it can be gone to a next field, even if
            //     the origin is intransitable.
            for (var neighbor : current.neighbors())
            {
                // Access the adjacent field.
                var field = accessor.getField(neighbor);

                // If no field exists on those coordinates, continue.
                if (field == null)
                {
                    continue;
                }

                // If that field is inaccessible, continue.
                if (!canAccess(field))
                {
                    continue;
                }

                // Compute the path to the neighbor.
                var tentative = distance.get(current) + 1;
                
                // (If the neighbor would be unachievable, continue.)
                if (tentative > radius)
                {
                    continue;
                }
                
                // Update the path and distance if it is quicker to pass through current,
                // or this is the first way found at all.
                var present = distance.get(neighbor);
                if (present == null || tentative < present)
                {
                    predecessor.put(neighbor, current);
                    distance.put(neighbor, tentative);

                    // Add the neighbor to the frontier.
                    if (!frontier.contains(neighbor))
                    {
                        frontier.add(neighbor);
                    }
                }
            }
        }

        // If the available nodes have been exhausted and the solution has not been found,
        // inform that there is no path.
        throw new GoalNotReachedException();
    }

    public boolean canMerge(Entity entity)
    {
        return isFellow(entity) && entity.number < MAXIMAL_NUMBER;
    }

    public Entity merge(Entity other)
    {
        movable = false;
        
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
    
    public void defeat(int fortitude)
    {
        double strength = strength();

        number = Math.max((int) (number - number / strength * fortitude), MINIMAL_NUMBER);
        morale = Math.max((int) (morale - morale / strength * fortitude), MINIMAL_MORALE);
    }
    
    public int getNumber()
    {
        return number;
    }
    
    public int getMorale()
    {
        return morale;
    }
    
    public void addMorale(int increase)
    {
        morale += increase;
        
        if (morale < MINIMAL_MORALE)
        {
            morale = MINIMAL_MORALE;
        }
        else if (morale > MAXIMAL_MORALE)
        {
            morale = MAXIMAL_MORALE;
        }
        else if (morale > number)
        {
            morale = number;
        }
    }
    
    public abstract EntityType getExtractedType();
    
    public boolean canExtract()
    {
        return movable && number > MINIMAL_NUMBER;
    }
    
    public Entity extract(int extractedNumber)
    {
        int extractedMorale = (int) ((double) extractedNumber / number * morale);
        
        var extract = newInstance(getExtractedType(), owner, extractedNumber);
        extract.morale = extractedMorale;
        extract.movable = true;
        
        number -= extractedNumber;
        morale -= extractedMorale;
        
        return extract;
    }
}
