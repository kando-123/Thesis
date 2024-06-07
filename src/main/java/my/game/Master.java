package my.game;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import my.gameplay.*;
import my.player.*;
import my.player.selection.*;
import my.world.*;

/**
 *
 * @author Kay Jay O'Nail
 */
public class Master extends JFrame implements ActionListener
{
    private State state;

    private PlayerConfigurationContentPane playerContentPane;
    private WorldConfigurationContentPane worldContentPane;
    private GameplayContentPane gameplayContentPane;

    private World world;

    private Master()
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
        if (state == State.INITIAL)
        {
            Dimension newDimension = getSize();
            newDimension.width *= 0.75;
            newDimension.height *= 0.75;

            playerContentPane = new PlayerConfigurationContentPane();
            playerContentPane.setPreferredSize(newDimension);
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
                worldContentPane = new WorldConfigurationContentPane();
                worldContentPane.setPreferredSize(getSize());
                setContentPane(worldContentPane);
                pack();
                setLocationRelativeTo(null);

                state = State.WORLD_PARAMETERIZATION;
            }
        }
    }

    private void beginGameplay()
    {
        if (state == State.WORLD_PARAMETERIZATION)
        {
            WorldConfiguration configuration = worldContentPane.getConfiguration();
            world = new World(configuration);
            gameplayContentPane = new GameplayContentPane(world);
            setContentPane(gameplayContentPane);
            setResizable(true);
            pack();
            setLocationRelativeTo(null);

            state = State.GAMEPLAY;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        switch (e.getActionCommand())
        {
            case "play" ->
            {
                beginPlayerSelection();
            }
            case "players-configured" ->
            {
                beginWorldConfiguration();
            }
            case "world-configured" ->
            {
                beginGameplay();
            }

            case "END" ->
            {
                System.out.println("It might not look like but the game is over.");

                ThreadPool pool = ThreadPool.getInstance();
                pool.joinAll();
            }
        }
    }

}
