package ge.main;

import ge.utilities.*;

/**
 *
 * @author Kay Jay O'Nail
 */
public class NextPlayerCommand extends Command<GameplayManager>
{
    @Override
    public void execute(GameplayManager executor)
    {
        executor.end();
        executor.begin();
    }
}
