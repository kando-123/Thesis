package my.main;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 *
 * @author Kay Jay O'Nail
 */
public class ArrowsManager
{
    private static final ArrowsManager instance = new ArrowsManager();
    
    public static ArrowsManager getInstance()
    {
        return instance;
    }
    
    private BufferedImage leftArrow;
    private BufferedImage rightArrow;
    
    private ArrowsManager()
    {
        InputStream leftStream = getClass().getResourceAsStream("/Arrows/Left Arrow.png");
        InputStream rightStream = getClass().getResourceAsStream("/Arrows/Right Arrow.png");
        try
        {
            leftArrow = ImageIO.read(leftStream);
            rightArrow = ImageIO.read(rightStream);
        }
        catch (IOException io)
        {
            System.err.println(io.getMessage());
        }
    }
    
    public BufferedImage getLeftArrow()
    {
        return leftArrow;
    }
    
    public BufferedImage getRightArrow()
    {
        return rightArrow;
    }
    
    public Icon getLeftArrowAsIcon()
    {
        return new ImageIcon(leftArrow);
    }
    
    public Icon getRightArrowAsIcon()
    {
        return new ImageIcon(rightArrow);
    }
}
