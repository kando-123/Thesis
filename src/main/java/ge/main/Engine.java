package ge.main;

import ge.gui.GUIManager;
import ge.player.PlayerManager;
import ge.world.WorldManager;

/**
 *
 * @author Kay Jay O'Nail
 */
public class Engine
{
    public static void main(String[] args)
    {
        var guiManager = new GUIManager();
        var worldManager = new WorldManager();
        var playerManager = new PlayerManager();
    }
}
