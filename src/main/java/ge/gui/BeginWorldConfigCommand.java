package ge.gui;

import ge.utilities.Command;

/**
 *
 * @author Kay Jay O'Nail
 */
public class BeginWorldConfigCommand extends Command<GUIManager>
{
    @Override
    public void execute(GUIManager executor)
    {
        executor.beginWorldConfig();
    }
}
