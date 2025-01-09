package algorithms;

import ge.entity.*;
import ge.field.*;
import ge.utilities.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Kay Jay O'Nail
 */
public class RangeTest
{
    @Test
    public void testInfantryIntransition()
    {
        var owner = new MockPlayer();
        var enemy = new MockPlayer();
        var infantry = new InfantryEntity(owner, 1);
        infantry.setMovable(true);

        Map<Hex, Field> world = new HashMap<>();
        Hex hex = Hex.getOrigin();
        world.put(hex, new WoodField(hex));
        Hex.processRing(2, h -> world.put(h, new GrassField(h)));

        hex = Hex.newInstance(0, -1, +2);
        var fellowOccupiedFortification = new FortressField(hex);
        fellowOccupiedFortification.setOwner(owner);
        fellowOccupiedFortification.setEntity(new InfantryEntity(owner, 1));
        world.put(hex, fellowOccupiedFortification);

        hex = Hex.newInstance(+1, -1, 0);
        var enemyUnoccupiedFortification = new FortressField(hex);
        enemyUnoccupiedFortification.setOwner(enemy);
        world.put(hex, enemyUnoccupiedFortification);

        hex = Hex.newInstance(+1, 0, -1);
        var enemyOccupiedFortification = new FortressField(hex);
        enemyOccupiedFortification.setOwner(enemy);
        enemyOccupiedFortification.setEntity(new InfantryEntity(enemy, 1));
        world.put(hex, enemyOccupiedFortification);

        hex = Hex.newInstance(0, +1, -1);
        Field occupiedLandField = new GrassField(hex);
        occupiedLandField.setOwner(owner);
        occupiedLandField.setEntity(new CavalryEntity(owner, 1));
        world.put(hex, occupiedLandField);

        hex = Hex.newInstance(-1, +1, 0);
        occupiedLandField = new MeadowField(hex);
        occupiedLandField.setOwner(enemy);
        occupiedLandField.setEntity(new CavalryEntity(enemy, 1));
        world.put(hex, occupiedLandField);

        hex = Hex.newInstance(-1, 0, +1);
        var seaField = new SeaField(hex);
        world.put(hex, seaField);

        var accessor = new MockAccessor(world);

        Set<Hex> range = infantry.range(Hex.getOrigin(), accessor);

        // The fields in the second ring should not belong to the range.
        for (var h : range)
        {
            if (h.getRing() == 2)
            {
                fail("Intransitable field was transited.");
            }
        }
    }

    @Test
    public void testInfantryTransition()
    {
        var owner = new MockPlayer();
        var infantry = new InfantryEntity(owner, 1);
        infantry.setMovable(true);

        Map<Hex, Field> world = new HashMap<>();

        Hex hex = Hex.getOrigin();
        world.put(hex, new WoodField(hex));

        Hex.processRing(2, h -> world.put(h, new GrassField(h)));

        Hex.processRing(1, h ->
        {
            if (h.getP() == 0)
            {
                var fellowUnoccupiedFortification = new FortressField(h);
                fellowUnoccupiedFortification.setOwner(owner);
                world.put(h, fellowUnoccupiedFortification);
            }
            else
            {
                var unoccupiedLand = new GrassField(h);
                world.put(h, unoccupiedLand);
            }
        });

        var accessor = new MockAccessor(world);

        Set<Hex> range = infantry.range(Hex.getOrigin(), accessor);

        // The fields in the second ring should belong to the range.
        Hex.processRing(2, h ->
        {
            if (!range.contains(h))
            {
                fail("Tranitable field was not transitd.");
            }
        });
    }

