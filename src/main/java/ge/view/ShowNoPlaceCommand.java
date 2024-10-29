package ge.view;

import ge.utilities.*;

/**
 *
 * @author Kay Jay O'Nail
 */
public class ShowNoPlaceCommand extends Command<ViewManager>
{
    @Override
    public void execute(ViewManager executor)
    {
        executor.showNoPlaceMessage();
    }
}
