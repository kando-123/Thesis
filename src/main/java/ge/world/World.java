package ge.world;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.stream.*;
import ge.utilities.*;

/**
 *
 * @author Kay Jay O'Nail
 */
public class World
{
    public static final int HEX_OUTER_RADIUS = 40;
    public static final int HEX_INNER_RADIUS = (int) Hex.computeInnerRadius(HEX_OUTER_RADIUS);
    public static final int HEX_WIDTH = 2 * HEX_OUTER_RADIUS;
    public static final int HEX_HEIGHT = (int) Hex.computeInnerRadius(2 * HEX_OUTER_RADIUS);

    private final int side;
    private final Map<Hex, AbstractField> fields;

    public World(WorldConfig config)
    {
        side = config.worldSide;

        assert (side > 0 && side <= 100);
        assert (config.seaPercentage >= 0.00 && config.seaPercentage <= 1.00);
        assert (config.mountainsPercentage >= 0.00 && config.mountainsPercentage <= 1.00);

        int surface = Hex.computeHexSurfaceSize(side);
        fields = new HashMap<>(surface);

        int westmostX = Hex.computeCornerPointAt(-side, 0, +side, HEX_OUTER_RADIUS, HEX_INNER_RADIUS).left;
        int northmostY = Hex.computeCornerPointAt(0, -side, +side, HEX_OUTER_RADIUS, HEX_INNER_RADIUS).right;

        Doublet<Integer> offset = new Doublet<>(-westmostX, -northmostY);

        Map<Object, Doublet<Integer>> centers = generateCenters(side, offset);

        generateSeaFields(config.seaPercentage, centers);
        generateMountainFields(config.mountainsPercentage, centers);
        generateLandFields(centers);

//        markedFields = new HashSet<>();
    }

    public int getSide()
    {
        return side;
    }

    private void placeField(Hex hex, AbstractField field)
    {
        field.setHex(hex);
        fields.put(hex, field);
    }

    public AbstractField getFieldAt(Hex hex)
    {
        return fields.get(hex);
    }