    @Test
    public void testInfantryAccession()
    {
        var owner = new MockPlayer();
        var enemy = new MockPlayer();

        Map<Hex, Field> world = new HashMap<>();
        List<Hex> accessibles = new ArrayList<>();
        List<Hex> inaccessibles = new ArrayList<>();

        Hex hex = Hex.getOrigin();
        world.put(hex, new WoodField(hex));

        Hex.processRing(1, h ->
        {
            world.put(h, new MeadowField(h));
            accessibles.add(h);
        });

        Hex.processRing(2, h ->
        {
            if (!h.isRadial())
            {
                world.put(h, new GrassField(h));
                accessibles.add(h);
            }
        });

        hex = Hex.newInstance(0, -2, +2);
        var seaWithMergeableNavy = new SeaField(hex);
        seaWithMergeableNavy.setOwner(owner);
        seaWithMergeableNavy.setEntity(new NavyEntity(owner, 1));
        world.put(hex, seaWithMergeableNavy);
        accessibles.add(hex);

        hex = Hex.newInstance(+2, -2, 0);
        var landOccupiedByEnemy = new GrassField(hex);
        landOccupiedByEnemy.setOwner(enemy);
        landOccupiedByEnemy.setEntity(new InfantryEntity(enemy, 1));
        world.put(hex, landOccupiedByEnemy);
        accessibles.add(hex);

        hex = Hex.newInstance(+2, 0, -2);
        var landOccupiedByMergeableFellow = new WoodField(hex);
        landOccupiedByMergeableFellow.setOwner(owner);
        landOccupiedByMergeableFellow.setEntity(new InfantryEntity(owner, 1));
        world.put(hex, landOccupiedByMergeableFellow);
        accessibles.add(hex);

        hex = Hex.newInstance(0, +2, -1);
        var seaWithUnmergeableNavy = new SeaField(hex);
        seaWithUnmergeableNavy.setOwner(owner);
        seaWithUnmergeableNavy.setEntity(new NavyEntity(owner, 100));
        world.put(hex, seaWithUnmergeableNavy);
        inaccessibles.add(hex);

        hex = Hex.newInstance(-2, +2, 0);
        world.put(hex, new SeaField(hex));
        inaccessibles.add(hex);

        hex = Hex.newInstance(-2, 0, +2);
        var landOccupiedByUnmergeableFellow = new MeadowField(hex);
        landOccupiedByUnmergeableFellow.setOwner(owner);
        landOccupiedByUnmergeableFellow.setEntity(new InfantryEntity(owner, 100));
        world.put(hex, landOccupiedByUnmergeableFellow);
        inaccessibles.add(hex);

        Hex.processRing(3, h ->
        {
            world.put(h, new MountainsField(h));
            inaccessibles.add(h);
        });

        var infantry = new InfantryEntity(owner, 1);
        infantry.setMovable(true);
        var range = infantry.range(Hex.getOrigin(), new MockAccessor(world));

        for (var a : accessibles)
        {
            if (!range.contains(a))
            {
                fail("Accessible field was not accessed.");
            }
        }
        for (var i : inaccessibles)
        {
            if (range.contains(i))
            {
                fail("Inaccessible field was accessed.");
            }
        }
    }

