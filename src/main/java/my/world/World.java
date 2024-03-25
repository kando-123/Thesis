/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package my.world;

import my.world.field.*;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.*;

/**
 *
 * @author Kay Jay O'Nail
 */
public class World
{
    public final int hexOuterRadius;
    public final int hexInnerRadius;
    public final int hexWidth;
    public final int hexHeight;
    public final int gridSide;
    
    private Pixel gridCenterOffset;
    
    private Map<Hex, Field> fields;
    
    private int hexSurface(int side)
    {
        return 3 * side * (side - 1) + 1;
    }
    
    public World(int side)
    {
        hexOuterRadius = 35;
        hexInnerRadius = (int) ((double) hexOuterRadius * Math.sin(Math.PI / 3.0));
        hexWidth = 2 * hexOuterRadius;
        hexHeight = 2 * hexInnerRadius;
        gridSide = side;
        
        int centerX = hexOuterRadius * (int) (1.5 * (double) gridSide - 0.25);
        int centerY = gridSide * hexHeight - hexInnerRadius;
        gridCenterOffset = new Pixel(centerX, centerY);
        
        assert (side > 0);
        
        fields = new HashMap<>(hexSurface(side));
        {
            Hex hex = Hex.make(0, 0, 0);
            Field field = new Field(FieldType.LAND_1);
            fields.put(hex, field);
        }
        for (int i = 1; i < side; ++i)
        {
            /* Right side: p = +i. */
            for (int j = 0; j < i; ++j)
            {
                Hex hex = Hex.make(+i, -i + j, -j);
                assert (hex != null);
                Field field = new Field(FieldType.LAND_1);
                fields.put(hex, field);
            }
            /* Left side: p = -i. */
            for (int j = 0; j < i; ++j)
            {
                Hex hex = Hex.make(-i, +i - j, +j);
                assert (hex != null);
                Field field = new Field(FieldType.LAND_1);
                fields.put(hex, field);
            }
            /* Bottom left side: q = +i. */
            for (int j = 0; j < i; ++j)
            {
                Hex hex = Hex.make(-j, +i, -i + j);
                assert (hex != null);
                Field field = new Field(FieldType.LAND_1);
                fields.put(hex, field);
            }
            /* Top right side: q = -i. */
            for (int j = 0; j < i; ++j)
            {
                Hex hex = Hex.make(+j, -i, +i - j);
                assert (hex != null);
                Field field = new Field(FieldType.LAND_1);
                fields.put(hex, field);
            }
            /* Top left side: r = +i. */
            for (int j = 0; j < i; ++j)
            {
                Hex hex = Hex.make(-i + j, -j, +i);
                assert (hex != null);
                Field field = new Field(FieldType.LAND_1);
                fields.put(hex, field);
            }
            /* Bottom right side: r = -i. */
            for (int j = 0; j < i; ++j)
            {
                Hex hex = Hex.make(+i - j, +j, -i);
                assert (hex != null);
                Field field = new Field(FieldType.LAND_1);
                fields.put(hex, field);
            }
        }
    }
    
    public void draw(Graphics2D graphics)
    {
        FieldManager fieldManager = FieldManager.getInstance();
        var iterator = fields.entrySet().iterator();
        while (iterator.hasNext())
        {
            Map.Entry<Hex, Field> entry = iterator.next();
            
            Hex hex = entry.getKey();
            Pixel pixel = hex.getCornerPixel(hexOuterRadius, hexInnerRadius).plus(gridCenterOffset);
            
            Field field = entry.getValue();
            BufferedImage image = fieldManager.getImage(field.getType());
            
            graphics.drawImage(image, pixel.getX(), pixel.getY(), hexWidth, hexHeight, null);
        }
    }
}
