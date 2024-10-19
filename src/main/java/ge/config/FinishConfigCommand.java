package ge.config;

import ge.utilities.Command;

/**
 *
 * @author Kay Jay O'Nail
 */
public class FinishConfigCommand extends Command<ConfigManager>
{
    @Override
    public void execute(ConfigManager executor)
    {
        executor.finishConfiguration();
    }
}
