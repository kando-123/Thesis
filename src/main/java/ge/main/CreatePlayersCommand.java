package ge.main;

import ge.player.*;
import ge.utilities.*;

/**
 *
 * @author Kay Jay O'Nail
 */
public class CreatePlayersCommand extends Command<Engine>
{
    private final PlayerConfig[] configs;

    public CreatePlayersCommand(PlayerConfig[] configs)
    {
        this.configs = configs;
    }
    
    @Override
    public void execute(Engine executor)
    {
        executor.createPlayers(configs);
    }
}