    @Test
    public void testCavalryIntransition()
    {
        var owner = new MockPlayer();
        var enemy = new MockPlayer();
        var cavalry = new CavalryEntity(owner, 1);
        cavalry.setMovable(true);

        Map<Hex, Field> world = new HashMap<>();

        Hex hex = Hex.getOrigin();
        world.put(hex, new WoodField(hex));

        Hex.processRing(2, h -> world.put(h, new GrassField(h)));

        hex = Hex.newInstance(0, -1, +2);
        var fellowOccupiedFortification = new FortressField(hex);
        fellowOccupiedFortification.setOwner(owner);
        fellowOccupiedFortification.setEntity(new InfantryEntity(owner, 1));
        world.put(hex, fellowOccupiedFortification);

        hex = Hex.newInstance(+1, -1, 0);
        var enemyUnoccupiedFortification = new FortressField(hex);
        enemyUnoccupiedFortification.setOwner(enemy);
        world.put(hex, enemyUnoccupiedFortification);

        hex = Hex.newInstance(+1, 0, -1);
        var enemyOccupiedFortification = new FortressField(hex);
        enemyOccupiedFortification.setOwner(enemy);
        enemyOccupiedFortification.setEntity(new InfantryEntity(enemy, 1));
        world.put(hex, enemyOccupiedFortification);

        hex = Hex.newInstance(0, +1, -1);
        var occupiedLandField = new GrassField(hex);
        occupiedLandField.setOwner(owner);
        occupiedLandField.setEntity(new CavalryEntity(owner, 1));
        world.put(hex, occupiedLandField);

        hex = Hex.newInstance(-1, +1, 0);
        world.put(hex, new MountainsField(hex));

        hex = Hex.newInstance(-1, 0, +1);
        world.put(hex, new SeaField(hex));

        var accessor = new MockAccessor(world);

        Set<Hex> range = cavalry.range(Hex.getOrigin(), accessor);

        // The fields in the second ring should not belong to the range.
        for (var h : range)
        {
            if (h.getRing() == 2)
            {
                fail("Intransitable field was transited.");
            }
        }
    }

    @Test
    public void testCavalryTransition()
    {
        var owner = new MockPlayer();
        var cavalry = new CavalryEntity(owner, 1);
        cavalry.setMovable(true);

        Map<Hex, Field> world = new HashMap<>();

        Hex hex = Hex.getOrigin();
        world.put(hex, new WoodField(hex));

        Hex.processRing(2, h -> world.put(h, new GrassField(h)));

        Hex.processRing(1, h ->
        {
            if (h.getP() == 0)
            {
                var fellowUnoccupiedFortification = new FortressField(h);
                fellowUnoccupiedFortification.setOwner(owner);
                world.put(h, fellowUnoccupiedFortification);
            }
            else
            {
                var unoccupiedLand = new GrassField(h);
                world.put(h, unoccupiedLand);
            }
        });

        var accessor = new MockAccessor(world);

        Set<Hex> range = cavalry.range(Hex.getOrigin(), accessor);

        // The fields in the second ring should belong to the range.
        Hex.processRing(2, h ->
        {
            if (!range.contains(h))
            {
                fail("Transitable field was not transitd.");
            }
        });
    }

    @Test
    public void testCavalryAccession()
    {
        var owner = new MockPlayer();
        var enemy = new MockPlayer();

        Map<Hex, Field> world = new HashMap<>();
        List<Hex> accessibles = new ArrayList<>();
        List<Hex> inaccessibles = new ArrayList<>();

        Hex hex = Hex.newInstance(0, -4, +4);
        var landOccupiedByEnemy = new MeadowField(hex);
        landOccupiedByEnemy.setOwner(enemy);
        landOccupiedByEnemy.setEntity(new InfantryEntity(enemy, 1));
        world.put(hex, landOccupiedByEnemy);
        accessibles.add(hex);

        hex = Hex.newInstance(0, +4, -4);
        var landOccupiedByMergeableFellow = new WoodField(hex);
        landOccupiedByMergeableFellow.setEntity(new CavalryEntity(owner, 1));
        world.put(hex, landOccupiedByMergeableFellow);
        accessibles.add(hex);

        hex = Hex.newInstance(-4, 0, +4);
        world.put(hex, new SeaField(hex));
        inaccessibles.add(hex);

        hex = Hex.newInstance(+4, 0, -4);
        var landOccupiedByUnmergeableFellow = new GrassField(hex);
        landOccupiedByUnmergeableFellow.setOwner(owner);
        landOccupiedByUnmergeableFellow.setEntity(new CavalryEntity(owner, 100));
        world.put(hex, landOccupiedByUnmergeableFellow);
        inaccessibles.add(hex);

        Hex.processSurfaceSpirally(4, h ->
        {
            if (!accessibles.contains(h) && !inaccessibles.contains(h))
            {
                world.put(h, new MeadowField(h));

                if (h.getRing() > 0)
                {
                    accessibles.add(h);
                }
            }
        });

        Hex.processRing(5, h ->
        {
            world.put(h, new GrassField(h));
            inaccessibles.add(h);
        });

        var cavalry = new CavalryEntity(owner, 1);
        cavalry.setMovable(true);
        var range = cavalry.range(Hex.getOrigin(), new MockAccessor(world));

        for (var a : accessibles)
        {
            if (!range.contains(a))
            {
                fail("Accessible field was not accessed.");
            }
        }
        for (var i : inaccessibles)
        {
            if (range.contains(i))
            {
                fail("Inaccessible field was accessed.");
            }
        }
    }

