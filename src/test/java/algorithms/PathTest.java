package algorithms;

import ge.entity.*;
import static ge.entity.Entity.TooFarAwayException;
import static ge.entity.Entity.GoalNotReachedException;
import static ge.entity.Entity.InaccessibleTargetException;
import ge.field.*;
import ge.player.Player;
import ge.utilities.*;
import ge.world.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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

    @Test
    public void testIntransitableSurroundings()
    {
        var owner = new MockPlayer();

        var infantry = new InfantryEntity(owner, 1);
        try
        {
            Map<Hex, Field> world = new HashMap<>();
            Hex.processSurfaceSpirally(3, (Hex hex) ->
            {
                var field = hex.getRing() == 1
                        ? new SeaField(hex)
                        : new GrassField(hex);
                world.put(hex, field);
            });

            var origin = world.get(Hex.newInstance(0, 0, 0));
            var target = world.get(Hex.newInstance(0, +2, -2));
            var accessor = new MockAccessor(world);

            infantry.path(origin, target, accessor);
            fail("Infantry should have thrown exception.");
        }
        catch (GoalNotReachedException e)
        {
        }

        var cavalry = new CavalryEntity(owner, 1);
        try
        {
            Map<Hex, Field> world = new HashMap<>();
            Hex.processSurfaceSpirally(3, (Hex hex) ->
            {
                var field = hex.getRing() == 2
                        ? new SeaField(hex)
                        : new GrassField(hex);
                world.put(hex, field);
            });

            var origin = world.get(Hex.newInstance(0, 0, 0));
            var target = world.get(Hex.newInstance(0, +3, -3));
            var accessor = new MockAccessor(world);

            cavalry.path(origin, target, accessor);
            fail("Cavalry should have thrown exception.");
        }
        catch (GoalNotReachedException e)
        {
        }

        var navy = new NavyEntity(owner, 1);
        try
        {
            Map<Hex, Field> world = new HashMap<>();
            Hex.processSurfaceSpirally(3, (Hex hex) ->
            {
                var field = hex.getRing() == 2
                        ? new GrassField(hex)
                        : new SeaField(hex);
                world.put(hex, field);
            });

            var origin = world.get(Hex.newInstance(0, 0, 0));
            var target = world.get(Hex.newInstance(0, +3, -3));
            var accessor = new MockAccessor(world);

            navy.path(origin, target, accessor);
            fail("Navy should have thrown exception.");
        }
        catch (GoalNotReachedException e)
        {
        }
    }

    @Test
    public void testTooLongPath()
    {
        var owner = new MockPlayer();

        var infantry = new InfantryEntity(owner, 1);
        try
        {
            Map<Hex, Field> world = new HashMap<>();
            Hex.processSurfaceSpirally(3, (Hex hex) ->
            {
                var field = hex.getRing() == 1
                        ? new SeaField(hex)
                        : new GrassField(hex);
                world.put(hex, field);
            });
            var hex = Hex.newInstance(0, -1, +1);
            world.put(hex, new GrassField(hex));

            var origin = world.get(Hex.newInstance(0, 0, 0));
            var target = world.get(Hex.newInstance(0, +2, -2));
            var accessor = new MockAccessor(world);

            infantry.path(origin, target, accessor);
            fail("Infantry should have thrown exception.");
        }
        catch (GoalNotReachedException e)
        {
        }

        var cavalry = new CavalryEntity(owner, 1);
        try
        {
            Map<Hex, Field> world = new HashMap<>();
            Hex.processSurfaceSpirally(3, (Hex hex) ->
            {
                var field = hex.getRing() == 2
                        ? new SeaField(hex)
                        : new GrassField(hex);
                world.put(hex, field);
            });
            var hex = Hex.newInstance(0, -2, +2);
            world.put(hex, new GrassField(hex));

            var origin = world.get(Hex.newInstance(0, 0, 0));
            var target = world.get(Hex.newInstance(0, +3, -3));
            var accessor = new MockAccessor(world);

            cavalry.path(origin, target, accessor);
            fail("Cavalry should have thrown exception.");
        }
        catch (GoalNotReachedException e)
        {
        }

        var navy = new NavyEntity(owner, 1);
        try
        {
            Map<Hex, Field> world = new HashMap<>();
            Hex.processSurfaceSpirally(3, (Hex hex) ->
            {
                var field = hex.getRing() == 2
                        ? new GrassField(hex)
                        : new SeaField(hex);
                world.put(hex, field);
            });
            var hex = Hex.newInstance(0, -2, +2);
            world.put(hex, new SeaField(hex));

            var origin = world.get(Hex.newInstance(0, 0, 0));
            var target = world.get(Hex.newInstance(0, +3, -3));
            var accessor = new MockAccessor(world);

            navy.path(origin, target, accessor);
            fail("Navy should have thrown exception.");
        }
        catch (GoalNotReachedException e)
        {
        }
    }

    @Test
    public void testSuccess()
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

            Hex hex;
            var origin = world.get(Hex.newInstance(0, 0, 0));
            var target = world.get(hex = Hex.newInstance(0, +2, -2));
            var accessor = new MockAccessor(world);

            // Simple case.
            List<Hex> path = infantry.path(origin, target, accessor);
            verifyEndpoints(path, origin, target);
            verifyPath(path, infantry, accessor);

            // Mergeable infantry entity on the target.
            target.setEntity(new InfantryEntity(owner, 1));

            path = infantry.path(origin, target, accessor);
            verifyEndpoints(path, origin, target);
            verifyPath(path, infantry, accessor);

            target = new SeaField(hex);
            world.put(hex, target);
            target.setEntity(new NavyEntity(owner, 1));

            // Embarkable navy entity on the target.
            path = infantry.path(origin, target, accessor);
            verifyEndpoints(path, origin, target);
            verifyPath(path, infantry, accessor);
        }
        catch (GoalNotReachedException
               | TooFarAwayException
               | InaccessibleTargetException e)
        {
            fail(e);
        }

        var cavalry = new CavalryEntity(owner, 1);
        try
        {
            Map<Hex, Field> world = new HashMap<>();
            Hex.processSurfaceSpirally(3, (Hex hex) ->
            {
                world.put(hex, new GrassField(hex));
            });

            // Simple case.
            var origin = world.get(Hex.newInstance(0, -2, +2));
            var target = world.get(Hex.newInstance(0, +2, -2));
            var accessor = new MockAccessor(world);

            List<Hex> path = cavalry.path(origin, target, accessor);
            verifyEndpoints(path, origin, target);
            verifyPath(path, cavalry, accessor);

            // Mergeable entity on the target.
            target.setEntity(new CavalryEntity(owner, 1));
            
            cavalry.path(origin, target, accessor);
            verifyEndpoints(path, origin, target);
            verifyPath(path, cavalry, accessor);
        }
        catch (GoalNotReachedException e)
        {
            fail(e);
        }

        var navy = new NavyEntity(owner, 1);
        try
        {
            Map<Hex, Field> world = new HashMap<>();
            Hex.processSurfaceSpirally(3, (Hex hex) ->
            {
                world.put(hex, new SeaField(hex));
            });

            var origin = world.get(Hex.newInstance(0, -2, +2));
            var target = world.get(Hex.newInstance(0, +2, -2));
            var accessor = new MockAccessor(world);

            // Simple case.
            List<Hex> path = navy.path(origin, target, accessor);
            verifyEndpoints(path, origin, target);
            verifyPath(path, navy, accessor);
        }
        catch (GoalNotReachedException e)
        {
            fail(e);
        }
    }
    
    private void verifyEndpoints(List<Hex> path, Field origin, Field target)
    {
        if (!path.getFirst().equals(origin.getHex()) || !path.getLast().equals(target.getHex()))
        {
            fail("The path has wrong endpoint(s).");
        }
    }

    private void verifyPath(List<Hex> path, Entity entity, MockAccessor accessor)
    {
        try
        {
            Class<?> klass = Class.forName("ge.entity.Entity");

            Method radiusMethod = klass.getDeclaredMethod("radius");
            radiusMethod.setAccessible(true);
            int radius = (Integer) radiusMethod.invoke(entity);

            Method canAccess = klass.getDeclaredMethod("canAccess", Field.class);
            canAccess.setAccessible(true);

            Method canTransit = klass.getDeclaredMethod("canTransit", Field.class);
            canAccess.setAccessible(true);

            if (path.size() > radius + 1)
            {
                fail("Wrong path: too long!");
            }

            for (int i = 1; i < path.size(); ++i)
            {
                Hex previous = path.get(i - 1);
                Hex current = path.get(i);

                if (!previous.isNeighbor(current))
                {
                    fail("Unadjacent successive points.");
                }

                if (!(Boolean) canAccess.invoke(entity, accessor.getField(current)))
                {
                    fail("Inaccessible field on the path.");
                }
                
                if (i < path.size() - 1
                    && !(Boolean) canTransit.invoke(entity, accessor.getField(current)))
                {
                    fail("Intransitable field on the path.");
                }
            }
        }
        catch (ClassNotFoundException
               | IllegalAccessException
               | NoSuchMethodException
               | SecurityException
               | InvocationTargetException e)
        {

        }
    }
}
