package algorithms;

import ge.entity.*;
import static ge.entity.Entity.TooFarAwayException;
import static ge.entity.Entity.GoalNotReachedException;
import static ge.entity.Entity.InaccessibleTargetException;
import ge.field.*;
import ge.player.Player;
import ge.utilities.*;
import ge.world.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class MockAccessor implements FieldAccessor
{
    private Map<Hex, Field> world;

    public MockAccessor(Map<Hex, Field> fields)
    {
        this.world = fields;
    }

    @Override
    public Field getField(Hex hex)
    {
        return world.get(hex);
    }
}

class MockPlayer extends Player
{
    public MockPlayer()
    {
        super(null, null, Player.ContourColor.RED);
    }

    @Override
    public void play()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getName()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

/**
 *
 * @author Kay Jay O'Nail
 */
public class PathTest
{
    @Test
    public void testTargetTooFar()
    {
        var owner = new MockPlayer();
        
        var infantry = new InfantryEntity(owner, 1);
        try
        {
            Map<Hex, Field> world = new HashMap<>();
            Hex.processSurfaceSpirally(3, (Hex hex) ->
            {
                world.put(hex, new GrassField(hex));
            });

            var origin = world.get(Hex.newInstance(0, -3, +3));
            var target = world.get(Hex.newInstance(0, +3, -3));
            var accessor = new MockAccessor(world);

            infantry.path(origin, target, accessor);
            fail("Infantry should have thrown exception.");
        }
        catch (TooFarAwayException | GoalNotReachedException e)
        {
        }

        var cavalry = new CavalryEntity(owner, 1);
        try
        {
            Map<Hex, Field> world = new HashMap<>();
            Hex.processSurfaceSpirally(3, (Hex hex) ->
            {
                world.put(hex, new GrassField(hex));
            });
            
            var origin = world.get(Hex.newInstance(0, -3, +3));
            var target = world.get(Hex.newInstance(0, +3, -3));
            var accessor = new MockAccessor(world);

            cavalry.path(origin, target, accessor);
            fail("Cavalry should have thrown exception.");
        }
        catch (TooFarAwayException | GoalNotReachedException e)
        {
        }

        var navy = new NavyEntity(owner, 1);
        try
        {
            Map<Hex, Field> world = new HashMap<>();
            Hex.processSurfaceSpirally(3, (Hex hex) ->
            {
                world.put(hex, new SeaField(hex));
            });
            
            var origin = world.get(Hex.newInstance(0, -3, +3));
            var target = world.get(Hex.newInstance(0, +3, -3));
            var accessor = new MockAccessor(world);

            navy.path(origin, target, accessor);
            fail("Navy should have thrown exception.");
        }
        catch (TooFarAwayException | GoalNotReachedException e)
        {
        }
    }
    
    @Test
    public void testOccupiedTarget()
    {
        var owner = new MockPlayer();
        
        var infantry = new InfantryEntity(owner, 1);
        try
        {
            Map<Hex, Field> world = new HashMap<>();
            Hex.processSurfaceSpirally(3, (Hex hex) ->
            {
                world.put(hex, new GrassField(hex));
            });

            var origin = world.get(Hex.newInstance(0, -1, +1));
            var target = world.get(Hex.newInstance(0, +1, -1));
            target.setEntity(new CavalryEntity(owner, 1));
            var accessor = new MockAccessor(world);

            infantry.path(origin, target, accessor);
            fail("Infantry should have thrown exception.");
        }
        catch (InaccessibleTargetException e)
        {
        }

        var cavalry = new CavalryEntity(owner, 1);
        try
        {
            Map<Hex, Field> world = new HashMap<>();
            Hex.processSurfaceSpirally(3, (Hex hex) ->
            {
                world.put(hex, new GrassField(hex));
            });
            
            var origin = world.get(Hex.newInstance(0, -1, +1));
            var target = world.get(Hex.newInstance(0, +1, -1));
            target.setEntity(new InfantryEntity(owner, 1));
            var accessor = new MockAccessor(world);

            cavalry.path(origin, target, accessor);
            fail("Cavalry should have thrown exception.");
        }
        catch (InaccessibleTargetException e)
        {
        }

        var navy = new NavyEntity(owner, 1);
        try
        {
            Map<Hex, Field> world = new HashMap<>();
            Hex.processSurfaceSpirally(3, (Hex hex) ->
            {
                world.put(hex, new SeaField(hex));
            });
            
            var origin = world.get(Hex.newInstance(0, -1, +1));
            var target = world.get(Hex.newInstance(0, +1, -1));
            target.setEntity(new NavyEntity(owner, 1));
            var accessor = new MockAccessor(world);

            navy.path(origin, target, accessor);
            fail("Navy should have thrown exception.");
        }
        catch (InaccessibleTargetException e)
        {
        }
    }

    // Test cases:
    // Goal not reached due to:
    //     occupied target [ ]
    //     being surrounded by intransitable fields [ ]
    //     obstacles elongating the path enough [ ]
    // Success
}
