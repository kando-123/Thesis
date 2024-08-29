package my.player;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import my.utils.Hex;
import my.world.World;

/**
 *
 * @author Kay Jay O'Nail
 */
public class PlayersQueue
{
    private final List<Player> queue;
    
    private int usersCount;
    
    public PlayersQueue(List<PlayerConfiguration> configurations)
    {
        queue = new LinkedList<>();
        usersCount = 0;
        
        List<PlayerColor> availableColors = new LinkedList<>();
        for (int i = 1; i < PlayerColor.values().length; ++i)
        {
            availableColors.add(PlayerColor.values()[i]);
        }

        for (var parameters : configurations)
        {
            Player player = new Player(parameters.type);
            
            if (parameters.type == PlayerType.USER)
            {
                ++usersCount;
            }

            if (parameters.color != PlayerColor.RANDOM)
            {
                player.setColor(parameters.color);
                availableColors.remove(parameters.color);
            }

            player.setName(parameters.name.isBlank() ? "Anonymous the Conqueror" : parameters.name);

            queue.add(player);
        }

        if (!availableColors.isEmpty())
        {
            Random random = new Random();
            for (var player : queue)
            {
                if (player.getColor() == null)
                {
                    int randomIndex = random.nextInt(availableColors.size());
                    PlayerColor color = availableColors.remove(randomIndex);
                    player.setColor(color);
                }
            }
        }

        Collections.shuffle(queue);
    }
    
    public void initCountries(Hex[] capitals, World world)
    {
        assert (capitals.length == queue.size());

        for (int i = 0; i < capitals.length; ++i)
        {
            queue.get(i).capture(world.getFieldAt(capitals[i]));

            for (var neighbor : capitals[i].neighbors())
            {
                var field = world.getFieldAt(neighbor);
                if (field != null && !field.getType().isMarine())
                {
                    queue.get(i).capture(field);
                }
            }
        }
    }
    
    public Player first()
    {
        while (queue.getFirst().getType() == PlayerType.BOT)
        {
            Player bot = queue.removeFirst();
            bot.play();
            queue.addLast(bot);
        }

        Player user = queue.removeFirst();
        queue.addLast(user);
        return user;
    }
    
    public Player next()
    {
        while (queue.getFirst().getType() == PlayerType.BOT)
        {
            Player bot = queue.removeFirst();
            bot.play();
            queue.addLast(bot);
        }

        Player user = queue.removeFirst();
        queue.addLast(user);
        return user;
    }
    
    public Player current()
    {
        return queue.getLast();
    }
}
