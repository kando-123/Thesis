package my.command;

import my.flow.Manager;

/**
 *
 * @author Kay Jay O'Nail
 */
public class PursueHiringCommand extends Command<Manager>
{
    @Override
    public void execute(Manager manager)
    {
        manager.pursueHiring();
    }  
}
