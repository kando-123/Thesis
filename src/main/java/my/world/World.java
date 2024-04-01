/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package my.world;

import java.awt.Dimension;
import my.world.field.*;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    public int gridSide;

    private Pixel centerOffset;
    private double scale;
    private final double scaleChange;
    private final double maxScale;
    private final double minScale;

    private final int screenWidth;
    private final int screenHeight;

    private Map<Hex, Field> fields;

    private final InputHandler inputHandler;

    private int hexSurface(int side)
    {
        return 3 * side * (side - 1) + 1;
    }

    public World(Dimension screenSize)
    {
        hexOuterRadius = 40;
        hexInnerRadius = (int) ((double) hexOuterRadius * Math.sin(Math.PI / 3.0));
        hexWidth = 2 * hexOuterRadius;
        hexHeight = 2 * hexInnerRadius;

        scale = 1.0;
        scaleChange = 0.01;
        maxScale = 2.5;
        minScale = 0.25;

        screenWidth = screenSize.width;
        screenHeight = screenSize.height;
        centerOffset = new Pixel(screenWidth / 2, screenHeight / 2);

        inputHandler = InputHandler.getInstance();
        FieldManager.getInstance();

        fields = new HashMap<>();
    }

    public void makeWorld()
    {
        int side = 10;
        int surface = hexSurface(side);

        int westmostX = Hex.getCornerPixelOf(-side, 0, +side, hexOuterRadius, hexInnerRadius).xCoord;
        int eastmostX = Hex.getCornerPixelOf(+side, 0, -side, hexOuterRadius, hexInnerRadius).xCoord + hexWidth;
        int northmostY = Hex.getCornerPixelOf(0, -side, +side, hexOuterRadius, hexInnerRadius).yCoord;
        int southmostY = Hex.getCornerPixelOf(0, +side, -side, hexOuterRadius, hexInnerRadius).yCoord + hexHeight;
        int areaWidth = eastmostX - westmostX;
        int areaHeight = southmostY - northmostY;
        int chunkSide = (hexOuterRadius + hexInnerRadius) / 2 * 4;
        Pixel offset = new Pixel(eastmostX, southmostY);

        Map<Object, Pixel> centers = new HashMap<>(surface);
        Hex hex = Hex.make(0, 0, 0);
        centers.put(hex.clone(), hex.getCentralPixel(hexOuterRadius, hexInnerRadius).plus(offset));
        for (int ring = 1; ring < side; ++ring)
        {
            hex.shift(HexagonalDirection.UP);
            Hex beginning = hex.clone();
            HexagonalDirection direction = HexagonalDirection.RIGHT_DOWN;
            do
            {
                centers.put(hex.clone(), hex.getCentralPixel(hexOuterRadius, hexInnerRadius).plus(offset));
                hex.shift(direction);
                if (hex.isRadial())
                {
                    direction = direction.next();
                }
            }
            while (!hex.equals(beginning));
        }

        PerlinNoise shorelinePerlin = new PerlinNoise(areaWidth, areaHeight, chunkSide);
        PerlinNoise woodsPerlin = new PerlinNoise(areaWidth, areaHeight, chunkSide);

        shorelinePerlin.setOctaves(5);
        Map<Object, Double> shorelineNoise = null;
        Map<Object, Double> woodsNoise = null;

        try
        {
            shorelineNoise = shorelinePerlin.makeNoise(centers);
            woodsNoise = woodsPerlin.makeNoise(centers);

            var iterator = shorelineNoise.entrySet().iterator();
            while (iterator.hasNext())
            {
                var entry = iterator.next();
                hex = (Hex) entry.getKey();
                double heightASL = entry.getValue();
                FieldType type;
                if (heightASL > 0.7)
                {
                    type = FieldType.MOUNTS;
                }
                else if (heightASL > 0.4)
                {
                    double wood = woodsNoise.get(hex);
                    type = wood > 0.5 ? FieldType.WOOD : FieldType.LAND;
                }
                else
                {
                    type = FieldType.SEE;
                }
                Field field = new Field(type);
                fields.put(hex, field);
            }
        }
        catch (Exception e)
        {
            System.err.println(e);
        }
    }

    public void update()
    {
        OrthogonalDirection shift = inputHandler.getShiftingDirection();
        if (shift != null)
        {
            centerOffset.add(shift.getOffset());
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
            BufferedImage image = field.getImage();

            int x = centerOffset.xCoord + (int) ((double) pixel.xCoord * scale);
            int y = centerOffset.yCoord + (int) ((double) pixel.yCoord * scale);

            int w = (int) ((double) hexWidth * scale);
            int h = (int) ((double) hexHeight * scale);

            if (x + w >= 0 && x < screenWidth && y + h >= 0 && y < screenHeight)
            {
                graphics.drawImage(image, x, y, w, h, null);
            }
        }
    }
}
