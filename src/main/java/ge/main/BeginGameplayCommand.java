package ge.main;

import ge.player.*;
import ge.utilities.*;
import ge.world.*;
import javax.swing.*;

/**
 *
 * @author Kay Jay O'Nail
 */
public class BeginGameplayCommand extends Command<Engine>
{
    private final JFrame frame;
    private final WorldConfig world;
    private final PlayerConfig[] players;

    public BeginGameplayCommand(JFrame frame, WorldConfig world, PlayerConfig[] players)
    {
        this.frame = frame;
        this.world = world;
        this.players = players;
    }
    
    @Override
    public void execute(Engine executor)
    {
        executor.beginGameplay(frame, world, players);
    }
}
