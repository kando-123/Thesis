 /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package my.game;

import java.awt.Dimension;
import javax.swing.*;

/**
 *
 * @author Kay Jay O'Nail
 */
public class Master extends JFrame
{
    private GameMode gameMode = null;
    
    private LanguagePanel languagePanel = null;
    
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
    
    public void serveAction(String command)
    {
        switch (command)
        {
            case "select language" ->
            {
                if (languagePanel == null)
                {
                    languagePanel = new LanguagePanel(this);
                    Dimension frameDimension = getSize();
                    languagePanel.setPreferredSize(frameDimension);
                }
                setContentPane(languagePanel);
                pack();
                setLocationRelativeTo(null);
            }
            case "language selected" ->
            {
                setContentPane(new InvitationPanel(this));
                pack();
                setLocationRelativeTo(null);
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
