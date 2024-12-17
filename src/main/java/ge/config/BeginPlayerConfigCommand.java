package ge.config;

import ge.utilities.Command;

/**
 *
 * @author Kay Jay O'Nail
 */
public class BeginPlayerConfigCommand extends Command<ConfigManager>
{
    @Override
    public void execute(ConfigManager executor)
    {
        executor.doPlayerConfig();
    }
}