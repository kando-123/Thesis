/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package my.world;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import my.world.field.*;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.*;
import javax.swing.JPanel;
import my.input.InputHandler;

/**
 *
 * @author Kay Jay O'Nail
 */
public class WorldPanel extends JPanel
{
    public final int hexOuterRadius;
    public final int hexInnerRadius;
    public final int hexWidth;
    public final int hexHeight;
    public int gridSide;

    private int panelWidth;
    private int panelHeight;
    private Pixel centerOffset;
    private double scale;
    private final double scaleChange;
    private final double maxScale;
    private final double minScale;

    private Map<Hex, Field> fields;

    private final InputHandler inputHandler;

    private int hexSurface(int side)
    {
        return 3 * side * (side - 1) + 1;
    }

    public WorldPanel()
    {
        hexOuterRadius = 40;
        hexInnerRadius = (int) ((double) hexOuterRadius * Math.sin(Math.PI / 3.0));
        hexWidth = 2 * hexOuterRadius;
        hexHeight = 2 * hexInnerRadius;

        scale = 1.0;
        scaleChange = 0.01;
        maxScale = 2.5;
        minScale = 0.25;

        inputHandler = InputHandler.getInstance();
        FieldManager.getInstance();

        fields = new HashMap<>();
    }

    public void makeWorld(Dimension panelSize)
    {
        panelWidth = panelSize.width;
        panelHeight = panelSize.height;
        centerOffset = new Pixel(panelWidth / 2, panelHeight / 2);

        int side = 10;
        int surface = hexSurface(side);

        int westmostX = Hex.getCornerPixelOf(-side, 0, +side, hexOuterRadius, hexInnerRadius).xCoord;
        int eastmostX = Hex.getCornerPixelOf(+side, 0, -side, hexOuterRadius, hexInnerRadius).xCoord + hexWidth;
        int northmostY = Hex.getCornerPixelOf(0, -side, +side, hexOuterRadius, hexInnerRadius).yCoord;
        int southmostY = Hex.getCornerPixelOf(0, +side, -side, hexOuterRadius, hexInnerRadius).yCoord + hexHeight;

        Pixel offset = new Pixel(eastmostX, southmostY);

        Map<Object, Pixel> centers = new HashMap<>(surface);
        Hex hex = Hex.getOrigin();
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

        int areaWidth = eastmostX - westmostX;
        int areaHeight = southmostY - northmostY;

        int hexSize = (int) Math.sqrt((double) (hexWidth * hexWidth + hexHeight * hexHeight) / 2.0);

        int shorelineChunkSide = hexSize * side / 3;
        PerlinNoise shorelinePerlin = new PerlinNoise(areaWidth, areaHeight, shorelineChunkSide);
        shorelinePerlin.setOctaves(3);

        int mountainsChunkSide = hexSize * (int) Math.sqrt(side);
        PerlinNoise mountainsPerlin = new PerlinNoise(areaWidth, areaHeight, mountainsChunkSide);
        mountainsPerlin.setOctaves(7);

        int woodsChunkSide = hexSize * side / 7;
        PerlinNoise woodsPerlin = new PerlinNoise(areaWidth, areaHeight, woodsChunkSide);
        woodsPerlin.setOctaves(5);

        try
        {
            Map<Object, Double> shorelineNoise = shorelinePerlin.makeNoise(centers);
            Map<Object, Double> mountainsNoise = mountainsPerlin.makeNoise(centers);
            Map<Object, Double> woodsNoise = woodsPerlin.makeNoise(centers);

            var iterator = shorelineNoise.entrySet().iterator();
            while (iterator.hasNext())
            {
                var entry = iterator.next();
                hex = (Hex) entry.getKey();
                double heightASL = entry.getValue();
                FieldType type;
                if (heightASL > 0.25)
                {
                    double mountainsValue = mountainsNoise.get(hex);
                    double woodValue = woodsNoise.get(hex);
                    if (mountainsValue > 0.75)
                    {
                        type = FieldType.MOUNTS;
                    }
                    else
                    {
                        if (woodValue > 0.5)
                        {
                            type = FieldType.WOOD;
                        }
                        else
                        {
                            type = FieldType.LAND;
                        }
                    }
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

    @Override
    public void paintComponent(Graphics graphics)
    //public void draw(Graphics2D graphics)
    {
        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setBackground(Color.black);
        graphics2D.clearRect(0, 0, panelWidth, panelHeight);

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

            if (x + w >= 0 && x < panelWidth && y + h >= 0 && y < panelHeight)
            {
                graphics2D.drawImage(image, x, y, w, h, null);
            }
        }
    }
}
