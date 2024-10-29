package ge.view;

import ge.utilities.Command;

/**
 *
 * @author Kay Jay O'Nail
 */
public class ShowNoMoneyCommand extends Command<ViewManager>
{
    @Override
    public void execute(ViewManager executor)
    {
        executor.showNoMoneyMessage();
    }
}
