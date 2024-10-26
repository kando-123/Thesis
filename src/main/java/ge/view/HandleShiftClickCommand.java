package ge.view;

import ge.utilities.*;

/**
 *
 * @author Kay Jay O'Nail
 */
public class HandleShiftClickCommand extends Command
{
    private final Hex hex;

    public HandleShiftClickCommand(Hex hex)
    {
        this.hex = hex;
    }
    
    @Override
    public void execute(Object executor)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
