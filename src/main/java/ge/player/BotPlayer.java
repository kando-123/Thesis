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

    private final Random random = new Random();

    @Override
    public void play()
    {
        System.out.println("The %s Bot is playing.".formatted(getColor().toString()));

        for (List<Action> actions = scanner.actions(this); !actions.isEmpty(); actions = scanner.actions(this))
        {
//            int size = actions.size();
//            
//            assert (size > 0);
//            System.out.print(size + ": ");
//            
//            var action = actions.get(random.nextInt(size));
//            for (var a : actions)
//            {
//                System.out.print(a.getClass().getSimpleName().charAt(0) + " ");
//            }
//            System.out.println();
//            action.perform(invoker);
            
            
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
