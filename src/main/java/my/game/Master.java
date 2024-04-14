 /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package my.game;

import javax.swing.*;

/**
 *
 * @author Kay Jay O'Nail
 */
public class Master extends JFrame
{
    private GameMode gameMode = null;
    
    private Master()
    {
        
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
    
    public void serveAction(String actionCommand)
    {
        switch (actionCommand)
        {
            case "language" ->
            {
                
            }
            case "singleplayer" ->
            {
                gameMode = GameMode.SINGLEPLAYER;
                // ...
            }
            case "host" ->
            {
                gameMode = GameMode.MULTIPLAYER_HOST;
                // ...
            }
            case "guest" ->
            {
                gameMode = GameMode.MULTIPLAYER_GUEST;
                // ...
            }
        }
    }
}
