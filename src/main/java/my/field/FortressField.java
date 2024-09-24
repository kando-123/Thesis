package my.field;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.font.TextAttribute;
import java.text.AttributedString;
import my.player.UnaryPredicate;
import my.utils.Doublet;
import my.utils.Hex;
import my.world.WorldAccessor;

/**
 *
 * @author Kay Jay O'Nail
 */
public class FortressField extends DefenseField
{
    private static final int[] FORTITUDE = {100, 125, 150};
    private int level;
    private int fortitude;
    
    public FortressField()
    {
        super(FieldType.FORTRESS);
        
        priceIntercept = 400;
        priceSlope = 100;
        
        level = 0;
        fortitude = FORTITUDE[level];
    }

    @Override
    public String getDescription()
    {
        return "Fortress gives additional defence points.";
    }

    @Override
    public String getCondition()
    {
        return "To build a fortress, you need a continental field.";
    }

    @Override
    public UnaryPredicate<Hex> getPredicate(WorldAccessor accessor)
    {
        return (Hex item) ->
        {
            var field = accessor.getFieldAt(item);
            return field != null && field.isContinental();
        };
    }
    
    @Override
    public void draw(Graphics2D graphics, Doublet<Integer> position, Dimension size)
    {
        super.draw(graphics, position, size);
        
        if (!hasEntity() && 0.13 * size.height > 9)
        {
            String bar = String.format("D%d", fortitude);
            
            AttributedString attributedBar = new AttributedString(bar);
            attributedBar.addAttribute(TextAttribute.SIZE, 0.13 * size.height);
            attributedBar.addAttribute(TextAttribute.FOREGROUND, Color.WHITE);
            graphics.drawString(attributedBar.getIterator(),
                    (float) (position.left + 0.36 * size.width),
                    (float) (position.right + 0.13 * size.height));
        }
    }

    @Override
    public int getFortitude()
    {
        return fortitude;
    }
    
    public static class MaximalLevelException extends Exception {}
    
    public void upgrade() throws MaximalLevelException
    {
        if (level < FORTITUDE.length - 1)
        {
            fortitude = FORTITUDE[++level];
        }
        else
        {
            throw new MaximalLevelException();
        }
    }
}
