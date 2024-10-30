package ge.view;

import ge.utilities.Command;

/**
 *
 * @author Kay Jay O'Nail
 */
public class UpdateMoneyCommand extends Command<ViewManager>
{
    private final int amount;

    public UpdateMoneyCommand(int amount)
    {
        this.amount = amount;
    }
    
    @Override
    public void execute(ViewManager executor)
    {
        executor.updateMoney(amount);
    }
}
