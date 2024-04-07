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

/**
 *
 * @author Kay Jay O'Nail
 */
public class InvitationPanel extends JPanel implements ActionListener
{
    private JFrame parentFrame;
    
    public InvitationPanel(JFrame parentFrame)
    {
        super(new GridBagLayout());
        
        this.parentFrame = parentFrame;
        
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
            add(imageLabel, c);
        }
        catch (IOException e)
        {
            System.err.println(e);
        }
        
        /* Singleplayer button. */
        JButton singleplayerButton = new JButton("Singleplayer");
        singleplayerButton.setActionCommand("singleplayer");
        singleplayerButton.addActionListener(this);
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = GridBagConstraints.REMAINDER;
        add(singleplayerButton, c);
        
        /* Multiplayer-host button. */
        JButton hostButton = new JButton("Multiplayer: Host");
        hostButton.setActionCommand("host");
        hostButton.addActionListener(this);
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 2;
        c.weightx = 1.0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.CENTER;
        add(hostButton, c);
        
        /* Multiplayer-host button. */
        JButton guestButton = new JButton("Multiplayer: Guest");
        guestButton.setActionCommand("guest");
        guestButton.addActionListener(this);
        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 2;
        c.weightx = 1.0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.CENTER;
        add(guestButton, c);
    }
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        System.out.println(e.getActionCommand());
    }
}
