package my.game;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;
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

    private PlayersQueue players;

    public Master()
    {
        state = State.INITIAL;

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
            world = new World(worldContentPane.getConfiguration());

            java.util.List<PlayerConfiguration> configurations = playerContentPane.getPlayerParameters();
            players = new PlayersQueue(configurations);

            Hex[] capitals = world.locateCapitals(configurations.size());
            players.initCountries(capitals, world);

            InputHandler inputHandler = new InputHandler();
            addKeyListener(inputHandler);
            setFocusable(true);
            requestFocus();

            gameplayContentPane = new GameplayContentPane(this, world, inputHandler);
            gameplayContentPane.start();

            Player firstUser = players.first();
            gameplayContentPane.setCurrentUser(firstUser);

            setContentPane(gameplayContentPane);
            setResizable(true);
            pack();
            setLocationRelativeTo(null);

            state = State.GAMEPLAY;
        }
    }

    private void nextUser()
    {
        players.current().endRound();
        Player user = players.next();
        gameplayContentPane.setCurrentUser(user);
    }

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
            case "to-build" ->
            {
                System.out.println(commandlets[1]);
                haveFieldsMarked(commandlets[1].toLowerCase());
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

    private void haveFieldsMarked(String propertyType)
    {
        world.unmarkAll();
        Player current = players.current();
        int marked = world.mark(switch (propertyType)
        {
            case "town", "village", "barracks" ->
            {
                yield (Field field) ->
                {
                    return field.getOwner() == current && field.getType().isPlains();
                };
            }
            case "farmfield" ->
            {
                yield (Field field) ->
                {
                    if (field.getOwner() != current || !field.getType().isPlains())
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
                    return field.getOwner() == current && field.getType().isMountainous();
                };
            }
            case "shipyard" ->
            {
                yield (Field field) ->
                {
                    if (field.getOwner() != current || ! field.getType().isPlains())
                    {
                        return false;
                    }

                    Field[] neighbors = getNeighboringFields(field);
                    for (var neighbor : neighbors)
                    {
                        if (neighbor.getType().isMarine())
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
                    return field.getOwner() == current && field.getType().isContinental();
                };
            }
            default ->
            {
                yield (whatever) -> false;
            }
        });
        if (marked == 0)
        {
            inform(propertyType);
        }
    }

    private void inform(String propertyType)
    {
        String message = switch (propertyType)
        {
            case "town", "village", "barracks" ->
            {
                yield """
                      To build a town, a village or barracks,
                      you need a land field.
                      """;
            }
            case "farmfield" ->
            {
                yield """
                      To build a farmfield, you need a land field
                      that is adjacent to a village.
                      """;
            }
            case "mine" ->
            {
                yield """
                      To build a mine, you need a mountain field.
                      """;
            }
            case "shipyard" ->
            {
                yield """
                      To build a shipyard, you need a land field
                      that is adjacent to a see field.
                      """;
            }
            case "fortress" ->
            {
                yield """
                      To build a fortress, you need a land field or
                      a mountain field.
                      """;
            }
            default ->
            {
                yield "";
            }
        };
        if (!message.isEmpty())
        {
            JOptionPane.showMessageDialog(this, message, "Information",
                    JOptionPane.INFORMATION_MESSAGE);
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