    @Test
    public void testNavyIntransition()
    {
        var owner = new MockPlayer();
        var enemy = new MockPlayer();
        var navy = new NavyEntity(owner, 1);
        navy.setMovable(true);

        Map<Hex, Field> world = new HashMap<>();

        Hex hex = Hex.getOrigin();
        world.put(hex, new SeaField(hex));

        Hex.processRing(1, h ->
        {
            if (h.getP() == 0)
            {
                world.put(h, new MountainsField(h));
            }
            else if (h.getQ() == 0)
            {
                var sea = new SeaField(h);
                sea.setOwner(owner);
                sea.setEntity(new NavyEntity(owner, 1));
                world.put(h, sea);
            }
            else
            {
                var sea = new SeaField(h);
                sea.setOwner(enemy);
                sea.setEntity(new NavyEntity(enemy, 1));
                world.put(h, sea);
            }
        });
        Hex.processRing(2, h -> world.put(h, new SeaField(h)));

        var accessor = new MockAccessor(world);

        Set<Hex> range = navy.range(Hex.getOrigin(), accessor);

        // The fields in the second ring should not belong to the range.
        for (var h : range)
        {
            if (h.getRing() == 2)
            {
                fail("Intransitable field was transited.");
            }
        }
    }

    @Test
    public void testNavy()
    {
        var owner = new MockPlayer();
        var enemy = new MockPlayer();

        Map<Hex, Field> world = new HashMap<>();
        List<Hex> accessibles = new ArrayList<>();
        List<Hex> inaccessibles = new ArrayList<>();

        Hex hex = Hex.newInstance(0, -4, +4);
        world.put(hex, new MeadowField(hex));
        inaccessibles.add(hex);

        hex = Hex.newInstance(+4, -4, 0);
        var occupiedFellowSea = new SeaField(hex);
        occupiedFellowSea.setOwner(owner);
        occupiedFellowSea.setEntity(new NavyEntity(owner, 1));
        world.put(hex, occupiedFellowSea);
        inaccessibles.add(hex);

        hex = Hex.newInstance(0, +4, -4);
        var occupiedEnemySea = new SeaField(hex);
        occupiedEnemySea.setOwner(enemy);
        occupiedEnemySea.setEntity(new NavyEntity(enemy, 1));
        world.put(hex, occupiedEnemySea);
        accessibles.add(hex);

        Hex.processSurfaceSpirally(4, h ->
        {
            if (!accessibles.contains(h) && !inaccessibles.contains(h))
            {
                world.put(h, new SeaField(h));

                if (h.getRing() > 0)
                {
                    accessibles.add(h);
                }
            }
        });

        Hex.processRing(5, h ->
        {
            world.put(h, new SeaField(h));
            inaccessibles.add(h);
        });

        var navy = new NavyEntity(owner, 1);
        navy.setMovable(true);
        var range = navy.range(Hex.getOrigin(), new MockAccessor(world));

        for (var a : accessibles)
        {
            if (!range.contains(a))
            {
                fail("Accessible field was not accessed.");
            }
        }
        for (var i : inaccessibles)
        {
            if (range.contains(i))
            {
                fail("Inaccessible field was accessed.");
            }
        }
    }
}
