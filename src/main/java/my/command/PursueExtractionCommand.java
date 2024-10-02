package my.command;

import my.flow.Manager;

/**
 *
 * @author Kay Jay O'Nail
 */
public class PursueExtractionCommand extends Command<Manager>
{
    private final int number;
    
    public PursueExtractionCommand(int number)
    {
        this.number = number;
    }
    
    @Override
    public void execute(Manager executor)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
