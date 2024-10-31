package ge.entity;

import ge.player.Player;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.font.TextAttribute;
import java.awt.image.*;
import java.text.AttributedString;

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
    
    public static final int MINIMAL_NUMBER = 1;
    public static final int MAXIMAL_NUMBER = 100;
    
    protected Entity(Player owner)
    {
        this.owner = owner;
        
        var name = getName();
        this.image = ASSET_MANAGER.getImage(name);
        this.brightImage = ASSET_MANAGER.getBrightImage(name);
    }
    
    private String getName()
    {
        String name = getClass().getName();
        return name.substring(name.lastIndexOf('.') + 1, name.lastIndexOf("Entity"));
    }
    
    public static Entity newInstance(EntityType type, Player owner)
    {
        return switch (type)
        {
            case CAVALRY ->
            {
                yield new CavalryEntity(owner);
            }
            case INFANTRY ->
            {
                yield new InfantryEntity(owner);
            }
            case NAVY ->
            {
                yield new NavyEntity(owner);
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
        final float iconWidthFraction = 0.7f;
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
        final float iconHeightFraction = 1.0f;
        final float sideMargin = (1 - iconWidthFraction) / 2;
        final float topMargin = (1 - iconHeightFraction);
        
        int x = (int) (xPosition + sideMargin * width);
        int y = (int) (yPosition + topMargin * height);
        int w = (int) (iconWidthFraction * width);
        int h = (int) (iconHeightFraction * height);
        
        graphics.drawImage(!marked ? image : brightImage, x, y, w, h, null);
    }
}
