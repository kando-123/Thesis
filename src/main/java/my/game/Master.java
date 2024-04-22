package my.game;

import my.i18n.LanguagePanel;
import javax.swing.*;
import my.player.selection.PlayerSelection;

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
    
    public void serveAction(String command)
    {
        switch (command)
        {
            case "select language" ->
            {
                setContentPane(new LanguagePanel(this));
                pack();
                setLocationRelativeTo(null);
            }
            case "language selected" ->
            {
                setContentPane(new InvitationPanel(this));
                pack();
                setLocationRelativeTo(null);
            }
            case "local" ->
            {
                gameMode = GameMode.LOCAL;
                
                setContentPane(new PlayerSelection(gameMode));
                pack();
                setLocationRelativeTo(null);
            }
            case "host" ->
            {
                gameMode = GameMode.REMOTE_HOST;
                
                setContentPane(new PlayerSelection(gameMode));
                pack();
                setLocationRelativeTo(null);
            }
            case "guest" ->
            {
                gameMode = GameMode.REMOTE_GUEST;
                
                setContentPane(new PlayerSelection(gameMode));
                pack();
                setLocationRelativeTo(null);
            }

        }
    }
}
