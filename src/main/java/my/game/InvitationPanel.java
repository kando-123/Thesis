/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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
public class InvitationPanel extends JPanel implements ActionListener
{
    private final Master master;
    
    public InvitationPanel(Master parentFrame)
    {
        super(new GridBagLayout());
        
        master = parentFrame;
        
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
            c.gridwidth = GridBagConstraints.REMAINDER;
            c.gridheight = 1;
            c.fill = GridBagConstraints.BOTH;
            add(imageLabel, c);
        }
        catch (IOException e)
        {
            System.err.println(e);
        }
        
        /* Singleplayer button. */
        JButton singleplayerButton = new JButton();
        String text = dictionary.translate(Statement.LOCAL);
        singleplayerButton.setText(text);
        singleplayerButton.setActionCommand("local");
        singleplayerButton.addActionListener(this);
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 2;
        c.gridheight = 1;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 20.0;
        c.weighty = 1.0;
        add(singleplayerButton, c);
        
        /* Language selection button. */
        JButton languageButton = new JButton();
        text = dictionary.translate(Statement.LANGUAGE);
        languageButton.setText(text);
        languageButton.setActionCommand("select language");
        languageButton.addActionListener(this);
        c = new GridBagConstraints();
        c.gridx = 2;
        c.gridy = 1;
        c.gridwidth = 1;
        c.gridheight = 2;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;
        add(languageButton, c);
        
        /* Multiplayer-host button. */
        JButton hostButton = new JButton();
        text = dictionary.translate(Statement.REMOTE_HOST);
        hostButton.setText(text);
        hostButton.setActionCommand("host");
        hostButton.addActionListener(this);
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 10.0;
        c.weighty = 1.0;
        add(hostButton, c);
        
        /* Multiplayer-host button. */
        JButton guestButton = new JButton();
        text = dictionary.translate(Statement.REMOTE_GUEST);
        guestButton.setText(text);
        guestButton.setActionCommand("guest");
        guestButton.addActionListener(this);
        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 2;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 10.0;
        c.weighty = 1.0;
        add(guestButton, c);
    }
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        master.serveAction(e.getActionCommand());
    }
}
