package algorithms;

import ge.utilities.Doublet;
import ge.world.PerlinNoise;
import java.lang.reflect.Method;
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
    @Test
    public void testCalculateThreshold()
    {
        try
        {
            Class klass = Class.forName("ge.world.World");
            Method calculateThreshold = klass.getDeclaredMethod("calculateThreshold",
                    Map.class, double.class);
            calculateThreshold.setAccessible(true);

            final int trials = 10;
            int[] widths = new int[trials];
            int[] heights = new int[trials];
            var perlins = new PerlinNoise[trials];

            for (int i = 0; i < trials; ++i)
            {
                widths[i] = 200 + i * 50;
                heights[i] = 100 + i * 75;
                final int chunk = 10 + i * 5;

                var builder = new PerlinNoise.Builder();
                builder.setAreaWidth(widths[i])
                        .setAreaHeight(heights[i])
                        .setChunkSize(chunk)
                        .setOctavesCount(3);
                perlins[i] = builder.get();
            }

            for (int t = 0; t < trials; ++t)
            {
                var perlin = perlins[t];

                var map = new HashMap<Object, Doublet<Integer>>();
                for (int c = 0; c < widths[t]; ++c)
                {
                    for (int r = 0; r < heights[t]; ++r)
                    {
                        map.put(new Object(), new Doublet<>(c, r));
                    }
                }
                var noise = perlin.makeNoise(map);

                double[] percentages =
                {
                    .1, .2, .3, .4, .5, .6, .7, .8, .9
                };
                double[] thresholds = new double[percentages.length];
                for (int i = 0; i < percentages.length; ++i)
                {
                    thresholds[i] = (Double) calculateThreshold.invoke(null,
                            noise, percentages[i]);
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
        catch (Exception e)
        {

        }

    }
}
