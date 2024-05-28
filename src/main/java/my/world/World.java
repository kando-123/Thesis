package my.world;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import my.world.field.Field;
import my.world.field.FieldType;

/**
 *
 * @author Kay Jay O'Nail
 */
public class World
{
    public static final int HEX_OUTER_RADIUS = 40;
    public static final int HEX_INNER_RADIUS = (int) (HEX_OUTER_RADIUS * Math.sin(Math.PI / 3.0));
    public static final int HEX_WIDTH = 2 * HEX_OUTER_RADIUS;
    public static final int HEX_HEIGHT = (int) (2 * HEX_OUTER_RADIUS * Math.sin(Math.PI / 3.0));
    
    private static final double WOODS_PERCENTAGE = 0.10;
    
    private final int side;
    private final Map<Hex, Field> fields;
    
    public World(WorldParameters parameters)
    {
        /* Extract the fields of `parameters`. */
        side = parameters.worldSide;
        double seaPercentage = parameters.seaPercentage;
        double mountsPercentage = parameters.mountsPercentage;
        
        /* Assert they fall within correct ranges. */
        assert (side > 0 && side <= 100);
        assert (seaPercentage >= 0.00 && seaPercentage <= 1.00);
        assert (mountsPercentage >= 0.00 && mountsPercentage <= 1.00);
        
        /* Initialize `fields`. */
        int surface = Hex.getHexSurfaceSize(side);
        fields = new HashMap<>(surface);
        
        /* Compute dimensions of the map. */
        int westmostX = Hex.computeCornerPixelAt(-side, 0, +side, HEX_OUTER_RADIUS, HEX_INNER_RADIUS).xCoord;
        int northmostY = Hex.computeCornerPixelAt(0, -side, +side, HEX_OUTER_RADIUS, HEX_INNER_RADIUS).yCoord;

        Pixel offset = new Pixel(-westmostX, -northmostY);
        
        /* Generate the centers of all the fields (themselves not yet created). */
        Map<Object, Pixel> centers = generateCenters(side, offset);
        
        /* Generate the fields. */
        generateSeaFields(seaPercentage, centers);
        generateMountsFields(mountsPercentage, centers);
        generateWoodAndLandFields(centers);
    }
    
