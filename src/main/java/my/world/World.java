package my.world;

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
    
    private final Map<Hex, Field> fields;
    
    private int hexSurface(int side)
    {
        return 3 * side * (side - 1) + 1;
    }
    
    public World(WorldParameters parameters)
    {
        int side = parameters.worldSide;
        double seaPercentage = parameters.seaPercentage;
        double mountsPercentage = parameters.mountsPercentage;
        
        assert (side >= 0 && side <= 100);
        assert (seaPercentage >= 0.00 && seaPercentage <= 1.00);
        assert (mountsPercentage >= 0.00 && mountsPercentage <= 1.00);
        
        int surface = hexSurface(side);
        int westmostX = Hex.getCornerPixelAt(-side, 0, +side, HEX_OUTER_RADIUS, HEX_INNER_RADIUS).xCoord;
        int eastmostX = Hex.getCornerPixelAt(+side, 0, -side, HEX_OUTER_RADIUS, HEX_INNER_RADIUS).xCoord + HEX_WIDTH;
        int northmostY = Hex.getCornerPixelAt(0, -side, +side, HEX_OUTER_RADIUS, HEX_INNER_RADIUS).yCoord;
        int southmostY = Hex.getCornerPixelAt(0, +side, -side, HEX_OUTER_RADIUS, HEX_INNER_RADIUS).yCoord + HEX_HEIGHT;
        
        Pixel offset = new Pixel(eastmostX, southmostY);
        Map<Object, Pixel> centers = generateCenters(side, offset);
        
        int areaWidth = eastmostX - westmostX;
        int areaHeight = southmostY - northmostY;
        
        int hexSize = (int) (Math.hypot(HEX_WIDTH, HEX_HEIGHT) / Math.sqrt(2.0));
        
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
        double seaThreshold = calculateThreshold(shorelineNoise, seaPercentage);
        
        var iterator = shorelineNoise.entrySet().iterator();
        List<Hex> keysForRemoval = new ArrayList<>();
        fields = new HashMap<>(surface);
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
    
    private Map<Object, Pixel> generateCenters(int side, Pixel offset)
    {
        Map<Object, Pixel> centers = new HashMap<>(hexSurface(side));
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
}
