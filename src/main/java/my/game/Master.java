package my.game;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;
import javax.imageio.*;
import javax.swing.*;
import my.gameplay.*;
import my.player.*;
import my.player.configuration.*;
import my.utils.*;
import my.units.*;
import my.world.*;
import my.world.configuration.*;

/**
 *
 * @author Kay Jay O'Nail
 */
public class Master extends JFrame implements ActionListener
{
    public static void main(String[] args)
    {
        Master master = new Master();

        master.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        master.setTitle("The Global Empire");
        master.setResizable(false);

        InvitationContentPane invitationPanel = new InvitationContentPane(master);
        master.setContentPane(invitationPanel);
        master.pack();

        master.setVisible(true);
        master.setLocationRelativeTo(null);
    }

    private State state;

    private PlayerConfigurationContentPane playerContentPane;
    private WorldConfigurationContentPane worldContentPane;
    private GameplayContentPane gameplayContentPane;

    private World world;

    private LinkedList<Player> players;

    public Master()
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

    private void beginPlayerSelection()
    {
        if (state == State.INITIAL || state == State.WORLD_CONFIGURATION)
        {
            Dimension newDimension = getSize();
            newDimension.width *= 0.8;
            newDimension.height *= 0.8;

            if (state == State.INITIAL)
            {
                playerContentPane = new PlayerConfigurationContentPane(this);
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
            java.util.List<PlayerConfiguration> playersData = playerContentPane.getPlayerParameters();
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
                    worldContentPane = new WorldConfigurationContentPane(this);
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

            java.util.List<PlayerConfiguration> playersData = playerContentPane.getPlayerParameters();
            int playersNumber = playersData.size();
            createPlayers(playersData);

            Hex[] capitals = world.locateCapitals(playersNumber);
            initCountries(capitals);

            InputHandler inputHandler = new InputHandler();
            addKeyListener(inputHandler);
            setFocusable(true);
            requestFocus();

            gameplayContentPane = new GameplayContentPane(this, world, inputHandler);
            gameplayContentPane.start();

            firstUser();

            setContentPane(gameplayContentPane);
            setResizable(true);
            pack();
            setLocationRelativeTo(null);

            state = State.GAMEPLAY;
        }
    }

    private void createPlayers(java.util.List<PlayerConfiguration> configurationList)
    {
        LinkedList<PlayerColor> availableColors = new LinkedList<>();
        for (int i = 1; i < PlayerColor.values().length; ++i)
        {
            availableColors.add(PlayerColor.values()[i]);
        }

        for (var parameters : configurationList)
        {
            Player player = new Player(parameters.type);

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
            players.get(i).capture(capitals[i], world.getFieldAt(capitals[i]));

            for (var neighbor : capitals[i].neighbors())
            {
                var field = world.getFieldAt(neighbor);
                if (field != null && field.getType() != FieldType.SEA)
                {
                    players.get(i).capture(neighbor, field);
                }
            }
        }
    }

    private void firstUser()
    {
        while (players.getFirst().getType() == PlayerType.BOT)
        {
            Player bot = players.removeFirst();
            bot.play();
            players.addLast(bot);
        }

        Player user = players.removeFirst();
        gameplayContentPane.setCurrentUser(user);
        players.addLast(user);
    }

    private Player currentUser()
    {
        return players.getLast();
    }

    private void nextUser()
    /* Will fall into an infinite loop when the last user dies!
       To be fixed before the bots learn how to kill... */
    {
        currentUser().endRound();
        firstUser();
    }

    private final static Random temporary = new Random();

    @Override
    public void actionPerformed(ActionEvent e)
    {
        String[] commandlets = e.getActionCommand().split(";");
        switch (commandlets[0])
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
                requestFocus();
            }
            case "temporary" ->
            {
                world.mark((obj) -> temporary.nextBoolean()); // if 0, nothing was marked
                requestFocus();
            }
            case "to-build" ->
            {
                world.unmarkAll();
                final Player current = currentUser();
                world.mark(switch (commandlets[1].toLowerCase())
                {
                    case "town", "village", "barracks" ->
                    {
                        yield (Field field) ->
                        {
                            boolean isMine = field.getOwner() == current;
                            boolean isLand = field.getType() == FieldType.LAND;
                            boolean isWood = field.getType() == FieldType.WOOD;
                            return isMine && (isLand || isWood);
                        };
                    }
                    case "farmfield" ->
                    {
                        yield (Field field) ->
                        {
                            if (field.getOwner() != current)
                            {
                                return false;
                            }
                            
                            boolean isLand = field.getType() == FieldType.LAND;
                            boolean isWood = field.getType() == FieldType.WOOD;
                            if (!isLand && !isWood)
                            {
                                return false;
                            }
                            
                            Field[] neighbors = getNeighboringFields(field);
                            for (var neighbor : neighbors)
                            {
                                if (neighbor.getType() == FieldType.VILLAGE)
                                {
                                    return true;
                                }
                            }
                            return false;
                        };
                    }
                    case "mine" ->
                    {
                        yield (Field field) ->
                        {
                            return field.getOwner() == current &&
                                   field.getType() == FieldType.MOUNTAINS;
                        };
                    }
                    case "shipyard" ->
                    {
                        yield (Field field) ->
                        {
                            if (field.getOwner() != current)
                            {
                                return false;
                            }
                            
                            boolean isLand = field.getType() == FieldType.LAND;
                            boolean isWood = field.getType() == FieldType.WOOD;
                            if (!isLand && !isWood)
                            {
                                return false;
                            }
                            
                            Field[] neighbors = getNeighboringFields(field);
                            for (var neighbor : neighbors)
                            {
                                if (neighbor.getType() == FieldType.SEA)
                                {
                                    return true;
                                }
                            }
                            return false;
                        };
                    }
                    case "fortress" ->
                    {
                        yield (Field field) ->
                        {
                            boolean isMine = field.getOwner() == current;
                            boolean isLand = field.getType() == FieldType.LAND;
                            boolean isWood = field.getType() == FieldType.WOOD;
                            boolean isMountain = field.getType() == FieldType.MOUNTAINS;
                            return isMine && (isLand || isWood || isMountain);
                        };
                    }
                    default ->
                    {
                        yield (obj) -> false;
                    }
                });
                requestFocus();
            }
            case "do-build" ->
            {
                requestFocus();
            }
            case "to-hire" ->
            {
                requestFocus();
            }
            case "do-hire" ->
            {
                requestFocus();
            }
        }
    }
    
    private Field[] getNeighboringFields(Field field)
    {
        Hex hex = field.getHex();
        int side = world.getSide();
        int neighborsCount = (hex.getRing() < side - 1) ? 6 : (hex.isRadial()) ? 3 : 4;
        Field[] neighboringFields = new Field[neighborsCount];
        int i = 0;
        for (var neighbor : hex.neighbors())
        {
            Field neighboringField = world.getFieldAt(neighbor);
            if (neighboringField != null)
            {
                neighboringFields[i++] = neighboringField;
            }
        }        
        return neighboringFields;
    }
}
