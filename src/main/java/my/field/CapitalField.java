package my.field;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.font.TextAttribute;
import java.text.AttributedString;
import my.entity.AbstractEntity;
import my.utils.Doublet;

/**
 *
 * @author Kay Jay O'Nail
 */
public class CapitalField extends AbstractField implements Defense, Spawner
{
    private static final int FORTITUDE = 200;
    
    public CapitalField()
    {
        super(FieldType.CAPITAL);
    }

    @Override
    public boolean canSpawn(AbstractEntity entity)
    {
        return !hasEntity() && switch (entity.getType())
        {
            case INFANTRY, CAVALRY ->
            {
                yield true;
            }
            default ->
            {
                yield false;
            }
        };
    }
    
    @Override
    public void draw(Graphics2D graphics, Doublet<Integer> position, Dimension size)
    {
        super.draw(graphics, position, size);
        
        if (!hasEntity() && 0.13 * size.height > 9)
        {
            String bar = String.format("D%d", FORTITUDE);
            
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
        return FORTITUDE;
    }
}