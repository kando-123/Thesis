/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package my.game;

import java.awt.*;
import javax.swing.*;
import my.input.*;

/**
 *
 * @author Kay Jay O'Nail
 */
public class Thesis
{
    public static void main(String[] args)
    {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("The Global Empire");  
        
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        int frameWidth = (int) ((double) screenSize.width / Math.sqrt(2.0));
        int frameHeight = (int) ((double) screenSize.height / Math.sqrt(2.0));
        Dimension frameSize = new Dimension(frameWidth, frameHeight);
        frame.setSize(frameSize);
        
//        InvitationPanel panel = new InvitationPanel();
//        frame.setContentPane(panel);
//        frame.pack();
        
        GamePanel panel = new GamePanel(frameSize);
        frame.setContentPane(panel);
        frame.addComponentListener(panel);
        frame.pack();
        
        InputHandler inputHandler = InputHandler.getInstance();
        frame.addKeyListener(inputHandler);
        
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        
        panel.startGameThread();
    }
}
