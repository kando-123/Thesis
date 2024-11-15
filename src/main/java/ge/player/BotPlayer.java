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
        // Through the invoker, send messages to the gameplay manager to demand actions.
        System.out.println("The %s Bot is playing.".formatted(getColor().toString()));
        
        invoker.invoke(new NextPlayerCommand());
    }

    @Override
    public String getName()
    {
        return getColor().toString().concat(" Bot");
    }
}
