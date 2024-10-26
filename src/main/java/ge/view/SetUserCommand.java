package ge.view;

import ge.player.UserAccessor;
import ge.utilities.Command;

/**
 *
 * @author Kay Jay O'Nail
 */
public class SetUserCommand extends Command<ViewManager>
{
    private final UserAccessor accessor;

    public SetUserCommand(UserAccessor accessor)
    {
        this.accessor = accessor;
    }
    
    @Override
    public void execute(ViewManager executor)
    {
        executor.setUser(accessor);
    }
}
