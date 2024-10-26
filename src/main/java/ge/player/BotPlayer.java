package ge.player;

import ge.main.NextPlayerCommand;
import ge.main.*;
import ge.utilities.*;

/**
 *
 * @author Kay Jay O'Nail
 */
public class BotPlayer extends Player
{
    private final Invoker<GameplayManager> invoker;
    
    public BotPlayer(BotConfig config, Invoker<GameplayManager> invoker)
    {
        super(config.color);
        
        this.invoker = invoker;
    }

    @Override
    public void play()
    {
        // Through the invoker, send messages to the gameplay manager to demand actions.
        System.out.println("The %s Bot is playing.".formatted(getColor().toString()));
        
        invoker.invoke(new NextPlayerCommand());
    }
    
}
