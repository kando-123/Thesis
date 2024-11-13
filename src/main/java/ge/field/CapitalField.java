package ge.field;

import ge.entity.*;
import ge.main.*;
import ge.utilities.*;
import java.awt.*;
import java.awt.font.*;
import java.text.*;

/**
 *
 * @author Kay Jay O'Nail
 */
public class CapitalField extends PropertyField implements Fortification, Spawner, Commercial
{
    private int fortitude;
    private final Invoker<GameplayManager> invoker;
    
    private static final int FORTITUDE = 200;
    private static final int MINIMAL_FORTITUDE = 1;
    public static final int INCOME = 200;
    
    public CapitalField(Hex coords, Invoker<GameplayManager> invoker)
    {
        super(coords);
        this.invoker = invoker;
        
        fortitude = FORTITUDE;
    }

    @Override
    public boolean canSpawn(EntityType type)
    {
        return !isOccupied() && (type == EntityType.CAVALRY || type == EntityType.INFANTRY);
    }

    @Override
    public int getIncome()
    {
        return INCOME;
    }

    @Override
    public void spawn(Entity entity)
    {
        placeEntity(entity);
        entity.setMovable(true);
    }
    
    private void subtractFortitude(int loss)
    {
        fortitude = Math.max(fortitude - loss, MINIMAL_FORTITUDE);
    }

    @Override
    public Entity placeEntity(Entity comer)
    {
        var oldOwner = owner;
        
        Entity remainder = null;
        if (!isOwned())
        {
            /* Move. */
            entity = comer;
            owner = comer.getOwner();
        }
        else if (!isOccupied())
        {
            if (isFellow(comer))
            {
                /* Move. */
                entity = comer;
            }
            else
            {
                /* Defend. */
                int attack = comer.strength();
                int fortitude = getFortitude();
                subtractFortitude(attack);
                
                if (attack > fortitude)
                {
                    // If the comer is stronger than this,
                    // the fortification is damaged and the comer conquers this field.
                    comer.defeat(fortitude);
                    entity = comer;
                    owner = entity.getOwner();
                }
                // else: Just subtract the attack (done); the comer perishes.
            }
        }
        else
        {
            if (isFellow(comer))
            {
                /* Merge. */
                remainder = entity.merge(comer);
            }
            else
            {
                /* Militate. */
                final int defense = entity.strength();
                final int fortitude = getFortitude();
                final int attack = comer.strength();
                
                int fortitudeLoss = (int) ((double) fortitude / (fortitude + defense) * attack);
                subtractFortitude(fortitudeLoss);
                
                if (defense + fortitude > attack)
                {
                    /* Victory. */
                    entity.defeat(attack - fortitudeLoss);
                }
                else
                {
                    /* Loss. */
                    comer.defeat(defense + fortitude);
                    entity = comer;
                    owner = comer.getOwner();
                }
            }
        }
        
        if (entity != null)
        {
            entity.setMovable(false);
        }
        
        if (owner != oldOwner)
        {
            invoker.invoke(new DropCommand(oldOwner));
        }
        
        return remainder;
    }

    @Override
    public int getFortitude()
    {
        return fortitude;
    }
    
    private static final float BAR_HEIGHT_FRACTION = 0.2f;

    @Override
    public void draw(Graphics2D graphics, int xPosition, int yPosition, int width, int height)
    {
        drawField(graphics, xPosition, yPosition, width, height);
        drawContour(graphics, xPosition, yPosition, width, height);

        var text = String.format("D%d", getFortitude());
        if (entity != null)
        {
            entity.draw(graphics, xPosition, yPosition, width, height, text);
        }
        else
        {
            final int minimalBarHeight = 12;
            if (BAR_HEIGHT_FRACTION * height > minimalBarHeight)
            {
                drawBar(graphics, xPosition, yPosition, width, height, text);
            }
        }
    }

    private void drawBar(Graphics2D graphics, int xPosition, int yPosition, int width, int height, String text)
    {
        var attributedBar = new AttributedString(text);
        final float size = BAR_HEIGHT_FRACTION * height;
        attributedBar.addAttribute(TextAttribute.SIZE, size);
        attributedBar.addAttribute(TextAttribute.BACKGROUND, Color.WHITE);
        
        graphics.drawString(attributedBar.getIterator(), (float) (xPosition + width / 4), (float) yPosition + size);
    }
}
