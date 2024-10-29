package ge.player;

import ge.utilities.*;
import ge.view.*;
import ge.world.WorldAccessor;

/**
 *
 * @author Kay Jay O'Nail
 */
public class UserPlayer extends Player
{
    private final String name;
    private final Invoker<ViewManager> invoker;
    
    public UserPlayer(WorldAccessor accessor, UserConfig config, Invoker<ViewManager> invoker)
    {
        super(accessor, config.color);
        
        this.name = config.name;
        this.invoker = invoker;
    }
    
    String getName()
    {
        return name;
    }

    @Override
    public void play()
    {
        // Use the invoker to inform the View that new user data should be set.
        invoker.invoke(new SetUserCommand(new UserAccessor(this)));
    }
}
