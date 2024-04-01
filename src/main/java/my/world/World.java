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

        List<Pixel> centers = new ArrayList<>(surface);
        Hex h = Hex.make(0, 0, 0);
        centers.add(h.getCentralPixel(hexOuterRadius, hexInnerRadius).plus(offset));
        for (int radius = 1; radius < side; ++radius)
        {
            //int p = 0, q = -radius, r = +radius;
            //int Δp = +1, Δq = 0, Δr = -1;
            h.shift(HexagonalDirection.UP);
            Hex beginning = h.clone();
            HexagonalDirection direction = HexagonalDirection.RIGHT_DOWN;
            do
            {
                /* Processing current hex. */
                System.out.println(String.format("Now being processed: %s", h.toString()));
                centers.add(h.getCentralPixel(hexOuterRadius, hexInnerRadius).plus(offset));

                /* Shifting the hex. */
                h.shift(direction);

                /* Changing the direction, if necessary. */
                if (h.getP() == 0 || h.getQ() == 0 || h.getR() == 0)
                {
                    direction = direction.next();
                }
//                p += Δp;
//                q += Δq;
//                r += Δr;
//                if (p == 0 || q == 0 || r == 0)
//                {
//                    if (p > 0)
//                    {
//                        --Δp;
//                    }
//                    else if (p < 0)
//                    {
//                        ++Δp;
//                    }
//                    if (q > 0)
//                    {
//                        --Δq;
//                    }
//                    else if (q < 0)
//                    {
//                        ++Δq;
//                    }
//                    if (r > 0)
//                    {
//                        --Δr;
//                    }
//                    else if (r < 0)
//                    {
//                        ++Δr;
//                    }
//            }
            }
            while (!h.equals(beginning));
        }

        PerlinNoise landscapePerlin = new PerlinNoise(areaWidth, areaHeight, chunkSide);

        landscapePerlin.setOctaves(5);
        List<Double> landscapeNoise = null;

        try
        {
            landscapeNoise = landscapePerlin.makeNoise(centers);
        }
        catch (Exception e)
        {

        }
        int counter = 0;
        fields = new HashMap<>(surface);
        {
            Hex hex = Hex.make(0, 0, 0);
            assert (hex != null);
            FieldType type = landscapeNoise.get(counter++) > 0.3
                    ? FieldType.LAND
                    : FieldType.SEE;
            Field field = new Field(type);
            fields.put(hex, field);
        }
        for (int i = 1;
                i < side;
                ++i)
        {
            for (int j = 0; j < i; ++j) // Right side: p = +i.
            {
                Hex hex = Hex.make(+i, -i + j, -j);
                assert (hex != null);
                FieldType type = landscapeNoise.get(counter++) > 0.3
                        ? FieldType.LAND
                        : FieldType.SEE;
                Field field = new Field(type);
                fields.put(hex, field);
            }
            for (int j = 0; j < i; ++j) // Left side: p = -i.
            {
                Hex hex = Hex.make(-i, +i - j, +j);
                assert (hex != null);
                FieldType type = landscapeNoise.get(counter++) > 0.3
                        ? FieldType.LAND
                        : FieldType.SEE;
                Field field = new Field(type);
                fields.put(hex, field);
            }
            for (int j = 0; j < i; ++j) // Bottom left side: q = +i.
            {
                Hex hex = Hex.make(-j, +i, -i + j);
                assert (hex != null);
                FieldType type = landscapeNoise.get(counter++) > 0.3
                        ? FieldType.LAND
                        : FieldType.SEE;
                Field field = new Field(type);
                fields.put(hex, field);
            }
            for (int j = 0; j < i; ++j) // Top right side: q = -i.
            {
                Hex hex = Hex.make(+j, -i, +i - j);
                assert (hex != null);
                FieldType type = landscapeNoise.get(counter++) > 0.3
                        ? FieldType.LAND
                        : FieldType.SEE;
                Field field = new Field(type);
                fields.put(hex, field);
            }
            for (int j = 0; j < i; ++j) // Top left side: r = +i.
            {
                Hex hex = Hex.make(-i + j, -j, +i);
                assert (hex != null);
                FieldType type = landscapeNoise.get(counter++) > 0.3
                        ? FieldType.LAND
                        : FieldType.SEE;
                Field field = new Field(type);
                fields.put(hex, field);
            }
            for (int j = 0; j < i; ++j) // Bottom right side: r = -i.
            {
                Hex hex = Hex.make(+i - j, +j, -i);
                assert (hex != null);
                FieldType type = landscapeNoise.get(counter++) > 0.3
                        ? FieldType.LAND
                        : FieldType.SEE;
                Field field = new Field(type);
                fields.put(hex, field);
            }
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
