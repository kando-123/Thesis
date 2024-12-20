package ge.field;

import ge.entity.*;
import ge.main.*;
import ge.player.*;
import ge.utilities.*;
import java.awt.*;
import java.awt.image.*;

/**
 *
 * @author Kay Jay O'Nail
 */
public abstract class Field
{
    protected final Hex coords;

    private boolean marked;
    private final BufferedImage image;
    private final BufferedImage brightImage;

    private static final FieldAssetManager ASSET_MANAGER = FieldAssetManager.getInstance();

    protected Player owner;
    protected Entity entity;

    protected Field(Hex coords)
    {
        this.coords = coords;

        var name = getName();
        image = ASSET_MANAGER.getImage(name);
        brightImage = ASSET_MANAGER.getBrightImage(name);
    }

    private String getName()
    {
        String name = getClass().getName();
        return name.substring(name.lastIndexOf('.') + 1, name.lastIndexOf("Field"));
    }

    public Hex getHex()
    {
        return coords;
    }

    public void setMarked(boolean m)
    {
        marked = m;
    }

    public boolean isMarked()
    {
        return marked;
    }
    
    protected void drawField(Graphics2D graphics, int xPosition, int yPosition, int width, int height)
    {
        graphics.drawImage(!marked ? image : brightImage, xPosition, yPosition, width, height, null);
    }
    
    protected void drawContour(Graphics2D graphics, int xPosition, int yPosition, int width, int height)
    {
        if (owner != null)
        {
            graphics.drawImage(owner.getContour(), xPosition, yPosition, width, height, null);
        }
    }
    
    protected void drawEntity(Graphics2D graphics, int xPosition, int yPosition, int width, int height)
    {
        if (entity != null)
        {
            entity.draw(graphics, xPosition, yPosition, width, height);
        }
    }
    
    public void draw(Graphics2D graphics, int xPosition, int yPosition, int width, int height)
    {
        drawField(graphics, xPosition, yPosition, width, height);
        drawContour(graphics, xPosition, yPosition, width, height);
        drawEntity(graphics, xPosition, yPosition, width, height);
    }

    public void setOwner(Player newOwner)
    {
        owner = newOwner;
    }
    
    public void clearOwner()
    {
        owner = null;
    }

    public Player getOwner()
    {
        return owner;
    }

    public boolean isOwned()
    {
        return owner != null;
    }

    public boolean isOwned(Player player)
    {
        return owner == player;
    }

    public boolean isOccupied()
    {
        return entity != null;
    }
    
    public boolean isFellow(Entity comer)
    {
        return comer.getOwner() == owner;
    }
    
    public Entity getEntity()
    {
        return entity;
    }
    
    public Entity takeEntity()
    {
        var leaver = entity;
        entity = null;
        return leaver;
    }

    public Entity setEntity(Entity newEntity)
    {
        var oldEntity = entity;
        entity = newEntity;
        return oldEntity;
    }

    public Entity placeEntity(Entity comer, Invoker<GameplayManager> invoker)
    {
        assert (comer.getNumber() > 0);
        
        Entity remainder = null;
        if (entity == null)
        {
            /* Move. */
            entity = comer;
            owner = comer.getOwner();
        }
        else if (comer.isFellow(entity))
        {
            /* Merge. */
            remainder = entity.merge(comer);
        }
        else
        {
            /* Militation. */
            final Player defender = owner;
            final Player attacker = comer.getOwner();
            if (entity.strength() < comer.strength())
            {
                /* Attacker's victory. */
                comer.defeat(entity);
                entity = comer;
                owner = comer.getOwner();
                
                invoker.invoke(new AdjustMoraleCommand(coords, attacker, defender));
            }
            else
            {
                /* Defender's victory. */
                entity.defeat(comer);
                
                invoker.invoke(new AdjustMoraleCommand(coords, defender, attacker));
            }
        }
        
        entity.setMovable(false);
        
        return remainder;
    }
}
