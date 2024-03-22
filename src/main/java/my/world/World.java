/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package my.world;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.*;
import javax.imageio.ImageIO;

/**
 *
 * @author Kay Jay O'Nail
 */
public class World
{
    private Map<FieldType, BufferedImage> images;
    private Map<Hex, Field> fields;
    
    private int hexSurface(int side)
    {
        return 3 * side * (side - 1) + 1;
    }
    
    public World(int radius)
    {
        /* Read images. */
        images = new HashMap<>(FieldType.values().length);
        for (var fieldType : FieldType.values())
        {
            InputStream stream = getClass().getResourceAsStream(fieldType.path);
            BufferedImage image;
            try
            {
                image = ImageIO.read(stream);
            }
            catch (Exception e)
            {
                // what to do???
            }
        }
        
        /* Make the grid. */
        assert (radius > 0);
        
        fields = new HashMap<>(hexSurface(radius));
        fields.put(Hex.make(0, 0, 0), new Field());
        for (int r = 1; r < radius; ++r)
        {
            for (int i = 0; i < r; ++i)
            {
                
            }
        }
    }
    
    public void draw(Graphics2D graphics)
    {
        
    }
}
