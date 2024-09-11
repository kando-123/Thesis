package my.entity;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import my.field.AbstractField;
import my.utils.Doublet;

/**
 *
 * @author Kay Jay O'Nail
 */
public abstract class AbstractEntity
{
    private final EntityType type;
    private final BufferedImage image;
    
    private AbstractField field;
    
    private static final EntitiesManager entitiesManager = EntitiesManager.getInstance();
    
    protected AbstractEntity(EntityType type)
    {
        this.type = type;
        this.image = entitiesManager.getEntity(type);
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
}
