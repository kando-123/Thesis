package my.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Kay Jay O'Nail
 */
public class InvitationContentPane extends JPanel
{
    private final Master master;
    
    public InvitationContentPane(Master master)
    {
        super(new GridBagLayout());
        
        this.master = master;
        
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
