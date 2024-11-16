package ge.player;

import ge.main.*;
import ge.utilities.*;
import ge.world.*;

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

    @Override
    public void play()
    {
        System.out.println("The %s Bot is playing.".formatted(getColor().toString()));
        
        // Recognize the situation.
        // Repeat
        //   List the available actions.
        //   Pick.
        // Decide to end.
        
        invoker.invoke(new NextPlayerCommand());
    }

    @Override
    public String getName()
    {
        return getColor().toString().concat(" Bot");
    }
}
