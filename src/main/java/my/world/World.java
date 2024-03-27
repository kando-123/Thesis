/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package my.world;

import my.world.field.*;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.*;
import my.input.InputHandler;

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
    private double scale;
    
    private Map<Hex, Field> fields;
    
    private final InputHandler inputHandler;
    
    private int hexSurface(int side)
    {
        return 3 * side * (side - 1) + 1;
    }
    
    public World(int side)
    {
        hexOuterRadius = 30;
        hexInnerRadius = (int) ((double) hexOuterRadius * Math.sin(Math.PI / 3.0));
        hexWidth = 2 * hexOuterRadius;
        hexHeight = 2 * hexInnerRadius;
        scale = 1.0;
        gridSide = side;
        createGrid(gridSide);
        
        inputHandler = InputHandler.getInstance();
        eastwards = new Pixel(+5, 0);
        southwards = new Pixel(0, +5);
        westwards = new Pixel(-5, 0);
        northwards = new Pixel(0, -5);
    }
    
    private void createGrid(int side)
    {
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
    
    public void setCenter(Pixel newCenterOffset)
    {
        gridCenterOffset = newCenterOffset;
    }
    
    private final Pixel eastwards;
    private final Pixel southwards;
    private final Pixel westwards;
    private final Pixel northwards;
    
    public void update()
    {
        if (inputHandler.shiftEastwards())
        {
            gridCenterOffset.add(eastwards);
        }
        else if (inputHandler.shiftSouthwards())
        {
            gridCenterOffset.add(southwards);
        }
        else if (inputHandler.shiftWestwards())
        {
            gridCenterOffset.add(westwards);
        }
        else if (inputHandler.shiftNorthwards())
        {
            gridCenterOffset.add(northwards);
        }
        
        if (inputHandler.zoomIn())
        {
            scale = Math.min(scale + 0.02, 2.5);
        }
        else if (inputHandler.zoomOut())
        {
            scale = Math.max(scale - 0.02, 0.5);
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
            Pixel pixel = hex.getCornerPixel(hexOuterRadius, hexInnerRadius);
            
            Field field = entry.getValue();
            BufferedImage image = fieldManager.getImage(field.getType());
            
            int x = (int) ((double) pixel.xCoord * scale) + gridCenterOffset.xCoord;
            int y = (int) ((double) pixel.yCoord * scale) + gridCenterOffset.yCoord;
            int w = (int) ((double) hexWidth * scale);
            int h = (int) ((double) hexHeight * scale);
            
            graphics.drawImage(image, x, y, w, h, null);
        }
    }
}
