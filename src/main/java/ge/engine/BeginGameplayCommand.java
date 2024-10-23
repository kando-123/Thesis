package ge.engine;

import ge.player.*;
import ge.utilities.*;
import ge.world.*;

/**
 *
 * @author Kay Jay O'Nail
 */
public class BeginGameplayCommand extends Command<Engine>
{
    final PlayerConfig[] players;
    final WorldConfig world;

    public BeginGameplayCommand(PlayerConfig[] players, WorldConfig world)
    {
        this.players = players;
        this.world = world;
    }
    
    @Override
    public void execute(Engine executor)
    {
        executor.beginGameplay(players, world);
    }
}
