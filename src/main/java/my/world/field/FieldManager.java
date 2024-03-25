/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package my.world.field;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import javax.imageio.ImageIO;

/**
 *
 * @author Kay Jay O'Nail
 */
public class FieldManager
{
    private Map<FieldType, BufferedImage> images;
    
    private FieldManager()
    {
        images = new HashMap<>(FieldType.values().length);
        for (var fieldType : FieldType.values())
        {
            InputStream stream = getClass().getResourceAsStream(fieldType.path);
            BufferedImage image;
            try
            {
                image = ImageIO.read(stream);
                images.put(fieldType, image);
            }
            catch (IOException e)
            {
                /* The exception will not have been thrown in a correct program.
                   Behavior of this function depends only on internal files,
                   not on external input. */
                System.err.println("Internal error. Some resources are lacking.");
            }
        }
    }
    
    private static FieldManager instance = null;
    
    public static FieldManager getInstance()
    {
        if (instance == null)
        {
            instance = new FieldManager();
        }
        return instance;
    }
    
    public BufferedImage getImage(FieldType type)
    {
        return images.get(type);
    }
}
