package ge.gui;

import ge.utilities.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;

/**
 *
 * @author Kay Jay O'Nail
 */
public class InvitationContentPane extends JPanel implements ActionListener
{
    private final Invoker<ConfigManager> invoker;
    
    public InvitationContentPane(Invoker<ConfigManager> invoker)
    {
        super(new GridBagLayout());
        
        this.invoker = invoker;
        
        var c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.BOTH;
        try
        {
            var stream = getClass().getResourceAsStream("/Logo/Logo.png");
            var image = ImageIO.read(stream);
            var icon = new ImageIcon(image);
            var imageLabel = new JLabel(icon);
            add(imageLabel, c);
        }
        catch (IOException e)
        {
            add(new JLabel(e.toString()), c);
        }
        
        ++c.gridy;
        var playButton = new JButton();
        playButton.setText("Play");
        playButton.setActionCommand("->players");
        playButton.addActionListener(this);
        add(playButton, c);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (e.getActionCommand().equals("->players"))
        {
            invoker.invoke(new BeginPlayerConfigCommand());
        }
    }
}
