package ge.view;

import ge.utilities.*;

/**
 *
 * @author Kay Jay O'Nail
 */
public class FinishRoundCommand extends Command<ViewManager>
{
    @Override
    public void execute(ViewManager executor)
    {
        executor.finish();
    }
}
