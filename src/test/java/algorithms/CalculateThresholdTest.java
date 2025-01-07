package algorithms;

import ge.utilities.Doublet;
import ge.world.PerlinNoise;
import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Kay Jay O'Nail
 */
public class CalculateThresholdTest
{
    private static final double PRECISION = 0.01;
    private static final int TIERS_NUMBER = (int) (1.0 / PRECISION) + 1;

    private static double calculateThreshold(Map<Object, Double> noise, double percentage)
    {
        /* The i-th interval will store the number of entries whose value falls
           within the range from i*accuracy (incl.) to (i+1)*accuracy (excl.). */
        int[] counters = new int[TIERS_NUMBER];
        for (int i = 0; i < TIERS_NUMBER; ++i)
        {
            counters[i] = 0;
        }
        noise.forEach((key, value) ->
        {
            int tier = (int) (value / PRECISION);
            ++counters[tier];
        });

        int maximum = counters[0];
        for (int i = 1; i < counters.length; ++i)
        {
            if (counters[i] > maximum)
            {
                maximum = counters[i];
            }
        }

        final int minimalSum = (int) (percentage * noise.size());
        int currentSum = 0;
        double threshold = 0.0;
        for (int i = 0; i < TIERS_NUMBER; ++i)
        {
            currentSum += counters[i];
            if (currentSum >= minimalSum)
            {
                threshold = i * PRECISION;
                break;
            }
        }
        return threshold;
    }

    @Test
    public void testCalculateThreshold()
    {
        final int width = 1000;
        final int height = 1000;
        final int chunk = 10;
        
        var builder = new PerlinNoise.Builder();
        builder.setAreaWidth(width)
                .setAreaHeight(height)
                .setChunkSize(chunk)
                .setOctavesCount(3);
        var perlins = new PerlinNoise[10];
        for (int i = 0; i < perlins.length; ++i)
        {
            perlins[i] = builder.get();
        }

        for (var perlin : perlins)
        {
            var map = new HashMap<Object, Doublet<Integer>>();
            for (int c = 0; c < 100; ++c)
            {
                for (int r = 0; r < 100; ++r)
                {
                    var label = String.format("(%d,%d)", c, r);
                    map.put(label, new Doublet<>(c, r));
                }
            }
            var noise = perlin.makeNoise(map);

            double[] percentages = { .1, .2, .3, .4, .5, .6, .7, .8, .9 };
            double[] thresholds = new double[percentages.length];
            for (int i = 0; i < percentages.length; ++i)
            {
                thresholds[i] = calculateThreshold(noise, percentages[i]);
            }

            final double tolerance = 0.05;
            final double size = noise.size();
            for (int i = 0; i < percentages.length; ++i)
            {
                final double threshold = thresholds[i];
                int counter = 0;
                for (var n : noise.entrySet())
                {
                    if (n.getValue() < threshold)
                    {
                        ++counter;
                    }
                }
                final double expectedPercentage = percentages[i];
                final double actualPercentage = counter / size;
                final double error = Math.abs(expectedPercentage - actualPercentage);
                if (error > tolerance)
                {
                    fail("Error exceeds acceptable threshold.");
                }
            }
        }
    }
}