    private Map<Object, Pixel> generateCenters(int side, Pixel offset)
    {
        Map<Object, Pixel> centers = new HashMap<>(Hex.getHexSurfaceSize(side));
        Hex hex = Hex.getOrigin();
        centers.put(hex.clone(), hex.getCentralPixel(HEX_OUTER_RADIUS, HEX_INNER_RADIUS).plus(offset));
        for (int ring = 1; ring < side; ++ring)
        {
            hex.shift(HexagonalDirection.UP);
            Hex beginning = hex.clone();
            HexagonalDirection direction = HexagonalDirection.RIGHT_DOWN;
            do
            {
                centers.put(hex.clone(), hex.getCentralPixel(HEX_OUTER_RADIUS, HEX_INNER_RADIUS).plus(offset));
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
    
    private void generateSeaFields(double seaPercentage, Map<Object, Pixel> centers)
    {
        int westmostX = Hex.computeCornerPixelAt(-side, 0, +side, HEX_OUTER_RADIUS, HEX_INNER_RADIUS).xCoord;
        int eastmostX = Hex.computeCornerPixelAt(+side, 0, -side, HEX_OUTER_RADIUS, HEX_INNER_RADIUS).xCoord + HEX_WIDTH;
        int northmostY = Hex.computeCornerPixelAt(0, -side, +side, HEX_OUTER_RADIUS, HEX_INNER_RADIUS).yCoord;
        int southmostY = Hex.computeCornerPixelAt(0, +side, -side, HEX_OUTER_RADIUS, HEX_INNER_RADIUS).yCoord + HEX_HEIGHT;
        int areaWidth = eastmostX - westmostX;
        int areaHeight = southmostY - northmostY;
        
        int hexDimension = (int) (Math.hypot(HEX_WIDTH, HEX_HEIGHT) / Math.sqrt(2.0));
        
        int shorelineChunkSide = hexDimension * side / 3;
        PerlinNoise shorelinePerlin = new PerlinNoise(areaWidth, areaHeight, shorelineChunkSide);
        shorelinePerlin.setOctaves(3);

        Map<Object, Double> shorelineNoise = shorelinePerlin.makeNoise(centers);
        double seaThreshold = calculateThreshold(shorelineNoise, seaPercentage);
        
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
    }
    
    private void generateMountsFields(double mountsPercentage, Map<Object, Pixel> centers)
    {
        int westmostX = Hex.computeCornerPixelAt(-side, 0, +side, HEX_OUTER_RADIUS, HEX_INNER_RADIUS).xCoord;
        int eastmostX = Hex.computeCornerPixelAt(+side, 0, -side, HEX_OUTER_RADIUS, HEX_INNER_RADIUS).xCoord + HEX_WIDTH;
        int northmostY = Hex.computeCornerPixelAt(0, -side, +side, HEX_OUTER_RADIUS, HEX_INNER_RADIUS).yCoord;
        int southmostY = Hex.computeCornerPixelAt(0, +side, -side, HEX_OUTER_RADIUS, HEX_INNER_RADIUS).yCoord + HEX_HEIGHT;
        int areaWidth = eastmostX - westmostX;
        int areaHeight = southmostY - northmostY;
        
        int hexDimension = (int) (Math.hypot(HEX_WIDTH, HEX_HEIGHT) / Math.sqrt(2.0));
        
        int mountainsChunkSide = hexDimension * (int) Math.sqrt(side);
        PerlinNoise mountainsPerlin = new PerlinNoise(areaWidth, areaHeight, mountainsChunkSide);
        mountainsPerlin.setOctaves(7);

        Map<Object, Double> mountainsNoise = mountainsPerlin.makeNoise(centers);
        double mountainsThreshold = calculateThreshold(mountainsNoise, mountsPercentage);

        var iterator = mountainsNoise.entrySet().iterator();
        List<Hex> keysForRemoval = new ArrayList<>();
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
    }
    
    private void generateWoodAndLandFields(Map<Object, Pixel> centers)
    {
        int westmostX = Hex.computeCornerPixelAt(-side, 0, +side, HEX_OUTER_RADIUS, HEX_INNER_RADIUS).xCoord;
        int eastmostX = Hex.computeCornerPixelAt(+side, 0, -side, HEX_OUTER_RADIUS, HEX_INNER_RADIUS).xCoord + HEX_WIDTH;
        int northmostY = Hex.computeCornerPixelAt(0, -side, +side, HEX_OUTER_RADIUS, HEX_INNER_RADIUS).yCoord;
        int southmostY = Hex.computeCornerPixelAt(0, +side, -side, HEX_OUTER_RADIUS, HEX_INNER_RADIUS).yCoord + HEX_HEIGHT;
        int areaWidth = eastmostX - westmostX;
        int areaHeight = southmostY - northmostY;
        
        int hexDimension = (int) (Math.hypot(HEX_WIDTH, HEX_HEIGHT) / Math.sqrt(2.0));
        
        int woodsChunkSide = hexDimension * side / 7;
        PerlinNoise woodsPerlin = new PerlinNoise(areaWidth, areaHeight, woodsChunkSide);
        woodsPerlin.setOctaves(5);

        Map<Object, Double> woodsNoise = woodsPerlin.makeNoise(centers);
        double woodsThreshold = calculateThreshold(woodsNoise, WOODS_PERCENTAGE);

        var iterator = woodsNoise.entrySet().iterator();
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
    
    public void draw(Graphics2D graphics, Pixel offset, double scale, Dimension panelSize)
    {
        var iterator = fields.entrySet().iterator();
        while (iterator.hasNext())
        {
            Map.Entry<Hex, Field> entry = iterator.next();

            Hex hex = entry.getKey();
            Pixel pixel = hex.getCornerPixel(HEX_OUTER_RADIUS, HEX_INNER_RADIUS);

            int x = offset.xCoord + (int) (pixel.xCoord * scale);
            int y = offset.yCoord + (int) (pixel.yCoord * scale);

            Field field = entry.getValue();

            int w = (int) (HEX_WIDTH * scale);
            int h = (int) (HEX_HEIGHT * scale);

            if (x + w >= 0 && x < panelSize.width && y + h >= 0 && y < panelSize.height)
            {
                field.draw(graphics, new Pixel(x, y), new Dimension(w, h));
            }
        }
    }
}
