package my.units;

import java.awt.image.*;
import java.io.*;
import javax.imageio.*;

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
        InputStream leftStream = getClass().getResourceAsStream("Left Arrow.png");
        InputStream rightStream = getClass().getResourceAsStream("Right Arrow.png");
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
}
