package ge.world;

import ge.field.*;
import ge.main.*;
import ge.utilities.*;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.*;
import java.util.stream.*;

/**
 *
 * @author Kay Jay O'Nail
 */
public class World
{
    public static final int HEX_OUTER_RADIUS;
    public static final int HEX_INNER_RADIUS;
    public static final int HEX_WIDTH;
    public static final int HEX_HEIGHT;

    static
    {
        HEX_OUTER_RADIUS = 40;
        HEX_INNER_RADIUS = (int) Hex.innerRadius(HEX_OUTER_RADIUS);
        HEX_WIDTH = 2 * HEX_OUTER_RADIUS;
        HEX_HEIGHT = (int) Hex.innerRadius(HEX_WIDTH);
    }

    public final int side;
    private final Map<Hex, Field> fields;

    public World(WorldConfig config)
    {
        assert (config.worldSide > 0 && config.worldSide <= 50);
        assert (config.seaPercentage >= 0.00 && config.seaPercentage <= 1.00);
        assert (config.mountainsPercentage >= 0.00 && config.mountainsPercentage <= 1.00);

        side = config.worldSide;
        fields = new HashMap<>(Hex.surfaceSize(side));

        int westmostX = Hex.cornerPointAt(-side, 0, +side, HEX_OUTER_RADIUS, HEX_INNER_RADIUS).x;
        int northmostY = Hex.cornerPointAt(0, -side, +side, HEX_OUTER_RADIUS, HEX_INNER_RADIUS).y;

        var offset = new Doublet<Integer>(-westmostX, -northmostY);

        Map<Object, Doublet<Integer>> centers = generateCenters(side, offset);

        generateSeaFields(config.seaPercentage, centers);
        generateMountainFields(config.mountainsPercentage, centers);
        generateLandFields(centers);
    }
    
    public WorldScanner scanner()
    {
        return new WorldScanner(this);
    }
    
    public WorldAccessor accessor()
    {
        return new WorldAccessor(this);
    }
    
    public WorldRenderer renderer()
    {
        return new WorldRenderer(this);
    }
    
    public Field getField(Hex coords)
    {
        return fields.get(coords);
    }
    
    public Stream<Field> fieldStream()
    {
        return fields.values().stream();
    }
    
    public void substitute(Field newField) throws WrongCoordsException
    {
        var coords = newField.getHex();
        if (fields.containsKey(coords))
        {
            var old = fields.put(coords, newField);
            if (old.isOccupied())
            {
                newField.setEntity(old.takeEntity());
            }
        }
        else
        {
            throw new WrongCoordsException();
        }
    }
    
    public static class WrongCoordsException extends RuntimeException
    {
    }

