package ge.view;

import ge.utilities.Command;

/**
 *
 * @author Kay Jay O'Nail
 */
public class VictoryMessageCommand extends Command<ViewManager>
{
    private final String name;

    public VictoryMessageCommand(String name)
    {
        this.name = name;
    }
    
    @Override
    public void execute(ViewManager executor)
    {
        executor.showVictoryMessage(name);
    }
}
