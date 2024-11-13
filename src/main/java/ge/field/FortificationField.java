package ge.field;

import ge.entity.*;
import ge.utilities.*;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.font.TextAttribute;
import java.text.AttributedString;

/**
 *
 * @author Kay Jay O'Nail
 */
public abstract class FortificationField extends BuildingField implements Fortification
{
    protected FortificationField(Hex coords)
    {
        super(coords);
    }

    protected abstract void subtractFortitude(int newFortitude);

    @Override
    public Entity placeEntity(Entity comer)
    {
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

        return remainder;
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
