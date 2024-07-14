package my.game;

import my.world.configuration.WorldConfiguration;
import my.world.configuration.WorldConfigurationContentPane;
import my.player.configuration.PlayerConfigurationContentPane;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import my.gameplay.*;
import my.gameplay.activity.*;
import my.player.*;
import my.world.*;
import my.world.field.FieldType;

/**
 *
 * @author Kay Jay O'Nail
 */
public class Master extends JFrame implements ActionListener, ActivityListener
{
    private State state;

    private PlayerConfigurationContentPane playerContentPane;
    private WorldConfigurationContentPane worldContentPane;
    private GameplayContentPane gameplayContentPane;

    private World world;

    private LinkedList<AbstractPlayer> players;

    private Master()
    {
        state = State.INITIAL;
        players = new LinkedList<>();

        try
        {
            InputStream stream = getClass().getResourceAsStream("/Logo/Icon.png");
            BufferedImage image = ImageIO.read(stream);
            setIconImage(image);
        }
        catch (IOException io)
        {

        }
    }

    private static Master instance = null;

    public static Master getInstance()
    {
        if (instance == null)
        {
            instance = new Master();
        }
        return instance;
    }

    private void beginPlayerSelection()
    {
        if (state == State.INITIAL || state == State.WORLD_CONFIGURATION)
        {
            Dimension newDimension = getSize();
            newDimension.width *= 0.8;
            newDimension.height *= 0.8;

            if (state == State.INITIAL)
            {
                playerContentPane = new PlayerConfigurationContentPane();
                playerContentPane.setPreferredSize(newDimension);
            }
            setContentPane(playerContentPane);
            pack();
            setLocationRelativeTo(null);

            state = State.PLAYERS_SELECTION;
        }
    }

    private void beginWorldConfiguration()
    {
        if (state == State.PLAYERS_SELECTION)
        {
            List<PlayerParameters> playersData = playerContentPane.getPlayerParameters();
            int playersNumber = playersData.size();
            if (playersNumber < 2)
            {
                JOptionPane.showMessageDialog(this,
                        "Select at least 2 players.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
            else
            {
                if (worldContentPane == null)
                {
                    worldContentPane = new WorldConfigurationContentPane();
                    worldContentPane.setPreferredSize(getSize());
                }
                setContentPane(worldContentPane);
                pack();
                setLocationRelativeTo(null);

                state = State.WORLD_CONFIGURATION;
            }
        }
    }

    private void beginGameplay()
    {
        if (state == State.WORLD_CONFIGURATION)
        {
            WorldConfiguration configuration = worldContentPane.getConfiguration();
            world = new World(configuration);

            List<PlayerParameters> playersData = playerContentPane.getPlayerParameters();
            int playersNumber = playersData.size();
            createPlayers(playersData);

            Hex[] capitals = world.locateCapitals(playersNumber);
            initCountries(capitals);

            gameplayContentPane = new GameplayContentPane(world);
            gameplayContentPane.start();
            
            nextUser();
            
            setContentPane(gameplayContentPane);
            setResizable(true);
            pack();
            setLocationRelativeTo(null);

            state = State.GAMEPLAY;
        }
    }

    private void createPlayers(List<PlayerParameters> parametersList)
    {
        LinkedList<PlayerColor> availableColors = new LinkedList<>();
        for (int i = 1; i < PlayerColor.values().length; ++i)
        {
            availableColors.add(PlayerColor.values()[i]);
        }

        for (var parameters : parametersList)
        {
            AbstractPlayer player = (parameters.type == PlayerType.USER)
                    ? new UserPlayer()
                    : new BotPlayer();

            if (parameters.color != PlayerColor.RANDOM)
            {
                player.setColor(parameters.color);
                availableColors.remove(parameters.color);
            }

            player.setName(parameters.name.isBlank() ? "Anonymous the Conqueror" : parameters.name);

            players.add(player);
        }

        if (!availableColors.isEmpty())
        {
            Random random = new Random();
            for (var player : players)
            {
                if (player.getColor() == null)
                {
                    int randomIndex = random.nextInt(availableColors.size());
                    PlayerColor color = availableColors.remove(randomIndex);
                    player.setColor(color);
                }
            }
        }
        
        Collections.shuffle(players);
    }

    private void initCountries(Hex[] capitals)
    {
        assert (capitals.length == players.size());

        for (int i = 0; i < capitals.length; ++i)
        {
            players.get(i).capture(world.getFieldAt(capitals[i]));

            for (var neighbor : capitals[i].neighbors())
            {
                var field = world.getFieldAt(neighbor);
                if (field != null && field.getType() != FieldType.SEA)
                {
                    players.get(i).capture(field);
                }
            }
        }
    }
    
    private void nextUser()
    /* Will fall into an infinite loop when the last user dies!
       To be fixed before the bots learn how to kill... */
    {
        while (players.getFirst().getType() == PlayerType.BOT)
        {
            BotPlayer bot = (BotPlayer) players.removeFirst();
            bot.play();
            players.addLast(bot);
        }
        
        UserPlayer user = (UserPlayer) players.removeFirst();
        gameplayContentPane.setCurrentUser(user);
        System.out.println(String.format("User '%s' is playing.", user.getName()));
        players.addLast(user);
        
        pack();
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        switch (e.getActionCommand())
        {
            case "->players" ->
            {
                beginPlayerSelection();
            }
            case "->world" ->
            {
                beginWorldConfiguration();
            }
            case "->gameplay" ->
            {
                beginGameplay();
            }
            case "done" ->
            {
                nextUser();
            }
        }
    }

    @Override
    public void performActivity(Activity a)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
