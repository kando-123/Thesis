package my.game;

import java.awt.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;

/**
 *
 * @author Kay Jay O'Nail
 */
public class InvitationContentPane extends JPanel
{
    private final Master master;
    
    public InvitationContentPane()
    {
        super(new GridBagLayout());
        
        master = Master.getInstance();
        
        /* Logo. */
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.BOTH;
        try
        {
            InputStream stream = getClass().getResourceAsStream("/Logo/Logo.png");
            Image image = ImageIO.read(stream);
            Icon icon = new ImageIcon(image);
            JLabel imageLabel = new JLabel(icon);
            add(imageLabel, c);
        }
        catch (IOException e)
        {
            add(new JLabel(e.toString()), c);
        }
        
        /* Play button. */
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 1;
        c.fill = GridBagConstraints.BOTH;
        JButton playButton = new JButton();
        playButton.setText("Play");
        playButton.setActionCommand("->players");
        playButton.addActionListener(master);
        add(playButton, c);
    }
}
