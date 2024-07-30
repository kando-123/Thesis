package my.game;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import my.player.Player;
import my.player.PlayerConfiguration;
import my.player.PlayerConfigurationContentPane;
import my.player.PlayersQueue;
import my.utils.Hex;
import my.world.InputHandler;
import my.world.World;
import my.world.WorldConfigurationContentPane;

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

    private Manager manager;
    private World world;
    private PlayersQueue players;

    public Master()
    {
        state = State.INITIAL;
        manager = new Manager();
        
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
    
    public Manager getManager()
    {
        return manager;
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
            List<PlayerConfiguration> playersData = playerContentPane.getPlayerParameters();
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
                Player current = players.current();
                var set = world.getBuildableProperties(current);
                JDialog buyDialog = new PropertiesDialog(this, set);
                buyDialog.setLocationRelativeTo(this);
                buyDialog.setVisible(true);
                buyDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                requestFocus();
            }
            case "do-build" ->
            {
                requestFocus();
            }
            case "to-hire" ->
            {
                System.out.println("Master: I received information that an entity shall be hired.");
                requestFocus();
            }
            case "do-hire" ->
            {
                requestFocus();
            }
        }
    }
}
