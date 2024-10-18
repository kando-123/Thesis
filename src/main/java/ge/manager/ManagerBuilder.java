package ge.manager;

import ge.player.PlayerConfig;
import ge.world.WorldConfig;
import javax.swing.*;

/**
 *
 * @author Kay Jay O'Nail
 */
public class ManagerBuilder
{
    private JFrame frame;
    private WorldConfig worldConfig;
    private PlayerConfig[] playerConfigs;
    
    public ManagerBuilder()
    {
        
    }
    
    public void setGUIFrame(JFrame frame)
    {
        this.frame = frame;
    }

    public void setWorldConfig(WorldConfig worldConfig)
    {
        this.worldConfig = worldConfig;
    }

    public void setPlayerConfigs(PlayerConfig[] playerConfigs)
    {
        this.playerConfigs = playerConfigs;
    }
    
    private GUIManager guiManager;
    private WorldManager worldManager;
    private PlayerManager playerManager;
    
    public static class LackingGUIFrameException extends RuntimeException {}
    public static class LackingWorldConfigException extends RuntimeException {}
    public static class LackingPlayerConfigsException extends RuntimeException {}
    
    public void build()
    {
        if (frame == null)
        {
            throw new LackingGUIFrameException();
        }
        else if (worldConfig == null)
        {
            throw new LackingWorldConfigException();
        }
        else if (playerConfigs == null)
        {
            throw new LackingPlayerConfigsException();
        }
        else
        {
            guiManager = new GUIManager(frame);
            worldManager = new WorldManager(worldConfig);
            playerManager = new PlayerManager(playerConfigs);
            
            /* Mutually relate the managers... */
        }
    }
    
    public GUIManager getGUIManager()
    {
        return guiManager;
    }
    
    public WorldManager getWorldManager()
    {
        return worldManager;
    }
    
    public PlayerManager getPlayerManager()
    {
        return playerManager;
    }
}
