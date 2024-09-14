/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package my.field;

import java.awt.image.BufferedImage;
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
public class AssetManagerTest
{
    
    public AssetManagerTest()
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
     * Test of getImage method, of class AssetManager.
     */
    @Test
    public void testGetImage()
    {
        System.out.println("getImage");
        FieldType type = null;
        AssetManager instance = new AssetManager();
        BufferedImage expResult = null;
        BufferedImage result = instance.getImage(type);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getBrightImage method, of class AssetManager.
     */
    @Test
    public void testGetBrightImage()
    {
        System.out.println("getBrightImage");
        FieldType type = null;
        AssetManager instance = new AssetManager();
        BufferedImage expResult = null;
        BufferedImage result = instance.getBrightImage(type);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getIcon method, of class AssetManager.
     */
    @Test
    public void testGetIcon()
    {
        System.out.println("getIcon");
        FieldType type = null;
        AssetManager instance = new AssetManager();
        Icon expResult = null;
        Icon result = instance.getIcon(type);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
