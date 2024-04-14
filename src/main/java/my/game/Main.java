/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package my.game;

import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;
import my.input.InputHandler;

/**
 *
 * @author Kay Jay O'Nail
 */
public class Main
{
    public static void main(String[] args)
    {
        Master master = Master.getInstance();
        
        master.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        master.setTitle("The Global Empire");
        master.setResizable(false);
        
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        int frameWidth = (int) ((double) screenSize.width / Math.sqrt(2.0));
        int frameHeight = (int) ((double) screenSize.height / Math.sqrt(2.0));
        Dimension frameSize = new Dimension(frameWidth, frameHeight);
        master.setSize(frameSize);
        
        InvitationPanel invitationPanel = new InvitationPanel(master);
        master.setContentPane(invitationPanel);
        master.pack();
        
//        GamePanel panel = new GamePanel(frameSize);
//        master.setContentPane(panel);
//        master.addComponentListener(panel);
        
        InputHandler inputHandler = InputHandler.getInstance();
        master.addKeyListener(inputHandler);
        
        master.setVisible(true);
        master.setLocationRelativeTo(null);
        
//        panel.startGameThread();
    }
}
