package ge.main;

import ge.utilities.Command;

/**
 *
 * @author Kay Jay O'Nail
 */
public class ResetCommand extends Command<Engine>
{
    @Override
    public void execute(Engine executor)
    {
        executor.reset();
    }
}
