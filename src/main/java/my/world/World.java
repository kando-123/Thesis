package my.world;

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
import my.player.Player;
import my.units.Field;
import my.units.FieldType;
import my.utils.DoubleDoublet;
import my.utils.Hex;
import my.utils.HexagonalDirection;
import my.utils.IntegerDoublet;
import my.utils.WeightedGenerator;

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

    private final int side;
    private final Map<Hex, Field> fields;

    public World(WorldConfiguration configuration)
    {
        side = configuration.worldSide;
        double seaPercentage = configuration.seaPercentage;
        double mountainsPercentage = configuration.mountainsPercentage;

        assert (side > 0 && side <= 100);
        assert (seaPercentage >= 0.00 && seaPercentage <= 1.00);
        assert (mountainsPercentage >= 0.00 && mountainsPercentage <= 1.00);

        int surface = Hex.getHexSurfaceSize(side);
        fields = new HashMap<>(surface);

        int westmostX = Hex.computeCornerPointAt(-side, 0, +side, HEX_OUTER_RADIUS, HEX_INNER_RADIUS).xCoord;
        int northmostY = Hex.computeCornerPointAt(0, -side, +side, HEX_OUTER_RADIUS, HEX_INNER_RADIUS).yCoord;

        IntegerDoublet offset = new IntegerDoublet(-westmostX, -northmostY);

        Map<Object, IntegerDoublet> centers = generateCenters(side, offset);

        generateSeaFields(seaPercentage, centers);
        generateMountsFields(mountainsPercentage, centers);
        generateLandFields(centers);

        predicates = makePredicatesMap();
    }

    public int getSide()
    {
        return side;
    }

    private void createFieldAt(FieldType type, Hex hex)
    {
        fields.put(hex, new Field(type, hex));
    }

    public Field getFieldAt(Hex hex)
    {
        return fields.get(hex);
    }

    private Map<Object, IntegerDoublet> generateCenters(int side, IntegerDoublet offset)
    {
        Map<Object, IntegerDoublet> centers = new HashMap<>(Hex.getHexSurfaceSize(side));
        Hex hex = Hex.getOrigin();
        centers.put(hex.clone(), hex.getCentralPoint(HEX_OUTER_RADIUS, HEX_INNER_RADIUS).plus(offset));
        for (int ring = 1; ring < side; ++ring)
        {
            hex.shift(HexagonalDirection.UP);
            Hex beginning = hex.clone();
            HexagonalDirection direction = HexagonalDirection.RIGHT_DOWN;
            do
            {
                centers.put(hex.clone(), hex.getCentralPoint(HEX_OUTER_RADIUS, HEX_INNER_RADIUS).plus(offset));
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

    private void generateSeaFields(double seaPercentage, Map<Object, IntegerDoublet> centers)
    {
        int westmostX = Hex.computeCornerPointAt(-side, 0, +side, HEX_OUTER_RADIUS, HEX_INNER_RADIUS).xCoord;
        int eastmostX = Hex.computeCornerPointAt(+side, 0, -side, HEX_OUTER_RADIUS, HEX_INNER_RADIUS).xCoord + HEX_WIDTH;
        int northmostY = Hex.computeCornerPointAt(0, -side, +side, HEX_OUTER_RADIUS, HEX_INNER_RADIUS).yCoord;
        int southmostY = Hex.computeCornerPointAt(0, +side, -side, HEX_OUTER_RADIUS, HEX_INNER_RADIUS).yCoord + HEX_HEIGHT;
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
                createFieldAt(FieldType.SEA, hex);
                keysForRemoval.add(hex);
            }
        }
        for (var key : keysForRemoval)
        {
            centers.remove(key);
        }
    }

    private void generateMountsFields(double mountsPercentage, Map<Object, IntegerDoublet> centers)
    {
        int westmostX = Hex.computeCornerPointAt(-side, 0, +side, HEX_OUTER_RADIUS, HEX_INNER_RADIUS).xCoord;
        int eastmostX = Hex.computeCornerPointAt(+side, 0, -side, HEX_OUTER_RADIUS, HEX_INNER_RADIUS).xCoord + HEX_WIDTH;
        int northmostY = Hex.computeCornerPointAt(0, -side, +side, HEX_OUTER_RADIUS, HEX_INNER_RADIUS).yCoord;
        int southmostY = Hex.computeCornerPointAt(0, +side, -side, HEX_OUTER_RADIUS, HEX_INNER_RADIUS).yCoord + HEX_HEIGHT;
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
                createFieldAt(FieldType.MOUNTAINS, hex);
                keysForRemoval.add(hex);
            }
        }
        for (var key : keysForRemoval)
        {
            centers.remove(key);
        }
    }

    private void generateLandFields(Map<Object, IntegerDoublet> centers)
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
                createFieldAt(generator.get(), hex);
            }
        }
        catch (WeightedGenerator.NonpositiveWeightException
               | WeightedGenerator.RepeatedElementException
               | WeightedGenerator.EmptyPoolException exc)
        {
            /* In practice, no exception will be thrown. */
        }
    }

    public void draw(Graphics2D graphics, DoubleDoublet centerOffset, double scale, Dimension panelSize)
    {
        var iterator = fields.entrySet().iterator();
        while (iterator.hasNext())
        {
            Map.Entry<Hex, Field> entry = iterator.next();

            Hex hex = entry.getKey();
            IntegerDoublet pixel = hex.getCornerPoint(HEX_OUTER_RADIUS, HEX_INNER_RADIUS);

            int x = (int) centerOffset.xCoord + (int) (pixel.xCoord * scale);
            int y = (int) centerOffset.yCoord + (int) (pixel.yCoord * scale);

            Field field = entry.getValue();

            int w = (int) (HEX_WIDTH * scale);
            int h = (int) (HEX_HEIGHT * scale);

            if (x + w >= 0 && x < panelSize.width && y + h >= 0 && y < panelSize.height)
            {
                field.draw(graphics, new IntegerDoublet(x, y), new Dimension(w, h));
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
            createFieldAt(FieldType.CAPITAL, capital);
        }

        return capitals;
    }

    private void splitHexes(HashSet<Hex> periphery,
                            HashSet<Hex> pool)
    {
        Set<Map.Entry<Hex, Field>> entries = fields.entrySet();
        for (var entry : entries)
        {
            var key = entry.getKey();
            var value = entry.getValue();

            if (value.getType().isPlains())
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
                    Field field = fields.get(neighbor);
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

   public Set<Field> markForPurchase(Player player, FieldType type)
    {
        Set<Field> markedFields = new HashSet<>();
        BinaryPredicate<Player, Field> predicate = predicates.get(type);
        if (predicate != null)
        {
            for (Field value : fields.values())
            {
                if (predicate.test(player, value))
                {
                    value.mark();
                    markedFields.add(value);
                }
            }
        }
        return markedFields;
    }
   
    public void unmarkAll()
    {
        for (Field value : fields.values())
        {
            value.unmark();
        }
    }
    
    private Map<FieldType, BinaryPredicate<Player, Field>> makePredicates()
    {
        Map<FieldType, BinaryPredicate<Player, Field>> map = new HashMap<>(FieldType.PURCHASABLES_COUNT);
        
        
        
        return map;
    }
    private Field[] getNeighboringFields(Field field)
    {
        Hex hex = field.getHex();
        int neighborsCount = (hex.getRing() < side - 1) ? 6 : (hex.isRadial()) ? 3 : 4;
        Field[] neighboringFields = new Field[neighborsCount];
        int i = 0;
        for (var neighbor : hex.neighbors())
        {
            Field neighboringField = getFieldAt(neighbor);
            if (neighboringField != null)
            {
                neighboringFields[i++] = neighboringField;
            }
        }
        return neighboringFields;
    }

    private static interface BinaryPredicate<L, R>
    {
        public boolean test(L left, R right);
    }

    private final Map<FieldType, BinaryPredicate<Player, Field>> predicates;

    private Map<FieldType, BinaryPredicate<Player, Field>> makePredicatesMap()
    {
        Map<FieldType, BinaryPredicate<Player, Field>> map = new HashMap<>(FieldType.PURCHASABLES_COUNT);

        map.put(FieldType.BARRACKS, (Player player, Field field) ->
        {
            return field.getOwner() == player && field.getType().isPlains();
        });

        map.put(FieldType.TOWN, (Player player, Field field) ->
        {
            return field.getOwner() == player && field.getType().isPlains();
        });

        map.put(FieldType.VILLAGE, (Player player, Field field) ->
        {
            return field.getOwner() == player && field.getType().isPlains();
        });
        
        map.put(FieldType.FARMFIELD, (Player player, Field field) ->
        {
            if (field.getOwner() != player || !field.getType().isPlains())
            {
                return false;
            }

            Field[] neighbors = getNeighboringFields(field);
            for (var neighbor : neighbors)
            {
                if (neighbor.getType() == FieldType.VILLAGE)
                {
                    return true;
                }
            }
            return false;
        });

        map.put(FieldType.FORTRESS, (Player player, Field field) ->
        {
            return field.getOwner() == player && field.getType().isContinental();
        });

        map.put(FieldType.MINE, (Player player, Field field) ->
        {
            return field.getOwner() == player && field.getType().isMountainous();
        });

        map.put(FieldType.SHIPYARD, (Player player, Field field) ->
        {
            if (field.getOwner() != player || !field.getType().isPlains())
            {
                return false;
            }

            Field[] neighbors = getNeighboringFields(field);
            for (var neighbor : neighbors)
            {
                if (neighbor.getType().isMarine())
                {
                    return true;
                }
            }
            return false;
        });

        return map;
    }

    public Set<FieldType> getBuildableProperties(Player player)
    {
        Set<FieldType> buildables = new HashSet<>();
        for (var value : FieldType.values())
        {
            if (value.isPurchasable())
            {
                var predicate = predicates.get(value);
                for (var field : player.getTerritory())
                {
                    if (predicate.test(player, field))
                    {
                        buildables.add(value);
                        break;
                    }
                }
            }
        }
        return buildables;
    }
    
    public void substitute(Field oldField, FieldType newType)
    {
        Hex hex = oldField.getHex();
        if (fields.get(hex) == oldField)
        {
            Field newField = new Field(newType, hex);
            newField.setOwner(oldField.getOwner());
            fields.put(hex, newField);
        }
        
    }
}
