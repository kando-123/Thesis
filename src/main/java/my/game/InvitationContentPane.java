package my.game;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;
import my.i18n.Dictionary;
import my.i18n.Statement;

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
        
        Dictionary dictionary = Dictionary.getInstance();
        
        /* Logo. */
        GridBagConstraints c;
        try
        {
            InputStream stream = getClass().getResourceAsStream("/Logo/Logo.png");
            Image image = ImageIO.read(stream);
            Icon icon = new ImageIcon(image);
            JLabel imageLabel = new JLabel(icon);
            c = new GridBagConstraints();
            c.gridx = 0;
            c.gridy = 0;
            c.gridwidth = 3;
            c.gridheight = 1;
            c.fill = GridBagConstraints.BOTH;
            add(imageLabel, c);
        }
        catch (IOException e)
        {
            System.err.println(e);
        }
        
        /* Language selection button. */
        JButton languageButton = new JButton();
        String text = dictionary.translate(Statement.LANGUAGE);
        languageButton.setText(text);
        languageButton.setActionCommand("select-language");
        languageButton.addActionListener(master);
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 1;
        c.gridheight = 2;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 2.0;
        c.weighty = 1.0;
        add(languageButton, c);
        
        /* Play button. */
        JButton playButton = new JButton();
        text = dictionary.translate(Statement.PLAY);
        playButton.setText(text);
        playButton.setActionCommand("play");
        playButton.addActionListener(master);
        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 1;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 4.0;
        c.weighty = 1.0;
        add(playButton, c);
        
        /* Join button. */
        JButton joinButton = new JButton();
        text = dictionary.translate(Statement.JOIN);
        joinButton.setText(text);
        joinButton.setActionCommand("join");
        joinButton.addActionListener(master);
        c = new GridBagConstraints();
        c.gridx = 2;
        c.gridy = 1;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 4.0;
        c.weighty = 1.0;
        add(joinButton, c);
    }
}