    private Map<Object, Doublet<Integer>> generateCenters(int side, Doublet<Integer> offset)
    {
        Map<Object, Doublet<Integer>> centers = new HashMap<>(Hex.surfaceSize(side));
        var hex = Hex.getOrigin();
        var center = hex.centralPoint(HEX_OUTER_RADIUS, HEX_INNER_RADIUS);
        center.x += offset.x;
        center.y += offset.y;
        centers.put(hex.clone(), center);
        for (int ring = 1; ring < side; ++ring)
        {
            final Hex beginning = hex = hex.neighbor(Hex.Direction.UP);
            var direction = Hex.Direction.RIGHT_DOWN;
            do
            {
                center = hex.centralPoint(HEX_OUTER_RADIUS, HEX_INNER_RADIUS);
                center.x += offset.x;
                center.y += offset.y;
                centers.put(hex, center);
                hex = hex.neighbor(direction);
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
    private static final int TIERS_NUMBER = (int) (1.0 / ACCURACY) + 1;

    private double calculateThreshold(Map<Object, Double> noise, double percentage)
    {
        /* The i-th interval will store the number of entries whose value falls
           within the range from i*accuracy (incl.) to (i+1)*accuracy (excl.). */
        List<Integer> noiseIntervals = new ArrayList<>(TIERS_NUMBER);
        for (int i = 0; i < TIERS_NUMBER; ++i)
        {
            noiseIntervals.add(0);
        }
        noise.forEach((key, value) ->
        {
            int tier = (int) (value / ACCURACY);
            noiseIntervals.set(tier, noiseIntervals.get(tier) + 1);
        });
        final int minimalSum = (int) (percentage * (double) noise.size());
        int currentSum = 0;
        double threshold = 0.0;
        for (int i = 0; i < TIERS_NUMBER; ++i)
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

    private void generateSeaFields(double percentage, Map<Object, Doublet<Integer>> centers)
    {
        int areaWidth = Hex.surfaceWidth(side, HEX_OUTER_RADIUS);
        int areaHeight = Hex.surfaceHeight(side, HEX_INNER_RADIUS);

        int hexDimension = (int) (Math.hypot(HEX_WIDTH, HEX_HEIGHT) / Math.sqrt(2.0));

        int chunkSize = hexDimension * side / 5;
        var builder = new PerlinNoise.Builder();
        try
        {
            builder.setAreaWidth(areaWidth)
                    .setAreaHeight(areaHeight)
                    .setChunkSize(chunkSize)
                    .setOctavesCount(3);
        }
        catch (PerlinNoise.Builder.WrongValueException vve)
        {
            assert (false);
        }
        PerlinNoise perlin = builder.get();

        assert (perlin != null);

        Map<Object, Double> noise = perlin.makeNoise(centers);
        double threshold = calculateThreshold(noise, percentage);

        var iterator = noise.entrySet().iterator();
        List<Hex> keysForRemoval = new ArrayList<>();
        while (iterator.hasNext())
        {
            var entry = iterator.next();
            var hex = (Hex) entry.getKey();
            double value = entry.getValue();
            if (value < threshold)
            {
                fields.put(hex, new SeaField(hex));
                keysForRemoval.add(hex);
            }
        }
        for (var key : keysForRemoval)
        {
            centers.remove(key);
        }
    }

    private void generateMountainFields(double percentage, Map<Object, Doublet<Integer>> centers)
    {
        int areaWidth = Hex.surfaceWidth(side, HEX_OUTER_RADIUS);
        int areaHeight = Hex.surfaceHeight(side, HEX_INNER_RADIUS);

        int hexDimension = (int) (Math.hypot(HEX_WIDTH, HEX_HEIGHT) / Math.sqrt(2.0));

        int chunkSide = hexDimension * (int) Math.sqrt(side);
        var builder = new PerlinNoise.Builder();
        try
        {
            builder.setAreaWidth(areaWidth)
                    .setAreaHeight(areaHeight)
                    .setChunkSize(chunkSide)
                    .setOctavesCount(7);
        }
        catch (PerlinNoise.Builder.WrongValueException vve)
        {
            assert (false);
        }
        PerlinNoise perlin = builder.get();

        assert (perlin != null);

        Map<Object, Double> noise = perlin.makeNoise(centers);
        double threshold = calculateThreshold(noise, percentage);

        var iterator = noise.entrySet().iterator();
        List<Hex> keysForRemoval = new ArrayList<>();
        while (iterator.hasNext())
        {
            var entry = iterator.next();
            var hex = (Hex) entry.getKey();
            var value = entry.getValue();
            if (value < threshold)
            {
                fields.put(hex, new MountainsField(hex));
                keysForRemoval.add(hex);
            }
        }
        for (var key : keysForRemoval)
        {
            centers.remove(key);
        }
    }

    private void generateLandFields(Map<Object, Doublet<Integer>> centers)
    {
        enum Type
        {
            GRASS,
            MEADOW,
            WOOD;
        }
        var generator = new WeightedGenerator<Type>();
        try
        {
            generator.add(Type.GRASS, 2);
            generator.add(Type.MEADOW, 2);
            generator.add(Type.WOOD, 1);

            var iterator = centers.entrySet().iterator();
            while (iterator.hasNext())
            {
                var entry = iterator.next();
                var hex = (Hex) entry.getKey();
                fields.put(hex, switch (generator.get())
                {
                    case GRASS ->
                    {
                        yield new GrassField(hex);
                    }
                    case MEADOW ->
                    {
                        yield new MeadowField(hex);
                    }
                    case WOOD ->
                    {
                        yield new WoodField(hex);
                    }
                });
            }
        }
        catch (WeightedGenerator.NonpositiveWeightException
               | WeightedGenerator.RepeatedElementException
               | WeightedGenerator.EmptyPoolException exc)
        {
            /* In practice, no exception will be thrown. */
        }
    }

    void draw(Graphics2D graphics, Doublet<Double> center, double scale, Dimension size)
    {
        // If both entries have entities, that which is more to the y will be drawn later.
        // If o1 has and entity and o2 does not, o1 will be drawn later.
        // If o1 has no entity and o2 does, o1 will be drawn before o2.
        // If both have no entities, they relative order is irrelevant;
        // since the comparison should be symmetric, 0 is returned.
        var entries = fields.entrySet()
                .stream()
                .sorted((o1, o2) ->
                {
                    boolean e1 = o1.getValue().isOccupied();
                    boolean e2 = o2.getValue().isOccupied();
                    int p1 = o1.getKey().getP();
                    int p2 = o2.getKey().getP();
                    return e1 ? (e2 ? p1 - p2 : +1) : (e2 ? -1 : 0);
                })
                .collect(Collectors.toList());
        for (var entry : entries)
        {
            Hex hex = entry.getKey();
            Doublet<Integer> pixel = hex.cornerPoint(HEX_OUTER_RADIUS, HEX_INNER_RADIUS);

            int x = (int) (center.x + scale * pixel.x);
            int y = (int) (center.y + scale * pixel.y);

            var field = entry.getValue();

            int w = (int) (HEX_WIDTH * scale);
            int h = (int) (HEX_HEIGHT * scale);

            if (x + w >= 0 && x < size.width && y + h >= 0 && y < size.height)
            {
                field.draw(graphics, x, y, w, h);
            }
        }
    }

    private static class Region
    {
        private final Set<Hex> territory;
        private final List<Hex> core;
        private Set<Hex> periphery;

        Region(Set<Hex> root)
        {
            assert (!root.isEmpty());

            territory = new HashSet<>();
            core = new ArrayList<>();
            periphery = new HashSet<>();

            territory.addAll(root);
            core.addAll(root);
            periphery.addAll(root);
        }

        int size()
        {
            return territory.size();
        }

        void propagate(Map<Hex, Integer> map, Set<Hex> alreadyTaken)
        {
            var acquisitions = new HashSet<Hex>();
            for (var peripheral : periphery)
            {
                int value = map.get(peripheral);
                for (var neighbor : peripheral.neighbors())
                {
                    if (territory.contains(neighbor) || alreadyTaken.contains(neighbor) // One is unnecessary. (?)
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

        Hex locateCapital()
        {
            int p = 0;
            int q = 0;

            for (var hex : core)
            {
                p += hex.getP();
                q += hex.getQ();
            }

            p = (int) ((double) p / (double) core.size());
            q = (int) ((double) q / (double) core.size());

            Hex average = Hex.newInstance(p, q, -(p + q));

            Hex capital = core.get(0);
            int minimalDistance = average.distance(capital);

            for (int i = 0; i < core.size(); ++i)
            {
                Hex origin = core.get(i);
                int distance = average.distance(origin);
                if (distance < minimalDistance)
                {
                    capital = origin;
                    minimalDistance = distance;
                }
            }

            return capital;
        }
    }

    private final static int MANIPULATION_MARGIN = 4;

    public Hex[] locateCapitals(int number, Invoker<GameplayManager> invoker)
    {
        /* Discard the seas and the mounts. Divide the lands and woods into the periphery
           and the pool. */
        var periphery = new HashSet<Hex>();
        var pool = new HashSet<Hex>();
        splitHexes(periphery, pool);

        /* Compute the inlandness for the periphery and prepare the next layer. */
        var inlandness = new HashMap<Hex, Integer>();
        var landwardLayer = new HashSet<Hex>();
        processShore(periphery, pool, landwardLayer, inlandness);

        /* Propagate landwards. */
        processLand(periphery, pool, landwardLayer, inlandness);

        /* Find candidates for local maxima. */
        List<Hex> maxima = findMaximumCandidates(inlandness);

        /* Filter the apparent maxima out. */
        removeApparentMaxima(maxima, inlandness);

        /* Initialize the regions. */
        HashSet<Hex> takenArea = new HashSet<>();
        List<Region> regions = initRegions(maxima, takenArea);

        /* Propagate seawards. */
        propagateRegions(regions, inlandness, takenArea);

        /* Find the capital candidates */
        Hex[] capitalCandidates = getCapitalCandidates(Math.min(number + MANIPULATION_MARGIN, regions.size()), regions);

        /* Find the max-weight combination */
        Hex[] capitals = findMaxWeightCombination(capitalCandidates, number);

        /* Create the capital fields. */
        for (var capital : capitals)
        {
            fields.put(capital, new CapitalField(capital, invoker));
        }

        return capitals;
    }

    private void splitHexes(Set<Hex> periphery, Set<Hex> pool)
    {
        Set<Map.Entry<Hex, Field>> entries = fields.entrySet();
        for (var entry : entries)
        {
            var key = entry.getKey();
            var value = entry.getValue();

            if (value instanceof PlainsField)
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
                        var field = fields.get(neighbor);
                        if (field instanceof SeaField || field instanceof MountainsField)
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
    }

    private void processShore(Set<Hex> periphery, Set<Hex> pool, Set<Hex> landwardLayer, Map<Hex, Integer> inlandness)
    {
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
                    if (field instanceof SeaField)
                    {
                        ++seaNeighbors;
                    }
                    else if (field instanceof MountainsField)
                    {
                        ++mountNeighbors;
                    }
                    else if (field instanceof PlainsField && pool.contains(neighbor))
                    {
                        pool.remove(neighbor);
                        landwardLayer.add(neighbor);
                    }
                }
            }
            if (seaNeighbors + mountNeighbors + nullNeighbors < 6)
            {
                int value = 6 - seaNeighbors - (mountNeighbors + nullNeighbors + 1) / 2;
                inlandness.put(hex, value);
            }
        }
    }

    private void processLand(Set<Hex> periphery, Set<Hex> pool, Set<Hex> landwardLayer, Map<Hex, Integer> inlandness)
    {
        while (!landwardLayer.isEmpty())
        {
            Set<Hex> previousLayer = periphery;
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
    }

    private List<Hex> findMaximumCandidates(Map<Hex, Integer> inlandness)
    {
        List<Hex> candidates = new LinkedList<>();

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
                candidates.add(key);
            }
        }

        return candidates;
    }

    private void removeApparentMaxima(List<Hex> maxima, Map<Hex, Integer> inlandness)
    {
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
    }

    private List<Region> initRegions(List<Hex> maxima, Set<Hex> takenArea)
    {
        List<Region> regions = new ArrayList<>();
        while (!maxima.isEmpty())
        {
            /* Find a whole connected group using DFS */
            var group = new HashSet<Hex>();
            var stack = new Stack<Hex>();

            Hex origin = maxima.removeFirst();
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
        return regions;
    }

    private void propagateRegions(List<Region> regions, Map<Hex, Integer> inlandness, Set<Hex> takenArea)
    {
        while (takenArea.size() < inlandness.size())
        {
            /* During propagation, give priority to the smaller regions. */
            regions.sort((reg1, reg2) -> reg1.size() - reg2.size());

            for (var region : regions)
            {
                region.propagate(inlandness, takenArea);
            }
        }
    }

    private Hex[] getCapitalCandidates(int count, List<Region> regions)
    {
        /* Prefer larger regions. */
        regions.sort((reg1, reg2) -> reg2.size() - reg1.size());

        Hex[] capitalCandidates = new Hex[count];

        for (int i = 0; i < count; ++i)
        {
            Hex capital = regions.get(i).locateCapital();
            capitalCandidates[i] = capital;
        }

        return capitalCandidates;
    }

    private Hex[] findMaxWeightCombination(Hex[] candidates, int combinationSize)
    {
        int poolSize = candidates.length;

        boolean[][] masks = generateAllCombinationMasks(poolSize, combinationSize);

        int maximumIndex = 0;
        int maximumValue = computeAggregateMaskedDistance(candidates, masks[0]);

        for (int i = 1; i < masks.length; ++i)
        {
            int value = computeAggregateMaskedDistance(candidates, masks[i]);
            if (value > maximumValue)
            {
                maximumIndex = i;
                maximumValue = value;
            }
        }

        Hex[] combination = new Hex[combinationSize];
        boolean[] mask = masks[maximumIndex];
        for (int i = 0, j = 0; i < poolSize; ++i)
        {
            if (mask[i])
            {
                combination[j++] = candidates[i];
            }
        }

        return combination;
    }

    private static boolean[][] generateAllCombinationMasks(int poolLength, int combinationLength)
    {
        int numberOfAllCombinations = binomialCoefficient(poolLength, combinationLength);
        boolean[][] masks = new boolean[numberOfAllCombinations][poolLength];

        int[] indexes = new int[combinationLength];

        for (int i = 0; i < combinationLength; ++i)
        {
            indexes[i] = poolLength - combinationLength + i;
        }

        for (int j = 0; j < combinationLength; ++j)
        {
            masks[0][indexes[j]] = true;
        }

        for (int i = 1; i < numberOfAllCombinations; ++i)
        {
            if (indexes[0] > 0)
            {
                --indexes[0];
            }
            else
            {
                int j = 1;
                while (indexes[j] == j)
                {
                    ++j;
                }

                --indexes[j];
                while (--j >= 0)
                {
                    indexes[j] = indexes[j + 1] - 1;
                }
            }

            for (int j = 0; j < combinationLength; ++j)
            {
                masks[i][indexes[j]] = true;
            }
        }

        return masks;
    }

    private static int binomialCoefficient(int n, int k)
    {
        assert (n >= k);

        int product = 1;
        for (int i = n; i > k; --i)
        {
            product *= i;
        }
        for (int i = n - k; i > 1; --i)
        {
            product /= i;
        }

        return product;
    }

    private int computeAggregateMaskedDistance(Hex[] hexes, boolean[] mask)
    {
        int sum = 0;

        for (int i = 0; i < hexes.length; ++i)
        {
            if (mask[i])
            {
                for (int j = i + 1; j < hexes.length; ++j)
                {
                    if (mask[j])
                    {
                        sum += hexes[i].distance(hexes[j]);
                    }
                }
            }
        }

        return sum;
    }
}