    private Map<Object, Doublet<Integer>> generateCenters(int side, Doublet<Integer> offset)
    {
        Map<Object, Doublet<Integer>> centers = new HashMap<>(Hex.computeHexSurfaceSize(side));
        Hex hex = Hex.getOrigin();
        Doublet<Integer> center = hex.getCentralPoint(HEX_OUTER_RADIUS, HEX_INNER_RADIUS);
        center.left += offset.left;
        center.right += offset.right;
        centers.put(hex.clone(), center);
        for (int ring = 1; ring < side; ++ring)
        {
            Hex beginning = hex;
            hex = hex.neighbor(HexagonalDirection.UP);
            HexagonalDirection direction = HexagonalDirection.RIGHT_DOWN;
            do
            {
                center = hex.getCentralPoint(HEX_OUTER_RADIUS, HEX_INNER_RADIUS);
                center.left += offset.left;
                center.right += offset.right;
                centers.put(hex.clone(), center);
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
    private static final int TIERS_COUNT = (int) (1.0 / ACCURACY) + 1;

    private double calculateThreshold(Map<Object, Double> noise, double percentage)
    {
        /* The i-th interval will store the number of entries whose value falls
           within the range from i*accuracy (incl.) to (i+1)*accuracy (excl.). */
        List<Integer> noiseIntervals = new ArrayList<>(TIERS_COUNT);
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

    private void generateSeaFields(double percentage, Map<Object, Doublet<Integer>> centers)
    {
        int westmostX = Hex.computeCornerPointAt(-side, 0, +side, HEX_OUTER_RADIUS, HEX_INNER_RADIUS).left;
        int eastmostX = Hex.computeCornerPointAt(+side, 0, -side, HEX_OUTER_RADIUS, HEX_INNER_RADIUS).left + HEX_WIDTH;
        int northmostY = Hex.computeCornerPointAt(0, -side, +side, HEX_OUTER_RADIUS, HEX_INNER_RADIUS).right;
        int southmostY = Hex.computeCornerPointAt(0, +side, -side, HEX_OUTER_RADIUS, HEX_INNER_RADIUS).right + HEX_HEIGHT;
        int areaWidth = eastmostX - westmostX;
        int areaHeight = southmostY - northmostY;

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

        Map<Object, Double> noise = perlin.makeNoise(centers);
        double threshold = calculateThreshold(noise, percentage);

        var iterator = noise.entrySet().iterator();
        List<Hex> keysForRemoval = new ArrayList<>();
        while (iterator.hasNext())
        {
            var entry = iterator.next();
            Hex hex = (Hex) entry.getKey();
            double value = entry.getValue();
            if (value < threshold)
            {
                placeField(hex, new SeaField());
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
        int westmostX = Hex.computeCornerPointAt(-side, 0, +side, HEX_OUTER_RADIUS, HEX_INNER_RADIUS).left;
        int eastmostX = Hex.computeCornerPointAt(+side, 0, -side, HEX_OUTER_RADIUS, HEX_INNER_RADIUS).left + HEX_WIDTH;
        int northmostY = Hex.computeCornerPointAt(0, -side, +side, HEX_OUTER_RADIUS, HEX_INNER_RADIUS).right;
        int southmostY = Hex.computeCornerPointAt(0, +side, -side, HEX_OUTER_RADIUS, HEX_INNER_RADIUS).right + HEX_HEIGHT;
        int areaWidth = eastmostX - westmostX;
        int areaHeight = southmostY - northmostY;

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
            Hex hex = (Hex) entry.getKey();
            double value = entry.getValue();
            if (value < threshold)
            {
                placeField(hex, new MountainsField());
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
        WeightedGenerator<FieldType> generator = new WeightedGenerator<>();
        try
        {
            generator.add(FieldType.GRASS, 2);
            generator.add(FieldType.MEADOW, 2);
            generator.add(FieldType.WOOD, 1);

            var iterator = centers.entrySet().iterator();
            while (iterator.hasNext())
            {
                var entry = iterator.next();
                Hex hex = (Hex) entry.getKey();
                placeField(hex, switch (generator.get())
                {
                    case GRASS ->
                    {
                        yield new GrassField();
                    }
                    case MEADOW ->
                    {
                        yield new MeadowField();
                    }
                    case WOOD ->
                    {
                        yield new WoodField();
                    }
                    default ->
                    {
                        yield null; // never happens
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

    public void draw(Graphics2D graphics, Doublet<Double> centerOffset, double scale, Dimension panelSize)
    {
        // If both entries have entities, that which is more to the right will be drawn later.
        // If o1 has and entity and o2 does not, o1 will be drawn later.
        // If o1 has no entity and o2 does, o1 will be drawn before o2.
        // If both have no entities, they relative order is irrelevant;
        // since the comparison should be symmetric, 0 is returned.
        var entries = fields.entrySet()
                .stream()
                .sorted((Map.Entry<Hex, AbstractField> o1, Map.Entry<Hex, AbstractField> o2) ->
                {
                    boolean e1 = o1.getValue().hasEntity();
                    boolean e2 = o2.getValue().hasEntity();
                    int p1 = o1.getKey().getP();
                    int p2 = o2.getKey().getP();

                    return e1 ? (e2 ? p1 - p2 : +1) : (e2 ? -1 : 0);
                })
                .collect(Collectors.toList());
        for (var entry : entries)
        {
            Hex hex = entry.getKey();
            Doublet<Integer> pixel = hex.getCornerPoint(HEX_OUTER_RADIUS, HEX_INNER_RADIUS);

            int x = centerOffset.left.intValue() + (int) (pixel.left * scale);
            int y = centerOffset.right.intValue() + (int) (pixel.right * scale);

            AbstractField field = entry.getValue();

            int w = (int) (HEX_WIDTH * scale);
            int h = (int) (HEX_HEIGHT * scale);

            if (x + w >= 0 && x < panelSize.width && y + h >= 0 && y < panelSize.height)
            {
                field.draw(graphics, new Doublet<>(x, y), new Dimension(w, h));
            }
        }
    }

    private static class Region
    {
        private final HashSet<Hex> territory;
        private final ArrayList<Hex> origins;
        private HashSet<Hex> periphery;

        public Region(HashSet<Hex> root)
        {
            assert (!root.isEmpty());

            territory = new HashSet<>();
            origins = new ArrayList<>();
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

        public Hex locateCapital()
        {
            int p = 0;
            int q = 0;

            for (var hex : origins)
            {
                p += hex.getP();
                q += hex.getQ();
            }

            p = (int) ((double) p / (double) origins.size());
            q = (int) ((double) q / (double) origins.size());

            Hex average = Hex.newInstance(p, q, -(p + q));

            Hex capital = origins.get(0);
            int minimalDistance = average.distance(capital);

            for (int i = 0; i < origins.size(); ++i)
            {
                Hex origin = origins.get(i);
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

    public Hex[] locateCapitals(int number)
    {
        /* Discard the seas and the mounts. Divide the lands and woods into the periphery
           and the pool. */
        HashSet<Hex> periphery = new HashSet<>();
        HashSet<Hex> pool = new HashSet<>();
        splitHexes(periphery, pool);

        /* Compute the inlandness for the periphery and prepare the next layer. */
        HashMap<Hex, Integer> inlandness = new HashMap<>();
        HashSet<Hex> landwardLayer = new HashSet<>();
        processShore(periphery, pool, landwardLayer, inlandness);

        /* Propagate landwards. */
        processLand(periphery, pool, landwardLayer, inlandness);

        /* Find candidates for local maxima. */
        LinkedList<Hex> maxima = findMaximumCandidates(inlandness);

        /* Filter the apparent maxima out. */
        removeApparentMaxima(maxima, inlandness);

        /* Initialize the regions. */
        HashSet<Hex> takenArea = new HashSet<>();
        List<Region> regions = initRegions(maxima, takenArea);

        /* Propagate seawards. */
        propagateRegions(regions, inlandness, takenArea);

        /* Find the capital candidates */
        Hex[] capitalCandidates = getCapitalCandidates(number + MANIPULATION_MARGIN, regions);

        /* Find the max-weight combination */
        Hex[] capitals = findMaxWeightCombination(capitalCandidates, number);

        /* Create the capital fields. */
        for (var capital : capitals)
        {
            placeField(capital, new CapitalField());
        }

        return capitals;
    }

    private void splitHexes(HashSet<Hex> periphery,
                            HashSet<Hex> pool)
    {
        Set<Map.Entry<Hex, AbstractField>> entries = fields.entrySet();
        for (var entry : entries)
        {
            var key = entry.getKey();
            var value = entry.getValue();

            if (value.isPlains())
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
                        if (type == FieldType.SEA || type == FieldType.MOUNTAINS)
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

    private void processShore(HashSet<Hex> periphery,
                              HashSet<Hex> pool,
                              HashSet<Hex> landwardLayer,
                              HashMap<Hex, Integer> inlandness)
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
                    AbstractField field = fields.get(neighbor);
                    switch (field.getType())
                    {
                        case SEA ->
                        {
                            ++seaNeighbors;
                        }
                        case MOUNTAINS ->
                        {
                            ++mountNeighbors;
                        }
                        case GRASS, MEADOW, WOOD ->
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
    }

    private void processLand(HashSet<Hex> periphery,
                             HashSet<Hex> pool,
                             HashSet<Hex> landwardLayer,
                             HashMap<Hex, Integer> inlandness)
    {
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
    }

    private LinkedList<Hex> findMaximumCandidates(HashMap<Hex, Integer> inlandness)
    {
        LinkedList<Hex> candidates = new LinkedList<>();

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

    private void removeApparentMaxima(List<Hex> maxima,
                                      HashMap<Hex, Integer> inlandness)
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

    private List<Region> initRegions(LinkedList<Hex> maxima,
                                     HashSet<Hex> takenArea)
    {
        List<Region> regions = new ArrayList<>();
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
        return regions;
    }

    private void propagateRegions(List<Region> regions,
                                  HashMap<Hex, Integer> inlandness,
                                  HashSet<Hex> takenArea)
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

    private Hex[] getCapitalCandidates(int count,
                                       java.util.List<Region> regions)
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

    private Hex[] findMaxWeightCombination(Hex[] candidates,
                                           int combinationSize)
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

    private static boolean[][] generateAllCombinationMasks(int poolLength,
                                                           int combinationLength)
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

    private int computeAggregateMaskedDistance(Hex[] hexes,
                                               boolean[] mask)
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

//    public void substitute(AbstractField oldField, AbstractField newField)
//    {
//        Hex hex = oldField.getHex();
//        if (fields.get(hex) == oldField)
//        {
//            newField.moveProperties(oldField);
//            fields.put(hex, newField);
//        }
//    }

//    private final HashSet<AbstractField> markedFields;
//
//    public void mark(Hex hex)
//    {
//        AbstractField field = fields.get(hex);
//        if (field != null)
//        {
//            field.mark();
//            markedFields.add(field);
//        }
//    }
//
//    public void unmark(Hex hex)
//    {
//        AbstractField field = fields.get(hex);
//        if (field != null)
//        {
//            field.unmark();
//            markedFields.remove(field);
//        }
//    }
//
//    public void unmarkAll()
//    {
//        for (AbstractField field : markedFields)
//        {
//            field.unmark();
//        }
//        markedFields.clear();
//    }
//
//    public boolean isMarked(Hex hex)
//    {
//        AbstractField field = fields.get(hex);
//        return markedFields.contains(field);
//    }

//    public WorldMarker createMarker()
//    {
//        return new WorldMarker(this);
//    }
//
//    public WorldAccessor createAccessor()
//    {
//        return new WorldAccessor(this);
//    }
//
//    public WorldMutator createMutator()
//    {
//        return new WorldMutator(this);
//    }
}
