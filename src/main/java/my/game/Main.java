package my.game;

import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;
import my.gameplay.WorldPanel;
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

        InvitationContentPane invitationPanel = new InvitationContentPane();
        master.setContentPane(invitationPanel);
        master.pack();
        
        InputHandler inputHandler = InputHandler.getInstance();
        master.addKeyListener(inputHandler);
        
        master.setVisible(true);
        master.setLocationRelativeTo(null);
    }
}
