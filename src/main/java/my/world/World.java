package my.world;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Stack;
import my.player.*;
import my.world.field.*;

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

    public World(WorldConfiguration parameters)
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

    public int getSide()
    {
        return side;
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

        int shorelineChunkSide = hexDimension * side / 5;
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
                Field field = new Field(FieldType.SEA);
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

    public void draw(Graphics2D graphics, Point offset, double scale, Dimension panelSize)
    {
        var iterator = fields.entrySet().iterator();
        while (iterator.hasNext())
        {
            Map.Entry<Hex, Field> entry = iterator.next();

            Hex hex = entry.getKey();
            Pixel pixel = hex.getCornerPixel(HEX_OUTER_RADIUS, HEX_INNER_RADIUS);

            int x = (int) offset.xCoord + (int) (pixel.xCoord * scale);
            int y = (int) offset.yCoord + (int) (pixel.yCoord * scale);

            Field field = entry.getValue();

            int w = (int) (HEX_WIDTH * scale);
            int h = (int) (HEX_HEIGHT * scale);

            if (x + w >= 0 && x < panelSize.width && y + h >= 0 && y < panelSize.height)
            {
                field.draw(graphics, new Pixel(x, y), new Dimension(w, h));
            }
        }
    }

    private class Region
    {
        private final HashSet<Hex> territory;
        private final HashSet<Hex> origins;
        private HashSet<Hex> periphery;

        public Region(HashSet<Hex> root)
        {
            territory = new HashSet<>();
            origins = new HashSet<>();
            periphery = new HashSet<>();

            territory.addAll(root);
            origins.addAll(root);
            periphery.addAll(root);
        }

        public int size()
        {
            return territory.size();
        }

        public void propagate(Map<Hex, Integer> map, Set<Hex> alreadyTaken)
        {
            HashSet<Hex> acquisitions = new HashSet<>();
            
            for (var peripheral : periphery)
            {
                int value = map.get(peripheral);
                
                for (var neighbor : peripheral.neighbors())
                {
                    if (territory.contains(neighbor) || alreadyTaken.contains(neighbor) // One is unnecessary...
                        || !map.containsKey(neighbor))
                    {
                        continue;
                    }
                    
                    if (map.get(neighbor) <= value)
                    {
                        territory.add(neighbor);
                        acquisitions.add(neighbor);
                        
                        alreadyTaken.add(neighbor);
                    }
                }
            }
            
            periphery = acquisitions;
        }
        
        public void capture(AbstractPlayer owner)
        {
            for (var hex : territory)
            {
                if (origins.contains(hex))
                {
                    fields.put(hex, new Field(FieldType.CAPITAL));
                }
                fields.get(hex).capture(owner);
            }
        }
    }

    public List<Hex> locateCapitals(int number)
    {
        List<Hex> capitalHexes = new ArrayList<>(number);

        /* Discard the seas and the mounts. Divide the lands and woods into the periphery
           and the pool. */
        HashSet<Hex> periphery = new HashSet<>();
        HashSet<Hex> pool = new HashSet<>();

        Set<Map.Entry<Hex, Field>> entries = fields.entrySet();
        for (var entry : entries)
        {
            var key = entry.getKey();
            var value = entry.getValue();

            if (value.getType() == FieldType.LAND || value.getType() == FieldType.WOOD)
            {
                boolean isPeripheral = false;

                for (var neighbor : key.neighbors())
                {
                    if (!fields.containsKey(neighbor))
                    {
                        isPeripheral = true;
                        break;
                    }
                    else
                    {
                        FieldType type = fields.get(neighbor).getType();
                        if (type == FieldType.SEA || type == FieldType.MOUNTS)
                        {
                            isPeripheral = true;
                            break;
                        }
                    }
                }

                if (isPeripheral)
                {
                    periphery.add(key);
                }
                else
                {
                    pool.add(key);
                }
            }
        }

        /* Compute the inlandness for the periphery and prepare the next layer. */
        HashMap<Hex, Integer> inlandness = new HashMap<>();
        HashSet<Hex> landwardLayer = new HashSet<>();
        for (var hex : periphery)
        {
            int mountNeighbors = 0;
            int seaNeighbors = 0;
            int nullNeighbors = 0;
            for (var neighbor : hex.neighbors())
            {
                if (!fields.containsKey(neighbor))
                {
                    ++nullNeighbors;
                }
                else
                {
                    Field field = fields.get(neighbor);
                    switch (field.getType())
                    {
                        case SEA ->
                        {
                            ++seaNeighbors;
                        }
                        case MOUNTS ->
                        {
                            ++mountNeighbors;
                        }
                        case LAND, WOOD ->
                        {
                            if (pool.contains(neighbor))
                            {
                                pool.remove(neighbor);
                                landwardLayer.add(neighbor);
                            }
                        }
                    }
                }
            }
            if (seaNeighbors + mountNeighbors + nullNeighbors < 6)
            {
                int value = 6 - seaNeighbors - (mountNeighbors + nullNeighbors + 1) / 2;
                inlandness.put(hex, value);
            }
        }

        /* Propagate landwards. */
        while (!landwardLayer.isEmpty())
        {
            HashSet<Hex> previousLayer = periphery;
            periphery = landwardLayer;
            landwardLayer = new HashSet<>();

            for (var hex : periphery)
            {
                int maximum = 0;
                for (var neighbor : hex.neighbors())
                {
                    if (previousLayer.contains(neighbor))
                    {
                        maximum = Math.max(inlandness.get(neighbor), maximum);
                    }
                    else if (pool.contains(neighbor))
                    {
                        pool.remove(neighbor);
                        landwardLayer.add(neighbor);
                    }
                }
                inlandness.put(hex, maximum + 1);
            }
        }

        /* Find candidates for local maxima. */
        LinkedList<Hex> maxima = new LinkedList<>();

        for (var entry : inlandness.entrySet())
        {
            var key = entry.getKey();
            var value = entry.getValue();

            boolean isMaximal = true;
            for (var neighbor : key.neighbors())
            {
                if (inlandness.containsKey(neighbor) && inlandness.get(neighbor) > value)
                {
                    isMaximal = false;
                    break;
                }
            }

            if (isMaximal)
            {
                maxima.add(key);
            }
        }

        /* Filter the apparent maxima out. */
        HashSet<Hex> apparentMaxima = new HashSet<>();
        for (var candidate : maxima)
        {
            if (apparentMaxima.contains(candidate))
            {
                continue;
            }
            
            int candidateValue = inlandness.get(candidate);
            for (var neighbor : candidate.neighbors())
            {
                if (inlandness.containsKey(neighbor))
                {
                    int neighborValue = inlandness.get(neighbor);
                    if (neighborValue == candidateValue && !maxima.contains(neighbor))
                    {
                        // This candidate and all candidates joint to it must be removed
                        Stack<Hex> stack = new Stack<>();
                        
                        apparentMaxima.add(candidate);
                        stack.push(candidate);
                        while (!stack.isEmpty())
                        {
                            Hex peek = stack.peek();
                            
                            Hex next = null;
                            for (var further : peek.neighbors())
                            {
                                if (maxima.contains(further) && !apparentMaxima.contains(further))
                                {
                                    next = further;
                                    break;
                                }
                            }
                            if (next != null)
                            {
                                apparentMaxima.add(next);
                                stack.push(next);
                            }
                            else
                            {
                                stack.pop();
                            }
                        }
                    }
                }
            }
        }
        maxima.removeAll(apparentMaxima);

        /* Initialize the regions. */
        List<Region> regions = new ArrayList<>();
        HashSet<Hex> takenArea = new HashSet<>();
        while (!maxima.isEmpty())
        {
            /* Find a whole connected group using DFS */
            HashSet<Hex> group = new HashSet<>();
            Stack<Hex> stack = new Stack<>();

            Hex origin = maxima.pop();
            group.add(origin);
            stack.push(origin);

            while (!stack.isEmpty())
            {
                Hex peek = stack.peek();

                Hex next = null;
                for (var neighbor : peek.neighbors())
                {
                    if (maxima.contains(neighbor))
                    {
                        next = neighbor;
                        break;
                    }
                }
                if (next != null)
                {
                    maxima.remove(next);
                    group.add(next);
                    stack.push(next);
                }
                else
                {
                    stack.pop();
                }
            }
            Region region = new Region(group);
            regions.add(region);
            
            takenArea.addAll(group);
        }
        
        /* Propagate seawards. */
        
        while (takenArea.size() < inlandness.size())
        {
            for (var region : regions)
            {
                region.propagate(inlandness, takenArea);
            }
        }
        
                AbstractPlayer[] owners = new AbstractPlayer[7];
                for (int i = 0; i < 7; ++i)
                {
                    owners[i] = new UserPlayer();
                    owners[i].setColor(PlayerColor.values()[i + 1]);
                }

                Random random = new Random();
                for (var region : regions)
                {
                    region.capture(owners[random.nextInt(0, 7)]);
                }
        
        /* Select the largest `number` regions
           and spawn the capitals in their focal points. */
        
        return capitalHexes;
    }
}

//for (var entry : inlandness.entrySet())
//{
//    var key = entry.getKey();
//    var value = entry.getValue();
//
//    if (value <= 3)
//    {
//        fields.get(key).capture(owners[0]);
//    }
//    else if (value > 3 && value < 9)
//    {
//        fields.get(key).capture(owners[value - 3]);
//    }
//    else
//    {
//        fields.get(key).capture(owners[6]);
//    }
//}
