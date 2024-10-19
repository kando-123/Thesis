package ge.main;

import ge.config.ConfigManager;
import ge.manager.*;
import ge.player.*;
import ge.utilities.*;
import ge.world.*;
import javax.swing.*;

/**
 *
 * @author Kay Jay O'Nail
 */
public class Engine
{
    public static void main(String[] args)
    {
        var engine = new Engine();
    }
    
    private GUIManager guiManager;
    private WorldManager worldManager;
    private PlayerManager playerManager;
    
    private Engine()
    {
        var configManager = new ConfigManager(new Invoker<>(this));
    }
    
    void beginGameplay(JFrame frame, WorldConfig worldConfig, PlayerConfig[] playerConfigs)
    {
        var builder = new ManagerBuilder();
        
        builder.setGUIFrame(frame);
        builder.setWorldConfig(worldConfig);
        builder.setPlayerConfigs(playerConfigs);
        
        builder.build();
        
        guiManager = builder.getGUIManager();
        worldManager = builder.getWorldManager();
        playerManager = builder.getPlayerManager();
        
        assert (guiManager != null);
        assert (worldManager != null);
        assert (playerManager != null);
    }
}
