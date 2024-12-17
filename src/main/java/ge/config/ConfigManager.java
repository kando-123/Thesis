package ge.config;

import ge.main.*;
import ge.utilities.*;
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;

/**
 * Manager of the configuration stage of the game.
 * 
 * The object administers the frame it receives on construction. When the configuration is
 * finished, the engine is informed by appropriate command.
 * 
 * @author Kay Jay O'Nail
 */
public class ConfigManager
{
    /**
     * Invoker to the Engine object that created this ConfigManager.
     */
    private final Invoker<Engine> invoker;

    /**
     * Frame where the manager puts views.
     */
    private JFrame frame;

    /**
     * Content pane used at player configuration.
     */
    private PlayerConfigContentPane playerContentPane;
    
    /**
     * Content pane used at world configuration.
     */
    private WorldConfigContentPane worldContentPane;

    /**
     * Size of the content pane.
     */
    private final Dimension contentPaneSize;

    /**
     * Constructor.
     * 
     * @param frame frame the manager will use
     * @param invoker invoker the manager will call the engine through
     */
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

    /**
     * Begins or comes back to the player configuration content pane.
     */
    void doPlayerConfig()
    {
        if (playerContentPane == null)
        {
            playerContentPane = new PlayerConfigContentPane(new Invoker<>(this));
            playerContentPane.setPreferredSize(contentPaneSize);
        }
        frame.setContentPane(playerContentPane);
        frame.pack();
    }

    /**
     * Begins or comes back to the world configuration content pane.
     */
    void doWorldConfig()
    {
        if (worldContentPane == null)
        {
            worldContentPane = new WorldConfigContentPane(new Invoker<>(this));
            worldContentPane.setPreferredSize(contentPaneSize);
        }
        frame.setContentPane(worldContentPane);
        frame.pack();
    }

    /**
     * Releases configuration content panes and invokes the engine. This method should
     * terminate the life of this object.
     */
    void finishConfiguration()
    {
        var playerConfigs = playerContentPane.getConfigs();
        playerContentPane = null;

        var worldConfig = worldContentPane.getConfig();
        worldContentPane = null;
        
        invoker.invoke(new BeginGameplayCommand(playerConfigs, worldConfig));
    }
}