package my.game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import my.i18n.LanguageSelectionContentPane;
import javax.swing.*;
import my.player.selection.PlayerSelectionContentPane;
import my.world.WorldParameterizationContentPane;

/**
 *
 * @author Kay Jay O'Nail
 */
public class Master extends JFrame implements ActionListener
{
    private GameMode gameMode;

    private int localPlayersCount;
    private int remotePlayersCount;
    private int botPlayersCount;

    private Master()
    {
        gameMode = null;
        
        try
        {
            InputStream stream = getClass().getResourceAsStream("/Logo/Icon.png");
            BufferedImage image = ImageIO.read(stream);
            setIconImage(image);
        }
        catch (IOException io)
        {
            
        }
        
//        JMenuBar menuBar = new JMenuBar();
//        
//        JMenu languageMenu = new JMenu("Language");
//        JMenuItem english = new JMenuItem("English");
//        english.setActionCommand("English");
//        english.addActionListener(this);
//        JMenuItem polish = new JMenuItem("Polski");
//        polish.setActionCommand("Polish");
//        polish.addActionListener(this);
//        languageMenu.add(english);
//        languageMenu.add(polish);
//        menuBar.add(languageMenu);
//        setJMenuBar(menuBar);
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
                JPanel newContentPane = new LanguageSelectionContentPane();
                newContentPane.setPreferredSize(getSize());
                setContentPane(newContentPane);
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

                JPanel newContentPane = new PlayerSelectionContentPane();
                newContentPane.setPreferredSize(getSize());
                setContentPane(newContentPane);
                pack();
                setLocationRelativeTo(null);
            }
            case "join" ->
            {
                gameMode = GameMode.GUEST;

                JPanel newContentPane = new PlayerSelectionContentPane();
                newContentPane.setPreferredSize(getSize());
                setContentPane(newContentPane);
                pack();
                setLocationRelativeTo(null);
            }
            case "players-selected" ->
            {
                if (gameMode == GameMode.HOST)
                {
                    assert (getContentPane().getClass() == PlayerSelectionContentPane.class);
                    var pane = (PlayerSelectionContentPane) getContentPane();
                    localPlayersCount = pane.getLocalPlayersCount();
                    remotePlayersCount = pane.getRemotePlayersCount();
                    botPlayersCount = pane.getBotsPlayersCount();
                    if (localPlayersCount + remotePlayersCount + botPlayersCount > 1)
                    {
                        setContentPane(new WorldParameterizationContentPane());
                        pack();
                        setLocationRelativeTo(null);
                    }
                    else
                    {
                        JOptionPane.showMessageDialog(this, "Select at least 2 players.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }

            }
        }
    }
}
