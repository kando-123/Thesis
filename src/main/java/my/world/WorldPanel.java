package my.world;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JPanel;
import my.input.*;
import my.world.field.*;

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

    private final Map<Hex, Field> fields;

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
        scaleChange = 1.01;
        maxScale = 2.5;
        minScale = 0.25;

        inputHandler = InputHandler.getInstance();
        FieldManager.getInstance();

        fields = new HashMap<>();
    }
    
    private static final double WOODS_PERCENTAGE = 0.10;

    public void makeWorld(Dimension panelSize, WorldParameters parameters)
    {
        assert (parameters.worldSide >= 10 && parameters.worldSide <= 30);
        assert (parameters.seaPercentage >= 0.20 && parameters.seaPercentage <= 0.70);
        assert (parameters.mountsPercentage >= 0.10 && parameters.mountsPercentage <= 0.40);

        panelWidth = panelSize.width;
        panelHeight = panelSize.height;
        centerOffset = new Pixel(panelWidth / 2, panelHeight / 2);

        int side = parameters.worldSide;

        int westmostX = Hex.getCornerPixelOf(-side, 0, +side, hexOuterRadius, hexInnerRadius).xCoord;
        int eastmostX = Hex.getCornerPixelOf(+side, 0, -side, hexOuterRadius, hexInnerRadius).xCoord + hexWidth;
        int northmostY = Hex.getCornerPixelOf(0, -side, +side, hexOuterRadius, hexInnerRadius).yCoord;
        int southmostY = Hex.getCornerPixelOf(0, +side, -side, hexOuterRadius, hexInnerRadius).yCoord + hexHeight;

        Pixel offset = new Pixel(eastmostX, southmostY);
        Map<Object, Pixel> centers = getCenters(side, offset);

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

        Map<Object, Double> shorelineNoise = shorelinePerlin.makeNoise(centers);
        double seaThreshold = calculateThreshold(shorelineNoise, parameters.seaPercentage);

        var iterator = shorelineNoise.entrySet().iterator();
        List<Hex> keysForRemoval = new ArrayList<>();
        while (iterator.hasNext())
        {
            var entry = iterator.next();
            Hex hex = (Hex) entry.getKey();
            double noise = entry.getValue();
            if (noise < seaThreshold)
            {
                Field field = new Field(FieldType.SEE);
                fields.put(hex, field);
                keysForRemoval.add(hex);
            }
        }
        for (var key : keysForRemoval)
        {
            centers.remove(key);
        }

        Map<Object, Double> mountainsNoise = mountainsPerlin.makeNoise(centers);
        double mountainsThreshold = calculateThreshold(mountainsNoise, parameters.mountsPercentage);

        iterator = mountainsNoise.entrySet().iterator();
        keysForRemoval = new ArrayList<>();
        while (iterator.hasNext())
        {
            var entry = iterator.next();
            Hex hex = (Hex) entry.getKey();
            double noise = entry.getValue();
            if (noise < mountainsThreshold)
            {
                Field field = new Field(FieldType.MOUNTS);
                fields.put(hex, field);
                keysForRemoval.add(hex);
            }
        }
        for (var key : keysForRemoval)
        {
            centers.remove(key);
        }

        Map<Object, Double> woodsNoise = woodsPerlin.makeNoise(centers);
        double woodsThreshold = calculateThreshold(woodsNoise, WOODS_PERCENTAGE);

        iterator = woodsNoise.entrySet().iterator();
        while (iterator.hasNext())
        {
            var entry = iterator.next();
            Hex hex = (Hex) entry.getKey();
            double noise = entry.getValue();
            if (noise < woodsThreshold)
            {
                Field field = new Field(FieldType.WOOD);
                fields.put(hex, field);
            }
            else
            {
                Field field = new Field(FieldType.LAND);
                fields.put(hex, field);
            }
        }
    }

    private Map<Object, Pixel> getCenters(int side, Pixel offset)
    {
        Map<Object, Pixel> centers = new HashMap<>(hexSurface(side));
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
        return centers;
    }

    private static final double ACCURACY = 0.05;
    private static final int TIERS_COUNT = (int) (1.0 / ACCURACY) + 1;

    private double calculateThreshold(Map<Object, Double> noise, double percentage)
    {
        /* The i-th interval will store the number of entries whose value falls
           within the range from i*accuracy (incl.) to (i+1)*accuracy (excl.). */
        List<Integer> noiseIntervals = new ArrayList(TIERS_COUNT);
        for (int i = 0; i < TIERS_COUNT; ++i)
        {
            noiseIntervals.add(0);
        }
        noise.forEach((Object key, Double value) ->
        {
            int tier = (int) (value / ACCURACY);
            noiseIntervals.set(tier, noiseIntervals.get(tier) + 1);
        }
        );
        final int minimalSum = (int) (percentage * (double) noise.size());
        int currentSum = 0;
        double threshold = 0.0;
        for (int i = 0; i < TIERS_COUNT; ++i)
        {
            currentSum += noiseIntervals.get(i);
            if (currentSum >= minimalSum)
            {
                threshold = (double) (i + 1) * ACCURACY;
                break;
            }
        }
        return threshold;
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
            scale = Math.min(scale * scaleChange, maxScale);
        }
        else if (inputHandler.zoomOut())
        {
            scale = Math.max(scale / scaleChange, minScale);
        }
    }

    @Override
    public void paintComponent(Graphics graphics)
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
