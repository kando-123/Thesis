package my.command;

import my.flow.Manager;

/**
 *
 * @author Kay Jay O'Nail
 */
public class ExtractEntityCommand extends Command<Manager>
{
    private final int number;
    
    public ExtractEntityCommand(int number)
    {
        this.number = number;
    }
    
    @Override
    public void execute(Manager executor)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
