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
    private double scaleChange;
    private double maxScale;
    private double minScale;
    
    private Map<Hex, Field> fields;
    
    private final InputHandler inputHandler;
    private final FieldManager fieldManager;
    
    private int hexSurface(int side)
    {
        return 3 * side * (side - 1) + 1;
    }
    
    public World(int side)
    {
        hexOuterRadius = 40;
        hexInnerRadius = (int) ((double) hexOuterRadius * Math.sin(Math.PI / 3.0));
        hexWidth = 2 * hexOuterRadius;
        hexHeight = 2 * hexInnerRadius;
        scale = 1.0;
        scaleChange = 0.01;
        maxScale = 2.5;
        minScale = 0.5;
        
        gridSide = side;
        createGrid(gridSide);
        
        inputHandler = InputHandler.getInstance();
        fieldManager = FieldManager.getInstance();
    }
    
    private void createGrid(int side)
    {
        assert (side > 0);
        
        fields = new HashMap<>(hexSurface(side));
        {
            Hex hex = Hex.make(0, 0, 0);
            Field field = new Field(FieldType.LAND);
            fields.put(hex, field);
        }
        for (int i = 1; i < side; ++i)
        {
            for (int j = 0; j < i; ++j) // Right side: p = +i.
            {
                Hex hex = Hex.make(+i, -i + j, -j);
                assert (hex != null);
                Field field = new Field(FieldType.LAND);
                fields.put(hex, field);
            }
            for (int j = 0; j < i; ++j) // Left side: p = -i.
            {
                Hex hex = Hex.make(-i, +i - j, +j);
                assert (hex != null);
                Field field = new Field(FieldType.LAND);
                fields.put(hex, field);
            }
            for (int j = 0; j < i; ++j) // Bottom left side: q = +i.
            {
                Hex hex = Hex.make(-j, +i, -i + j);
                assert (hex != null);
                Field field = new Field(FieldType.LAND);
                fields.put(hex, field);
            }
            for (int j = 0; j < i; ++j) // Top right side: q = -i.
            {
                Hex hex = Hex.make(+j, -i, +i - j);
                assert (hex != null);
                Field field = new Field(FieldType.LAND);
                fields.put(hex, field);
            }
            for (int j = 0; j < i; ++j) // Top left side: r = +i.
            {
                Hex hex = Hex.make(-i + j, -j, +i);
                assert (hex != null);
                Field field = new Field(FieldType.LAND);
                fields.put(hex, field);
            }
            for (int j = 0; j < i; ++j) // Bottom right side: r = -i.
            {
                Hex hex = Hex.make(+i - j, +j, -i);
                assert (hex != null);
                Field field = new Field(FieldType.LAND);
                fields.put(hex, field);
            }
        }
    }
    
    public void setCenter(Pixel newCenterOffset)
    {
        gridCenterOffset = newCenterOffset;
    }
    
    public void update()
    {
        OrthogonalDirection shift = inputHandler.getShiftingDirection();
        if (shift != null)
        {
            gridCenterOffset.add(shift.getOffset());
        }
        
        if (inputHandler.zoomIn())
        {
            scale = Math.min(scale + scaleChange, maxScale);
        }
        else if (inputHandler.zoomOut())
        {
            scale = Math.max(scale - scaleChange, minScale);
        }
    }
    
    public void draw(Graphics2D graphics)
    {
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
