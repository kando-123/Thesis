package ge.player;

import ge.entity.*;
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
    private Invoker<ViewManager> invoker;
    
    private UserPlayer(WorldScanner scanner, WorldAccessor accessor, UserConfig config)
    {
        super(scanner, accessor, config.color);
        
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
    
    public static class Builder
    {
        private UserConfig config;
        private WorldScanner scanner;
        private WorldAccessor accessor;
        private Invoker<ViewManager> invoker;

        public void setConfig(UserConfig config)
        {
            this.config = config;
        }

        public void setScanner(WorldScanner scanner)
        {
            this.scanner = scanner;
        }
        
        public void setAccessor(WorldAccessor accessor)
        {
            this.accessor = accessor;
        }

        public void setInvoker(Invoker<ViewManager> invoker)
        {
            this.invoker = invoker;
        }
        
        public UserPlayer get()
        {
            UserPlayer user = null;
            if (config != null && scanner != null && accessor != null && invoker != null)
            {
                user = new UserPlayer(scanner, accessor, config);
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

    @Override
    public void buy(EntityType entity, int number)
    {
        super.buy(entity, number);
        invoker.invoke(new UpdateMoneyCommand(getMoney()));
    }
    
    public Hex center()
    {
        return scanner.center(this);
    }
}
