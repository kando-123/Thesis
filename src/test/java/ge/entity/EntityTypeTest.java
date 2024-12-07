package ge.entity;

import javax.swing.Icon;
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
public class EntityTypeTest
{
    
    public EntityTypeTest()
    {
    }
    
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
     * Test of values method, of class EntityType.
     */
    @Test
    public void testValues()
    {
        System.out.println("values");
        EntityType[] expResult = null;
        EntityType[] result = EntityType.values();
        assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of valueOf method, of class EntityType.
     */
    @Test
    public void testValueOf()
    {
        System.out.println("valueOf");
        String name = "";
        EntityType expResult = null;
        EntityType result = EntityType.valueOf(name);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of price method, of class EntityType.
     */
    @Test
    public void testPrice()
    {
        System.out.println("price");
        int number = 0;
        EntityType instance = null;
        int expResult = 0;
        int result = instance.price(number);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of maxNumber method, of class EntityType.
     */
    @Test
    public void testMaxNumber()
    {
        System.out.println("maxNumber");
        int money = 0;
        EntityType instance = null;
        int expResult = 0;
        int result = instance.maxNumber(money);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of toString method, of class EntityType.
     */
    @Test
    public void testToString()
    {
        System.out.println("toString");
        EntityType instance = null;
        String expResult = "";
        String result = instance.toString();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of icon method, of class EntityType.
     */
    @Test
    public void testIcon()
    {
        System.out.println("icon");
        EntityType instance = null;
        Icon expResult = null;
        Icon result = instance.icon();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of description method, of class EntityType.
     */
    @Test
    public void testDescription()
    {
        System.out.println("description");
        EntityType instance = null;
        String expResult = "";
        String result = instance.description();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of conditions method, of class EntityType.
     */
    @Test
    public void testConditions()
    {
        System.out.println("conditions");
        EntityType instance = null;
        String expResult = "";
        String result = instance.conditions();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of pricing method, of class EntityType.
     */
    @Test
    public void testPricing()
    {
        System.out.println("pricing");
        EntityType instance = null;
        String expResult = "";
        String result = instance.pricing();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
