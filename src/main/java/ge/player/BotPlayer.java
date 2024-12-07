package ge.player;

import ge.main.*;
import ge.player.action.*;
import ge.utilities.*;
import ge.world.*;
import java.util.*;

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
        for (int i = 0; i < 20; ++i)
        {
            List<Action> actions = scanner.actions(this);
            
            if (actions.isEmpty())
            {
                break;
            }
            
            int size = actions.size();
            assert (size > 0);
            
            var generator = new WeightedGenerator<Action>();
            for (var action : actions)
            {
                generator.add(action, action.weight());
            }
            
            var action = generator.get();
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
