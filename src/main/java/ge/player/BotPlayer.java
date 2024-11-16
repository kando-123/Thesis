package ge.player;

import ge.main.*;
import ge.player.action.Action;
import ge.utilities.*;
import ge.world.*;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Kay Jay O'Nail
 */
public class BotPlayer extends Player
{
    private final Invoker<GameplayManager> invoker;
    
    public BotPlayer(WorldScanner scanner, WorldAccessor accessor, BotConfig config, Invoker<GameplayManager> invoker)
    {
        super(scanner, accessor, config.color);
        
        this.invoker = invoker;
    }

    private final Random random = new Random();
    
    @Override
    public void play()
    {
        System.out.println("The %s Bot is playing.".formatted(getColor().toString()));
        
        List<Action> actions = scanner.actions(this, accessor);
        
        int size = actions.size();
        if (size > 0)
        {
            var action = actions.get(random.nextInt(size));
            action.perform(invoker);
        }
        
        invoker.invoke(new NextPlayerCommand());
    }

    @Override
    public String getName()
    {
        return getColor().toString().concat(" Bot");
    }
}
