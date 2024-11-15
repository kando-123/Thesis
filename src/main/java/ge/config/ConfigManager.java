package ge.config;

import ge.main.BeginGameplayCommand;
import ge.main.Engine;
import ge.utilities.*;
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;

/**
 *
 * @author Kay Jay O'Nail
 */
public class ConfigManager
{
    private final Invoker<Engine> invoker;

    private JFrame frame;

    private PlayerConfigContentPane playerContentPane;
    private WorldConfigContentPane worldContentPane;

    private final Dimension contentPaneSize;

    public ConfigManager(JFrame frame, Invoker<Engine> invoker)
    {
        this.invoker = invoker;

        this.frame = frame;

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("The Global Empire");
        frame.setResizable(false);

        var pane = new InvitationContentPane(new Invoker<>(this));
        frame.setContentPane(pane);
        frame.pack();

        contentPaneSize = pane.getSize();

        frame.setVisible(true);
        frame.setLocationRelativeTo(null);

        try
        {
            InputStream stream = getClass().getResourceAsStream("/Logo/Icon.png");
            BufferedImage image = ImageIO.read(stream);
            frame.setIconImage(image);
        }
        catch (IOException io)
        {
            /* Never thrown. */
        }
    }

    void beginPlayerConfig()
    {
        if (playerContentPane == null)
        {
            playerContentPane = new PlayerConfigContentPane(new Invoker<>(this));
            playerContentPane.setPreferredSize(contentPaneSize);
        }
        frame.setContentPane(playerContentPane);
        frame.pack();
    }

    void beginWorldConfig()
    {
        if (worldContentPane == null)
        {
            worldContentPane = new WorldConfigContentPane(new Invoker<>(this));
            worldContentPane.setPreferredSize(contentPaneSize);
        }
        frame.setContentPane(worldContentPane);
        frame.pack();
    }

    void finishConfiguration()
    {
        var playerConfigs = playerContentPane.getConfigs();
        playerContentPane = null;

        var worldConfig = worldContentPane.getConfig();
        worldContentPane = null;
        
        invoker.invoke(new BeginGameplayCommand(playerConfigs, worldConfig));
    }
}