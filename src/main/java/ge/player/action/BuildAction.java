package ge.player.action;

import ge.field.*;
import ge.main.*;
import ge.player.*;
import ge.utilities.*;
import java.util.*;

/**
 *
 * @author Kay Jay O'Nail
 */
public class BuildAction extends Action<GameplayManager>
{
    private final BuildingType type;
    private final Hex[] locations;
    private final Player player;

    public BuildAction(BuildingType type, Hex[] locations, Player player)
    {
        assert (locations.length > 0);
        
        this.type = type;
        this.locations = locations;
        this.player = player;
    }

    private static final Random RANDOM = new Random();
    
    @Override
    public void perform(Invoker<GameplayManager> invoker)
    {
        var coords = locations[RANDOM.nextInt(locations.length)];
        var building = BuildingField.newInstance(type, coords);
        building.setOwner(player);
        invoker.invoke(new BuildCommand(building));
    }

    @Override
    public int weight()
    {
        final int commercialWeight = 30;
        final int spawnerWeight = 5;
        final int fortificationWeight = 20;
        if (type.isCommercial())
        {
            return commercialWeight;
        }
        else if (type.isSpawner())
        {
            return spawnerWeight;
        }
        else if (type.isFortification())
        {
            return fortificationWeight;
        }
        else
        {
            throw new RuntimeException(); // Never happens.
        }
    }
}
