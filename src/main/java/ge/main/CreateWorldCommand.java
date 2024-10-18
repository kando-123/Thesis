package ge.main;

import ge.utilities.*;
import ge.world.*;

/**
 *
 * @author Kay Jay O'Nail
 */
public class CreateWorldCommand extends Command<Engine>
{
    private final WorldConfig config;

    public CreateWorldCommand(WorldConfig config)
    {
        this.config = config;
    }
    
    @Override
    public void execute(Engine executor)
    {
        executor.createWorld(config);
    }
}
