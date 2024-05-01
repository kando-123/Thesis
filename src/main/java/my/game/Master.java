package my.game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import my.i18n.LanguageSelectionContentPane;
import javax.swing.*;
import my.player.selection.PlayerSelectionContentPane;

/**
 *
 * @author Kay Jay O'Nail
 */
public class Master extends JFrame implements ActionListener
{
    private GameMode gameMode;
    
    private Master()
    {
        gameMode = null;
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
    
    public GameMode getGameMode()
    {
        return gameMode;
    }
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        switch (e.getActionCommand())
        {
            case "select-language" ->
            {
                setContentPane(new LanguageSelectionContentPane());
                pack();
                setLocationRelativeTo(null);
            }
            case "language-selected" ->
            {
                setContentPane(new InvitationContentPane());
                pack();
                setLocationRelativeTo(null);
            }
            case "play" ->
            {
                gameMode = GameMode.HOST;
                
                setContentPane(new PlayerSelectionContentPane());
                pack();
                setLocationRelativeTo(null);
            }
            case "join" ->
            {
                gameMode = GameMode.GUEST;
                
                setContentPane(new PlayerSelectionContentPane());
                pack();
                setLocationRelativeTo(null);
            }
            case "players-selected" ->
            {
                
            }
        }
    }
}
