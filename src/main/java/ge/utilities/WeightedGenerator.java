package ge.utilities;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 *
 * @param <E>
 * @author Kay Jay O'Nail
 */
public class WeightedGenerator<E>
{
    private final Map<E, Integer> elements;
    private int aggregatedWeights;
    private final Random random;

    public WeightedGenerator()
    {
        elements = new HashMap<>();
        aggregatedWeights = 0;
        random = new Random();
    }

    public static class NonpositiveWeightException extends RuntimeException
    {
        public NonpositiveWeightException()
        {
            super("Nonpositive weight was passed.");
        }
    }
    
    public static class RepeatedElementException extends RuntimeException
    {
        public RepeatedElementException()
        {
            super("Cannot add the same element more than once.");
        }
    }

    public void add(E element, int weight)
    {
        if (elements.containsKey(element))
        {
            throw new RepeatedElementException();
        }
        else if (!(weight > 0))
        {
            throw new NonpositiveWeightException();
        }
        else
        {
            elements.put(element, weight);
            aggregatedWeights += weight;
        }
    }

    public static class EmptyPoolException extends RuntimeException
    {
        public EmptyPoolException()
        {
            super("Cannot pick an element from an empty pool.");
        }
    }

    public E get()
    {
        if (elements.isEmpty())
        {
            throw new EmptyPoolException();
        }
        else
        {
            int index = random.nextInt(0, aggregatedWeights);
            int sum = 0;
            for (Map.Entry<E, Integer> entry : elements.entrySet())
            {
                var element = entry.getKey();
                var weight = entry.getValue();
                
                sum += weight;
                if (index < sum)
                {
                    return element;
                }
            }
            return null;
        }
    }
}
