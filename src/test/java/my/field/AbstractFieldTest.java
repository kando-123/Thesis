package my.field;

import java.awt.Dimension;
import java.awt.Graphics2D;
import javax.swing.Icon;
import my.player.Player;
import my.utils.Doublet;
import my.utils.Hex;
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
public class AbstractFieldTest
{
    
    public AbstractFieldTest()
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
     * Test of copy method, of class AbstractField.
     */
    @Test
    public void testCopy()
    {
        System.out.println("copy");
        AbstractField instance = null;
        AbstractField expResult = null;
        AbstractField result = instance.copy();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of cloneProperties method, of class AbstractField.
     */
    @Test
    public void testCloneProperties()
    {
        System.out.println("cloneProperties");
        AbstractField other = null;
        AbstractField instance = null;
        instance.cloneProperties(other);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getName method, of class AbstractField.
     */
    @Test
    public void testGetName()
    {
        System.out.println("getName");
        AbstractField instance = null;
        String expResult = "";
        String result = instance.getName();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getType method, of class AbstractField.
     */
    @Test
    public void testGetType()
    {
        System.out.println("getType");
        AbstractField instance = null;
        FieldType expResult = null;
        FieldType result = instance.getType();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setHex method, of class AbstractField.
     */
    @Test
    public void testSetHex()
    {
        System.out.println("setHex");
        Hex newHex = null;
        AbstractField instance = null;
        Hex expResult = null;
        Hex result = instance.setHex(newHex);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getHex method, of class AbstractField.
     */
    @Test
    public void testGetHex()
    {
        System.out.println("getHex");
        AbstractField instance = null;
        Hex expResult = null;
        Hex result = instance.getHex();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getWidth method, of class AbstractField.
     */
    @Test
    public void testGetWidth()
    {
        System.out.println("getWidth");
        AbstractField instance = null;
        int expResult = 0;
        int result = instance.getWidth();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getHeight method, of class AbstractField.
     */
    @Test
    public void testGetHeight()
    {
        System.out.println("getHeight");
        AbstractField instance = null;
        int expResult = 0;
        int result = instance.getHeight();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isOwned method, of class AbstractField.
     */
    @Test
    public void testIsOwned()
    {
        System.out.println("isOwned");
        AbstractField instance = null;
        boolean expResult = false;
        boolean result = instance.isOwned();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setOwner method, of class AbstractField.
     */
    @Test
    public void testSetOwner()
    {
        System.out.println("setOwner");
        Player newOwner = null;
        AbstractField instance = null;
        instance.setOwner(newOwner);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getOwner method, of class AbstractField.
     */
    @Test
    public void testGetOwner()
    {
        System.out.println("getOwner");
        AbstractField instance = null;
        Player expResult = null;
        Player result = instance.getOwner();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isFree method, of class AbstractField.
     */
    @Test
    public void testIsFree()
    {
        System.out.println("isFree");
        AbstractField instance = null;
        boolean expResult = false;
        boolean result = instance.isFree();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of mark method, of class AbstractField.
     */
    @Test
    public void testMark()
    {
        System.out.println("mark");
        AbstractField instance = null;
        instance.mark();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of unmark method, of class AbstractField.
     */
    @Test
    public void testUnmark()
    {
        System.out.println("unmark");
        AbstractField instance = null;
        instance.unmark();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of draw method, of class AbstractField.
     */
    @Test
    public void testDraw()
    {
        System.out.println("draw");
        Graphics2D graphics = null;
        Doublet<Integer> position = null;
        Dimension size = null;
        AbstractField instance = null;
        instance.draw(graphics, position, size);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getIcon method, of class AbstractField.
     */
    @Test
    public void testGetIcon()
    {
        System.out.println("getIcon");
        AbstractField instance = null;
        Icon expResult = null;
        Icon result = instance.getIcon();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isNatural method, of class AbstractField.
     */
    @Test
    public void testIsNatural()
    {
        System.out.println("isNatural");
        AbstractField instance = null;
        boolean expResult = false;
        boolean result = instance.isNatural();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isMarine method, of class AbstractField.
     */
    @Test
    public void testIsMarine()
    {
        System.out.println("isMarine");
        AbstractField instance = null;
        boolean expResult = false;
        boolean result = instance.isMarine();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isContinental method, of class AbstractField.
     */
    @Test
    public void testIsContinental()
    {
        System.out.println("isContinental");
        AbstractField instance = null;
        boolean expResult = false;
        boolean result = instance.isContinental();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isMountainous method, of class AbstractField.
     */
    @Test
    public void testIsMountainous()
    {
        System.out.println("isMountainous");
        AbstractField instance = null;
        boolean expResult = false;
        boolean result = instance.isMountainous();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isPlains method, of class AbstractField.
     */
    @Test
    public void testIsPlains()
    {
        System.out.println("isPlains");
        AbstractField instance = null;
        boolean expResult = false;
        boolean result = instance.isPlains();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isProperty method, of class AbstractField.
     */
    @Test
    public void testIsProperty()
    {
        System.out.println("isProperty");
        AbstractField instance = null;
        boolean expResult = false;
        boolean result = instance.isProperty();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isCapital method, of class AbstractField.
     */
    @Test
    public void testIsCapital()
    {
        System.out.println("isCapital");
        AbstractField instance = null;
        boolean expResult = false;
        boolean result = instance.isCapital();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isBuilding method, of class AbstractField.
     */
    @Test
    public void testIsBuilding()
    {
        System.out.println("isBuilding");
        AbstractField instance = null;
        boolean expResult = false;
        boolean result = instance.isBuilding();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isCommercial method, of class AbstractField.
     */
    @Test
    public void testIsCommercial()
    {
        System.out.println("isCommercial");
        AbstractField instance = null;
        boolean expResult = false;
        boolean result = instance.isCommercial();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isSpawner method, of class AbstractField.
     */
    @Test
    public void testIsSpawner()
    {
        System.out.println("isSpawner");
        AbstractField instance = null;
        boolean expResult = false;
        boolean result = instance.isSpawner();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isDefense method, of class AbstractField.
     */
    @Test
    public void testIsDefense()
    {
        System.out.println("isDefense");
        AbstractField instance = null;
        boolean expResult = false;
        boolean result = instance.isDefense();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of newInstance method, of class AbstractField.
     */
    @Test
    public void testNewInstance()
    {
        System.out.println("newInstance");
        FieldType type = null;
        AbstractField expResult = null;
        AbstractField result = AbstractField.newInstance(type);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    public class AbstractFieldImpl extends AbstractField
    {
        public AbstractFieldImpl()
        {
            super(null);
        }
    }
    
}
