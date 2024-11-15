package ge.field;

import ge.entity.*;
import ge.main.*;
import ge.player.Player;
import ge.utilities.*;
import java.awt.*;
import java.awt.font.*;
import java.text.*;

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
    public Entity placeEntity(Entity comer, Invoker<GameplayManager> invoker)
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
                final Player defender = owner;
                final Player attacker = comer.getOwner();
                
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
                    
                    invoker.invoke(new AdjustMoraleCommand(coords, attacker, defender));
                }
                else
                {
                    //Just subtract the attack (done); the comer perishes.
                    invoker.invoke(new AdjustMoraleCommand(coords, defender, attacker));
                }
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
                final Player defender = owner;
                final Player attacker = comer.getOwner();
                
                final int defense = entity.strength();
                final int fortitude = getFortitude();
                final int attack = comer.strength();

                int fortitudeLoss = (int) ((double) fortitude / (fortitude + defense) * attack);
                subtractFortitude(fortitudeLoss);

                if (defense + fortitude > attack)
                {
                    /* Defender's Victory. */
                    entity.defeat(attack - fortitudeLoss);
                    
                    invoker.invoke(new AdjustMoraleCommand(coords, defender, attacker));
                }
                else
                {
                    /* Attacker's Victory. */
                    comer.defeat(defense + fortitude);
                    entity = comer;
                    owner = comer.getOwner();
                    
                    invoker.invoke(new AdjustMoraleCommand(coords, attacker, defender));
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
