package my.utils;

//import org.junit.jupiter.api.AfterEach;

import java.util.HashMap;
import java.util.Map;

//import org.junit.jupiter.api.AfterAll;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Test;
//import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Kay Jay O'Nail
 */
public class WeightedGeneratorTest
{
//    public WeightedGeneratorTest()
//    {
//    }
//
//    @org.junit.jupiter.api.BeforeAll
//    public static void setUpClass() throws Exception
//    {
//    }
//
//    @org.junit.jupiter.api.AfterAll
//    public static void tearDownClass() throws Exception
//    {
//    }
//
//    @org.junit.jupiter.api.BeforeEach
//    public void setUp() throws Exception
//    {
//    }
//
//    @org.junit.jupiter.api.AfterEach
//    public void tearDown() throws Exception
//    {
//    }
//
//    @BeforeAll
//    public static void setUpClass()
//    {
//    }
//
//    @AfterAll
//    public static void tearDownClass()
//    {
//    }
//
//    @BeforeEach
//    public void setUp()
//    {
//    }
//
//    @AfterEach
//    public void tearDown()
//    {
//    }
//
//    /**
//     * Test of add method, of class WeightedGenerator.
//     */
//    @org.junit.jupiter.api.Test
//    public void testAdd() throws Exception
//    {
//        System.out.println("add");
//        Object element = null;
//        int weight = 0;
//        WeightedGenerator instance = new WeightedGenerator();
//        instance.add(element, weight);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of get method, of class WeightedGenerator.
//     */
//    @org.junit.jupiter.api.Test
//    public void testGet() throws Exception
//    {
//        System.out.println("get");
//        WeightedGenerator instance = new WeightedGenerator();
//        Object expResult = null;
//        Object result = instance.get();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
}

//WeightedGenerator<Character> generator = new WeightedGenerator<>();
//try
//{
//    generator.add('A', 3);
//    generator.add('B', 4);
//    generator.add('C', 5);
//    generator.add('D', 2);
//    generator.add('E', 1);
//
//    Map<Character, Integer> counters = new HashMap<>(5);
//    counters.put('A', 0);
//    counters.put('B', 0);
//    counters.put('C', 0);
//    counters.put('D', 0);
//    counters.put('E', 0);
//
//    for (int i = 0; i < 1_000_000; ++i)
//    {
//        Character ch = generator.get();
//        int count = counters.get(ch);
//        counters.put(ch, ++count);
//    }
//
//    for (char ch = 'A'; ch <= 'E'; ++ch)
//    {
//        System.out.println(ch + " = " + counters.get(ch) / 10_000d + "%");
//    }
//}
//catch (WeightedGenerator.NonpositiveWeightException
//        | WeightedGenerator.RepeatedElementException
//        | WeightedGenerator.EmptyPoolException exc)
//{
//
//}