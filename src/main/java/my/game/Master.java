package my.game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import my.player.*;
import my.player.selection.*;

/**
 *
 * @author Kay Jay O'Nail
 */
public class Master extends JFrame implements ActionListener
{
    private JPanel contentPane;
    private List<AbstractPlayer> players;

    private Master()
    {
        try
        {
            InputStream stream = getClass().getResourceAsStream("/Logo/Icon.png");
            BufferedImage image = ImageIO.read(stream);
            setIconImage(image);
        }
        catch (IOException io)
        {

        }
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

    @Override
    public void actionPerformed(ActionEvent e)
    {
        switch (e.getActionCommand())
        {
            case "play" ->
            {
                contentPane = new PlayerSelectionContentPane();
                contentPane.setPreferredSize(getSize());
                setContentPane(contentPane);
                pack();
                setLocationRelativeTo(null);
            }
            case "players-selected" ->
            {
                List<PlayerParameters> playersData = ((PlayerSelectionContentPane) contentPane).getPlayerParameters();
                if (playersData.size() < 2)
                {
                    JOptionPane.showMessageDialog(this, "Select at least 2 players.", "Error", JOptionPane.ERROR_MESSAGE);
                }
                else
                {
                    for (var data : playersData)
                    {
                        assert (data.type != null);

                        AbstractPlayer player =
                        switch (data.type)
                        {
                            case USER ->
                            {
                                yield new UserPlayer();
                            }
                            case BOT ->
                            {
                                yield new BotPlayer();
                            }
                        };
                    }
                }
            }
            case "world-parameters-selected" ->
            {

            }
        }
    }
}
