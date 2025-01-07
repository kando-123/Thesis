package ge.utilities;

import java.util.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Kay Jay O'Nail
 */
public class WeightedGeneratorTest
{
    @BeforeAll
    public static void setUpClass()
    {
    }
    
    @AfterAll
    public static void tearDownClass()
    {
    }
    
    @BeforeEach
    public void setUp()
    {
    }
    
    @AfterEach
    public void tearDown()
    {
    }

    /**
     * Test of add method, of class WeightedGenerator.
     */
    @Test
    public void testAdd()
    {
        System.out.println("add");
        
        var instance = new WeightedGenerator();
        
        /* Nonpositive weights. */
        try
        {
            instance.add("A", 0);
            fail("Exception shall have been thrown: nonpositive weight.");
            
            instance.add("B", -1);
            fail("Exception shall have been thrown: nonpositive weight.");
        }
        catch (WeightedGenerator.NonpositiveWeightException e)
        {}
        
        try
        {
            instance.add("C", 1);
            instance.add("D", 4);
            instance.add("E", 5);
        }
        catch (WeightedGenerator.NonpositiveWeightException
                | WeightedGenerator.RepeatedElementException e)
        {
            fail("Exception shall not have been thrown.");
        }
        
        /* Repeated values */
        try
        {
            instance.add("C", 1);
            instance.add("D", 2);
            instance.add("E", 3);
            
            fail("Exception shall have been thrown: repeated values.");
        }
        catch (WeightedGenerator.RepeatedElementException e)
        {}
    }

    /**
     * Test of get method, of class WeightedGenerator.
     */
    @Test
    public void testGet()
    {
        var instance = new WeightedGenerator();
        
        /* Empty pool */
        try
        {
            instance.get();
            
            fail("Exception shall have been thrown: empty pool.");
        }
        catch (WeightedGenerator.EmptyPoolException e)
        {}
        
        var elements = new HashMap<Object, Integer>();
        elements.put("A", 1);
        elements.put("B", 3);
        elements.put("C", 6);
        int sum = 0;
        
        var counters = new HashMap<Object, Integer>();
        
        for (var e : elements.entrySet())
        {
            instance.add(e.getKey(), e.getValue());
            counters.put(e.getKey(), 0);
            
            sum += e.getValue();
        }
        
        final int length = 10_000;
        for (int i = 0; i < length; ++i)
        {
            var element = instance.get();
            
            assertNotEquals(null, element);
            
            int counter = counters.get(element);
            counters.put(element, ++counter);
        }
        
        final float tolerance = 0.01f; //   +/- 1%
        for (var e : elements.entrySet())
        {
            var element = e.getKey();
            var weight = e.getValue();
            var counter = counters.get(element);
            final float theoreticalFrequency = (float) weight / sum;
            final float actualFrequency = (float) counter / length;
            final float error = Math.abs(theoreticalFrequency - actualFrequency);
            if (error > tolerance)
            {
                fail("Too great error.");
            }
        }
    }
}
