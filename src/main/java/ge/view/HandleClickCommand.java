package ge.view;

import ge.utilities.*;

/**
 *
 * @author Kay Jay O'Nail
 */
public class HandleClickCommand extends Command
{
    private final Hex hex;

    public HandleClickCommand(Hex hex)
    {
        this.hex = hex;
    }
    
    @Override
    public void execute(Object executor)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
