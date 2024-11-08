package ge.main;

import ge.player.*;
import ge.utilities.*;

/**
 *
 * @author Kay Jay O'Nail
 */
public class DropCommand extends Command<GameplayManager>
{
    private final Player player;

    public DropCommand(Player player)
    {
        this.player = player;
    }
    
    @Override
    public void execute(GameplayManager executor)
    {
        executor.drop(player);
    }
}
