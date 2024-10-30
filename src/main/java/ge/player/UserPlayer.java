package ge.player;

import ge.field.*;
import ge.utilities.*;
import ge.view.*;
import ge.world.*;

/**
 *
 * @author Kay Jay O'Nail
 */
public class UserPlayer extends Player
{
    private final String name;
    private WorldMarker marker;
    private Invoker<ViewManager> invoker;
    
    private UserPlayer(WorldAccessor accessor, UserConfig config)
    {
        super(accessor, config.color);
        
        this.name = config.name;
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
    
    public void markPlaces(boolean value, BuildingType building)
    {
        marker.mark(value, f -> f.isOwned(this) && building.predicate(accessor).test(f));
    }
    
    public static class Builder
    {
        private UserConfig config;
        private WorldAccessor accessor;
        private WorldMarker marker;
        private Invoker<ViewManager> invoker;

        public void setConfig(UserConfig config)
        {
            this.config = config;
        }

        public void setAccessor(WorldAccessor accessor)
        {
            this.accessor = accessor;
        }

        public void setMarker(WorldMarker marker)
        {
            this.marker = marker;
        }

        public void setInvoker(Invoker<ViewManager> invoker)
        {
            this.invoker = invoker;
        }
        
        public UserPlayer get()
        {
            UserPlayer user = null;
            if (config != null && accessor != null && marker != null && invoker != null)
            {
                user = new UserPlayer(accessor, config);
                user.marker = marker;
                user.invoker = invoker;
            }
            return user;
        }
    }

    @Override
    public void buy(BuildingType building) throws TooLittleMoneyException
    {
        super.buy(building);
        
        invoker.invoke(new UpdateMoneyCommand(getMoney()));
    }
    
}
